package community.whatever.onembackendkotlin.application

import community.whatever.onembackendkotlin.application.dto.BlockedDomainCreateRequest
import community.whatever.onembackendkotlin.application.dto.ShortenUrlCreateRequest
import community.whatever.onembackendkotlin.application.dto.ShortenUrlSearchRequest
import community.whatever.onembackendkotlin.application.exception.DomainAlreadyBlockedException
import community.whatever.onembackendkotlin.application.exception.UrlNotFoundException
import community.whatever.onembackendkotlin.domain.ShortenedUrl
import community.whatever.onembackendkotlin.domain.ShortenedUrlRepository
import community.whatever.onembackendkotlin.infra.repository.BlockedDomainInMemoryRepository
import community.whatever.onembackendkotlin.infra.repository.ShortenedUrlInMemoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DefaultUrlShortenServiceTest {

    private lateinit var shortenedUrlRepository: ShortenedUrlRepository
    private lateinit var blockedDomainService: BlockedDomainService
    private lateinit var urlShortenService: UrlShortenService

    @BeforeEach
    fun setUp() {
        shortenedUrlRepository = ShortenedUrlInMemoryRepository()
        blockedDomainService = DefaultBlockedDomainService(BlockedDomainInMemoryRepository())
        urlShortenService = DefaultUrlShortenService(shortenedUrlRepository, blockedDomainService)
    }

    @DisplayName("ShortenedUrl 저장")
    @Nested
    inner class SaveShortenUrl {

        private lateinit var originUrl: String
        private lateinit var shortenedUrl: ShortenedUrl
        private lateinit var savedShortenedUrl: ShortenedUrl

        @BeforeEach
        fun setUp() {
            originUrl = "https://www.google.com"
            shortenedUrl = ShortenedUrl(originUrl)
        }

        @Test
        fun `저장된 url이 없으면 새로 저장하고 키를 반환한다`() {
            // when
            val result = urlShortenService.saveShortenUrl(ShortenUrlCreateRequest(originUrl))

            // then
            assertThat(result.shortenedUrl).isNotNull
        }

        @Test
        fun `이미 저장된 url이 있으면 찾아서 키를 반환한다`() {
            // given
            val expect = urlShortenService.saveShortenUrl(ShortenUrlCreateRequest(originUrl))

            // when
            val result = urlShortenService.saveShortenUrl(ShortenUrlCreateRequest(originUrl))

            // then
            assertThat(result).isEqualTo(expect)
        }

        @Test
        fun `차단된 도메인이면 예외를 발생시킨다`() {
            // given
            blockedDomainService.saveBlockedDomain(BlockedDomainCreateRequest(originUrl))

            // when, then
            assertThatThrownBy { urlShortenService.saveShortenUrl(ShortenUrlCreateRequest(originUrl)) }
                .isInstanceOf(DomainAlreadyBlockedException::class.java)
        }
    }

    @DisplayName("원본 URL을 찾기")
    @Nested
    inner class GetOriginUrl {

        private lateinit var originUrl: String
        private lateinit var shortenedUrl: ShortenedUrl

        @BeforeEach
        fun setUp() {
            originUrl = "https://www.google.com"
            shortenedUrl = ShortenedUrl(originUrl)
        }

        @Test
        fun `id에 해당하는 원본 URL을 찾아서 반환한다`() {
            // given
            val expect = shortenedUrlRepository.save(shortenedUrl)

            // when
            val result = urlShortenService.getOriginUrl(ShortenUrlSearchRequest(requireNotNull(expect.id)))

            // then
            assertThat(result.originUrl).isEqualTo(originUrl)
        }

        @Test
        fun `id에 해당하는 원본 URL이 없으면 예외를 발생시킨다`() {
            // when, then
            assertThatThrownBy { urlShortenService.getOriginUrl(ShortenUrlSearchRequest("not-exist-id")) }
                .isInstanceOf(UrlNotFoundException::class.java)
        }
    }
}
