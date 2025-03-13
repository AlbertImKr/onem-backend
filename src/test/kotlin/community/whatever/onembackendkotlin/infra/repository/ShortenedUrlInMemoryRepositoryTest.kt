package community.whatever.onembackendkotlin.infra.repository

import community.whatever.onembackendkotlin.domain.ShortenedUrl
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ShortenedUrlInMemoryRepositoryTest {

    private lateinit var repository: ShortenedUrlInMemoryRepository
    private val faker = Faker()

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
            val originUrl: String = faker.internet().url()
            val shortenedUrl = ShortenedUrl(originUrl)
            val savedShortenedUrl = repository.save(shortenedUrl)

            // when
            val foundShortenedUrl = repository.findById(savedShortenedUrl.id!!)

            // then
            assertThat(foundShortenedUrl).isEqualTo(savedShortenedUrl)
        }

        @Test
        fun `존재하지 않는 아이디로 찾으면 null을 반환한다`() {
            // when
            val notFoundShortenedUrl = faker.internet().url()
            val foundShortenedUrl = repository.findById(notFoundShortenedUrl)

            // then
            assertThat(foundShortenedUrl).isNull()
        }
    }

    @DisplayName("shortenedUrl을 저장")
    @Nested
    inner class Save {

        @Test
        fun `ShortenedUrl을 저장하면 저장된 ShortenedUrl을 반환한다`() {
            // given
            val originUrl = faker.internet().url()
            val shortenedUrl = ShortenedUrl(originUrl)

            // when
            val savedShortenedUrl = repository.save(shortenedUrl)

            // then
            assertThat(savedShortenedUrl.originUrl).isEqualTo(originUrl)
            assertThat(savedShortenedUrl.id).isNotNull()
        }

        @Test
        fun `ShortenedUrl을 저장하면 저장된 ShortenedUrl의 id는 순차적으로 증가한다`() {
            // given
            val originUrl1 = faker.internet().url()
            val shortenedUrl1 = ShortenedUrl(originUrl1)
            val originUrl2 = faker.internet().url()
            val shortenedUrl2 = ShortenedUrl(originUrl2)

            // when
            val savedShortenedUrl1 = repository.save(shortenedUrl1)
            val savedShortenedUrl2 = repository.save(shortenedUrl2)

            // then
            assertThat(savedShortenedUrl1.id).isNotEqualTo(savedShortenedUrl2.id)
        }
    }

    @DisplayName("원본 URL로 ShortenedUrl 존재 여부를 확인")
    @Nested
    inner class ExistsByOriginUrl {

        @Test
        fun `존재하는 원본 URL로 확인하면 true를 반환한다`() {
            // given
            val originUrl = faker.internet().url()
            val shortenedUrl = ShortenedUrl(originUrl)
            repository.save(shortenedUrl)

            // when
            val exists = repository.existsByOriginUrl(shortenedUrl.originUrl)

            // then
            assert(exists)
        }

        @Test
        fun `존재하지 않는 원본 URL로 확인하면 false를 반환한다`() {
            // when
            val nonExistentOriginUrl = faker.internet().url()
            val exists = repository.existsByOriginUrl(nonExistentOriginUrl)

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
            val originUrl = faker.internet().url()
            val shortenedUrl = ShortenedUrl(originUrl)
            val savedShortenedUrl = repository.save(shortenedUrl)

            // when
            val foundShortenedUrl = repository.findByOriginUrl(shortenedUrl.originUrl)

            // then
            assert(foundShortenedUrl == savedShortenedUrl)
        }

        @Test
        fun `존재하지 않는 원본 URL로 찾으면 null을 반환한다`() {
            // when
            val nonExistentOriginUrl = faker.internet().url()
            val foundShortenedUrl = repository.findByOriginUrl(nonExistentOriginUrl)

            // then
            assert(foundShortenedUrl == null)
        }
    }
}
