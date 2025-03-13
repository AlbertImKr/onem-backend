package community.whatever.onembackendkotlin.presentation

import community.whatever.onembackendkotlin.application.BlockedDomainService
import community.whatever.onembackendkotlin.application.dto.BlockedDomainCheckRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainCreateRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainDeleteRequest
import community.whatever.onembackendkotlin.domain.BlockedDomain
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockedDomainController(private val blockedDomainService: BlockedDomainService) {

    @PostMapping("/blocked-domains/create")
    fun saveBlockedDomain(@RequestBody request: BlockedDomainCreateRequest): ResponseEntity<BlockedDomain> {
        return ResponseEntity.ok(blockedDomainService.save(request))
    }

    @PostMapping("/blocked-domains/check")
    fun isBlockedDomain(@RequestBody request: BlockedDomainCheckRequest): ResponseEntity<IsBlockedDomainResponse> {
        return ResponseEntity.ok(IsBlockedDomainResponse(blockedDomainService.isBlocked(request)))
    }

    @PostMapping("/blocked-domains/delete")
    fun deleteBlockedDomain(@RequestBody request: BlockedDomainDeleteRequest): ResponseEntity<Unit> {
        blockedDomainService.delete(request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/blocked-domains/all")
    fun getAllBlockedDomains(): ResponseEntity<List<BlockedDomain>> {
        return ResponseEntity.ok(blockedDomainService.getAll())
    }
}
