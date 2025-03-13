package community.whatever.onembackendkotlin.domain

interface BlockedDomainRepository {

    fun save(blockedDomain: BlockedDomain): BlockedDomain

    fun existsByDomain(domain: String): Boolean

    fun deleteByDomain(domain: String)

    fun findAll(): List<BlockedDomain>
}
