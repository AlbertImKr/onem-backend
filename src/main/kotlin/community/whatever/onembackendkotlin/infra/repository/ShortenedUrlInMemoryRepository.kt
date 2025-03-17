package community.whatever.onembackendkotlin.infra.repository

import community.whatever.onembackendkotlin.domain.ShortenedUrl
import community.whatever.onembackendkotlin.domain.ShortenedUrlRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

@Repository
class ShortenedUrlInMemoryRepository : ShortenedUrlRepository {

    private val keyPrefix: String = System.getenv("spring.profiles.active") ?: "local"

    private val shortenUrls = mutableMapOf<String, ShortenedUrl>()
    private val seq: AtomicLong = AtomicLong()

    override fun findById(id: String): ShortenedUrl? {
        return shortenUrls[id]
    }

    override fun save(shortenedUrl: ShortenedUrl): ShortenedUrl {
        val id = generateId()
        return shortenedUrl.copy(id = id).also { shortenUrls[id] = it }
    }

    override fun existsByOriginUrl(originUrl: String): Boolean {
        return shortenUrls.values.any { it.originUrl == originUrl && !it.deleted }
    }

    override fun findByOriginUrl(originUrl: String): ShortenedUrl? {
        return shortenUrls.values.find { it.originUrl == originUrl }
    }

    override fun deleteAll() {
        shortenUrls.clear()
    }

    override fun deleteAllByExpiredAtBefore(baseTime: LocalDateTime) {
        shortenUrls.replaceAll { _, url ->
            url.takeUnless { it.expiredAt.isBefore(baseTime) } ?: url.copy(deleted = true)
        }
    }

    private fun generateId(): String {
        return keyPrefix + seq.incrementAndGet()
    }
}
