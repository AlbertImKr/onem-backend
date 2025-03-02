package community.whatever.onembackendkotlin

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ShortenedUrlInMemoryRepositoryTest {

    private lateinit var repository: ShortenedUrlInMemoryRepository

    @BeforeEach
    fun setUp() {
        repository = ShortenedUrlInMemoryRepository()
    }

    @DisplayName("아이디로 원본 URL을 찾기")
    @Nested
    inner class FindById {

        @Test
        fun `존재하는 아이디로 찾으면 해당 ShortenedUrl을 반환한다`() {
            // given
            val shortenedUrl = ShortenedUrl("https://www.google.com")
            val savedShortenedUrl = repository.save(shortenedUrl)

            // when
            val foundShortenedUrl = repository.findById(savedShortenedUrl.id!!)

            // then
            assert(foundShortenedUrl == savedShortenedUrl)
        }

        @Test
        fun `존재하지 않는 아이디로 찾으면 null을 반환한다`() {
            // when
            val foundShortenedUrl = repository.findById("non-exist-id")

            // then
            assert(foundShortenedUrl == null)
        }
    }

    @DisplayName("shortenedUrl을 저장")
    @Nested
    inner class Save {

        @Test
        fun `ShortenedUrl을 저장하면 저장된 ShortenedUrl을 반환한다`() {
            // given
            val shortenedUrl = ShortenedUrl("https://www.google.com")

            // when
            val savedShortenedUrl = repository.save(shortenedUrl)

            // then
            assert(savedShortenedUrl.originUrl == shortenedUrl.originUrl)
            assert(savedShortenedUrl.id != null)
        }

        @Test
        fun `ShortenedUrl을 저장하면 저장된 ShortenedUrl의 id는 순차적으로 증가한다`() {
            // given
            val shortenedUrl1 = ShortenedUrl("https://www.google.com")
            val shortenedUrl2 = ShortenedUrl("https://www.naver.com")

            // when
            val savedShortenedUrl1 = repository.save(shortenedUrl1)
            val savedShortenedUrl2 = repository.save(shortenedUrl2)

            // then
            assert(savedShortenedUrl1.id == "1")
            assert(savedShortenedUrl2.id == "2")
        }
    }

    @DisplayName("원본 URL로 ShortenedUrl 존재 여부를 확인")
    @Nested
    inner class ExistsByOriginUrl {

        @Test
        fun `존재하는 원본 URL로 확인하면 true를 반환한다`() {
            // given
            val shortenedUrl = ShortenedUrl("https://www.google.com")
            repository.save(shortenedUrl)

            // when
            val exists = repository.existsByOriginUrl(shortenedUrl.originUrl)

            // then
            assert(exists)
        }

        @Test
        fun `존재하지 않는 원본 URL로 확인하면 false를 반환한다`() {
            // when
            val exists = repository.existsByOriginUrl("non-exist-origin-url")

            // then
            assert(!exists)
        }
    }

    @DisplayName("원본 URL로 ShortenedUrl을 찾기")
    @Nested
    inner class FindByOriginUrl {

        @Test
        fun `존재하는 원본 URL로 찾으면 해당 ShortenedUrl을 반환한다`() {
            // given
            val shortenedUrl = ShortenedUrl("https://www.google.com")
            val savedShortenedUrl = repository.save(shortenedUrl)

            // when
            val foundShortenedUrl = repository.findByOriginUrl(shortenedUrl.originUrl)

            // then
            assert(foundShortenedUrl == savedShortenedUrl)
        }

        @Test
        fun `존재하지 않는 원본 URL로 찾으면 null을 반환한다`() {
            // when
            val foundShortenedUrl = repository.findByOriginUrl("non-exist-origin-url")

            // then
            assert(foundShortenedUrl == null)
        }
    }
}
