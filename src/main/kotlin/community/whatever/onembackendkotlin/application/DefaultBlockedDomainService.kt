package community.whatever.onembackendkotlin.application

import community.whatever.onembackendkotlin.application.dto.BlockedDomainCheckRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainCreateRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainDeleteRequest
import community.whatever.onembackendkotlin.application.exception.DomainAlreadyBlockedException
import community.whatever.onembackendkotlin.domain.BlockedDomain
import community.whatever.onembackendkotlin.domain.BlockedDomainRepository
import org.springframework.stereotype.Service
import java.net.URL
import java.util.UUID

@Service
class DefaultBlockedDomainService(private val blockedDomainRepository: BlockedDomainRepository) : BlockedDomainService {

    override fun saveBlockedDomain(request: BlockedDomainCreateRequest): BlockedDomain {
        val domain = URL(request.url).host
        if (blockedDomainRepository.existsByDomain(domain)) {
            throw DomainAlreadyBlockedException()
        }
        return blockedDomainRepository.save(BlockedDomain(domain = domain, UUID.randomUUID()))
    }

    override fun isBlockedDomain(request: BlockedDomainCheckRequest): Boolean {
        val domain = URL(request.url).host
        return blockedDomainRepository.existsByDomain(domain)
    }

    override fun deleteBlockedDomain(request: BlockedDomainDeleteRequest) {
        val domain = URL(request.url).host
        blockedDomainRepository.deleteByDomain(domain)
    }

    override fun getAllBlockedDomains(): List<BlockedDomain> {
        return blockedDomainRepository.findAll()
    }
}
