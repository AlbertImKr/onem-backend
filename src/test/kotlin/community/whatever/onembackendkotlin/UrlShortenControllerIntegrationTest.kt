package community.whatever.onembackendkotlin

import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
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
            val key = createUrl(originUrl)
                .expectBody(String::class.java)
                .returnResult()
                .responseBody!!

            // when
            val result = searchShortenUrl(key)

            // then
            result.expectStatus().isOk()
                .expectBody(String::class.java)
                .isEqualTo(originUrl)
        }

        @ParameterizedTest
        @CsvSource(
            "https://www.google.com",
            "https://www.naver.com",
            "https://www.daum.net"
        )
        fun `등록되지 않은 원본 URL을 키로 검색하면 404를 반환한다`(originUrl: String) = runTest {
            // given
            val key = "non-exist-key"

            // when
            val result = searchShortenUrl(key)

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
            val result = createUrl(originUrl)

            // then
            val key = result.expectStatus().isOk()
                .expectBody(String::class.java)
                .returnResult()
                .responseBody!!
            assertThat(key).isNotBlank()
        }

        @ParameterizedTest
        @CsvSource("50", "100")
        fun `동시에 여러 개의 원본 URL을 등록해도 키가 중복되지 않는다`(count: Int) = runTest {
            // given
            val originUrls = (1..count).map { "https://www.google.com/$it" }

            // when
            val keys = originUrls.map { async { createUrlAndReturnKey(it) } }

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
            val key = createUrlAndReturnKey(originUrl)

            // when
            val result = createUrl(originUrl)

            // then
            result.expectStatus().isOk()
                .expectBody(String::class.java)
                .isEqualTo(key)
        }
    }

    private fun searchShortenUrl(key: String) = webTestClient.post()
        .uri("/shorten-url/search")
        .bodyValue(key)
        .exchange()

    private fun createUrl(originUrl: String) = webTestClient.post()
        .uri("/shorten-url/create")
        .bodyValue(originUrl)
        .exchange()

    private fun createUrlAndReturnKey(originUrl: String) = createUrl(originUrl)
        .expectBody(String::class.java)
        .returnResult()
        .responseBody!!
}
