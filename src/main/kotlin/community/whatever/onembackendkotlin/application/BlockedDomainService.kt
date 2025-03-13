package community.whatever.onembackendkotlin.application

import community.whatever.onembackendkotlin.application.dto.BlockedDomainCheckRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainCreateRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainDeleteRequest
import community.whatever.onembackendkotlin.domain.BlockedDomain

interface BlockedDomainService {

    fun save(request: BlockedDomainCreateRequest): BlockedDomain

    fun isBlocked(request: BlockedDomainCheckRequest): Boolean

    fun delete(request: BlockedDomainDeleteRequest)

    fun getAll(): List<BlockedDomain>
}
