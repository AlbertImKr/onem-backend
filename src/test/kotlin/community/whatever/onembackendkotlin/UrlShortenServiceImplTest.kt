package community.whatever.onembackendkotlin

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("UrlShortenServiceImpl 테스트")
class UrlShortenServiceImplTest {

    private val shortenedUrlRepository: ShortenedUrlRepository = mockk()
    private val urlShortenService = UrlShortenServiceImpl(shortenedUrlRepository)

    @DisplayName("ShortenedUrl 저장")
    @Nested
    inner class SaveShortenUrl {

        private lateinit var originUrl: String
        private lateinit var id: String
        private lateinit var shortenedUrl: ShortenedUrl
        private lateinit var savedShortenedUrl: ShortenedUrl

        @BeforeEach
        fun setUp() {
            originUrl = "https://www.google.com"
            id = "abc"
            shortenedUrl = ShortenedUrl(originUrl)
            savedShortenedUrl = shortenedUrl.copy(id = id)
        }

        @Test
        fun `저장된 url이 없으면 새로 저장하고 키를 반환한다`() {
            // given
            every { shortenedUrlRepository.existsByOriginUrl(originUrl) } returns false
            every { shortenedUrlRepository.save(shortenedUrl) } returns savedShortenedUrl

            // when
            val result = urlShortenService.saveShortenUrl(ShortenUrlCreateRequest(originUrl))

            // then
            assertThat(result).isEqualTo(ShortenedUrlResponse(id))
            verify { shortenedUrlRepository.save(shortenedUrl) }
        }

        @Test
        fun `이미 저장된 url이 있으면 찾아서 키를 반환한다`() {
            // given
            every { shortenedUrlRepository.existsByOriginUrl(originUrl) } returns true
            every { shortenedUrlRepository.findByOriginUrl(originUrl) } returns savedShortenedUrl

            // when
            val result = urlShortenService.saveShortenUrl(ShortenUrlCreateRequest(originUrl))

            // then
            assertThat(result).isEqualTo(ShortenedUrlResponse(id))
            verify { shortenedUrlRepository.findByOriginUrl(originUrl) }
        }
    }

    @DisplayName("원본 URL을 찾기")
    @Nested
    inner class GetOriginUrl {

        private lateinit var id: String
        private lateinit var originUrl: String
        private lateinit var shortenedUrl: ShortenedUrl

        @BeforeEach
        fun setUp() {
            id = "abc"
            originUrl = "https://www.google.com"
            shortenedUrl = ShortenedUrl(originUrl, id)
        }

        @Test
        fun `id에 해당하는 원본 URL을 찾아서 반환한다`() {
            // given
            every { shortenedUrlRepository.findById(id) } returns shortenedUrl

            // when
            val result = urlShortenService.getOriginUrl(ShortenUrlSearchRequest(id))

            // then
            assertThat(result).isEqualTo(OriginUrlResponse(originUrl))
            verify { shortenedUrlRepository.findById(id) }
        }

        @Test
        fun `id에 해당하는 원본 URL이 없으면 예외를 발생시킨다`() {
            // given
            every { shortenedUrlRepository.findById(id) } returns null

            // when, then
            assertThatThrownBy { urlShortenService.getOriginUrl(ShortenUrlSearchRequest(id)) }
                .isInstanceOf(UrlNotFoundException::class.java)
            verify { shortenedUrlRepository.findById(id) }
        }
    }
}
