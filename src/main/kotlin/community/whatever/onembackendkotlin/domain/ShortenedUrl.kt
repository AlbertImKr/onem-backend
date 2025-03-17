package community.whatever.onembackendkotlin.domain

import java.time.LocalDateTime

data class ShortenedUrl(
    val originUrl: String,
    val expiredAt: LocalDateTime,
    val id: String? = null,
    val deleted: Boolean = false,
)
