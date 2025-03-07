package community.whatever.onembackendkotlin.presentation

import community.whatever.onembackendkotlin.common.dto.OriginUrlResponse
import community.whatever.onembackendkotlin.common.dto.ShortenUrlCreateRequest
import community.whatever.onembackendkotlin.common.dto.ShortenUrlSearchRequest
import community.whatever.onembackendkotlin.common.dto.ShortenedUrlResponse
import community.whatever.onembackendkotlin.domain.ShortenedUrlRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UrlShortenControllerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var shortenedUrlRepository: ShortenedUrlRepository

    @BeforeEach
    fun setUp() {
        shortenedUrlRepository.deleteAll()
    }

    @DisplayName("원본 URL 조회")
    @Nested
    inner class ShortenUrlSearch {

        @ParameterizedTest
        @CsvSource(
            "https://www.google.com",
            "https://www.naver.com",
            "https://www.daum.net"
        )
        fun `등록된 원본 URL을 키로 검색하면 해당 원본 URL을 반환한다`(originUrl: String) = runTest {
            // given
            val key = createUrl(ShortenUrlCreateRequest(originUrl))
                .expectBody(ShortenedUrlResponse::class.java)
                .returnResult()
                .responseBody!!

            // when
            val result = searchShortenUrl(ShortenUrlSearchRequest(key.shortenedUrl))

            // then
            result.expectStatus().isOk()
                .expectBody(OriginUrlResponse::class.java)
                .isEqualTo(OriginUrlResponse(originUrl))
        }

        @Test
        fun `등록되지 않은 원본 URL을 키로 검색하면 404를 반환한다`() = runTest {
            // given
            val key = "non-exist-key"

            // when
            val result = searchShortenUrl(ShortenUrlSearchRequest(key))

            // then
            result.expectStatus()
                .isNotFound()
        }
    }

    @DisplayName("원본 URL 등록")
    @Nested
    inner class ShortenUrlCreate {

        @ParameterizedTest
        @CsvSource(
            "https://www.google.com",
            "https://www.naver.com",
            "https://www.daum.net"
        )
        fun `원본 URL을 등록하면 키를 반환한다`(originUrl: String) = runTest {
            // when
            val result = createUrl(ShortenUrlCreateRequest(originUrl))

            // then
            val shortenedUrlResponse = result.expectStatus().isOk()
                .expectBody(ShortenedUrlResponse::class.java)
                .returnResult()
                .responseBody!!
            assertThat(shortenedUrlResponse.shortenedUrl).isNotBlank()
        }

        @ParameterizedTest
        @CsvSource("50", "100")
        fun `동시에 여러 개의 원본 URL을 등록해도 키가 중복되지 않는다`(count: Int) = runTest {
            // given
            val originUrls = (1..count).map { "https://www.google.com/$it" }

            // when
            val keys = originUrls.map { async { createUrlAndReturnKey(ShortenUrlCreateRequest(it)) } }
                .map { it.await() }

            // then
            await untilAsserted { assertThat(keys).hasSize(count).doesNotHaveDuplicates() }
        }

        @ParameterizedTest
        @CsvSource(
            "https://www.google.com",
            "https://www.naver.com",
            "https://www.daum.net"
        )
        fun `등록된 원본 URL을 다시 등록하면 기존 키를 반환한다`(originUrl: String) = runTest {
            // given
            val key = createUrlAndReturnKey(ShortenUrlCreateRequest(originUrl))

            // when
            val result = createUrl(ShortenUrlCreateRequest(originUrl))

            // then
            result.expectStatus().isOk()
                .expectBody(String::class.java)
                .isEqualTo(key)
        }
    }

    private fun searchShortenUrl(request: ShortenUrlSearchRequest) = webTestClient.post()
        .uri("/shorten-url/search")
        .bodyValue(request)
        .exchange()

    private fun createUrl(request: ShortenUrlCreateRequest) = webTestClient.post()
        .uri("/shorten-url/create")
        .bodyValue(request)
        .exchange()

    private fun createUrlAndReturnKey(request: ShortenUrlCreateRequest) = createUrl(request)
        .expectBody(String::class.java)
        .returnResult()
        .responseBody!!
}
