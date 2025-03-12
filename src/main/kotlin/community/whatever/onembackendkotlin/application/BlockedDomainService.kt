package community.whatever.onembackendkotlin.application

import community.whatever.onembackendkotlin.application.dto.BlockedDomainCheckRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainCreateRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainDeleteRequest
import community.whatever.onembackendkotlin.domain.BlockedDomain

interface BlockedDomainService {

    fun saveBlockedDomain(request: BlockedDomainCreateRequest): BlockedDomain

    fun isBlockedDomain(request: BlockedDomainCheckRequest): Boolean

    fun deleteBlockedDomain(request: BlockedDomainDeleteRequest)

    fun getAllBlockedDomains(): List<BlockedDomain>
}
