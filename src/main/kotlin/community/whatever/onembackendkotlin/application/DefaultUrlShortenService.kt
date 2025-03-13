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
        if (blockedDomainService.isBlocked(BlockedDomainCheckRequest(originUrl))) {
            throw DomainAlreadyBlockedException()
        }
        shortenedUrlRepository.findByOriginUrl(originUrl)?.id
            ?.let { return ShortenedUrlResponse(it) }
        return shortenedUrlRepository.save(ShortenedUrl(originUrl)).id
            ?.let { ShortenedUrlResponse(it) }
            ?: throw UrlNotFoundException()
    }
}
