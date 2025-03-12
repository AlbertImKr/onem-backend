package community.whatever.onembackendkotlin.application

import community.whatever.onembackendkotlin.application.dto.BlockedDomainCheckRequest
import community.whatever.onembackendkotlin.application.dto.OriginUrlResponse
import community.whatever.onembackendkotlin.application.dto.ShortenUrlCreateRequest
import community.whatever.onembackendkotlin.application.dto.ShortenUrlSearchRequest
import community.whatever.onembackendkotlin.application.dto.ShortenedUrlResponse
import community.whatever.onembackendkotlin.application.exception.DomainAlreadyBlockedException
import community.whatever.onembackendkotlin.application.exception.UrlNotFoundException
import community.whatever.onembackendkotlin.domain.ShortenedUrl
import community.whatever.onembackendkotlin.domain.ShortenedUrlRepository
import org.springframework.stereotype.Service

@Service
class DefaultUrlShortenService(
    private val shortenedUrlRepository: ShortenedUrlRepository,
    private val blockedDomainService: BlockedDomainService,
) : UrlShortenService {

    override fun getOriginUrl(request: ShortenUrlSearchRequest): OriginUrlResponse {
        val id = request.shortenUrl
        return OriginUrlResponse(shortenedUrlRepository.findById(id)?.originUrl ?: throw UrlNotFoundException())
    }

    override fun saveShortenUrl(request: ShortenUrlCreateRequest): ShortenedUrlResponse {
        val originUrl = request.originUrl
        if (blockedDomainService.isBlockedDomain(BlockedDomainCheckRequest(originUrl))) {
            throw DomainAlreadyBlockedException()
        }
        if (shortenedUrlRepository.existsByOriginUrl(originUrl)) {
            val id = requireNotNull(shortenedUrlRepository.findByOriginUrl(originUrl)?.id) { "ID가 존재하지 않습니다." }
            return ShortenedUrlResponse(id)
        }
        val id = requireNotNull(shortenedUrlRepository.save(ShortenedUrl(originUrl)).id) { "ID가 존재하지 않습니다." }
        return ShortenedUrlResponse(id)
    }
}
