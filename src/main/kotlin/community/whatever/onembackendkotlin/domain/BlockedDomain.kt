package community.whatever.onembackendkotlin.domain

import java.net.URL
import java.util.UUID

data class BlockedDomain(val domain: String, val id: UUID) {
    constructor(url: String) : this(domain = URL(url).host, id = UUID.randomUUID())
}
