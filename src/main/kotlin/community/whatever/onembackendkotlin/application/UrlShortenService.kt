package community.whatever.onembackendkotlin.application

import community.whatever.onembackendkotlin.common.dto.OriginUrlResponse
import community.whatever.onembackendkotlin.common.dto.ShortenUrlCreateRequest
import community.whatever.onembackendkotlin.common.dto.ShortenUrlSearchRequest
import community.whatever.onembackendkotlin.common.dto.ShortenedUrlResponse
import community.whatever.onembackendkotlin.common.exception.UrlNotFoundException
import community.whatever.onembackendkotlin.domain.ShortenedUrl
import community.whatever.onembackendkotlin.domain.ShortenedUrlRepository
import org.springframework.stereotype.Service

interface UrlShortenService {

    fun getOriginUrl(request: ShortenUrlSearchRequest): OriginUrlResponse
    fun saveShortenUrl(request: ShortenUrlCreateRequest): ShortenedUrlResponse
}

@Service
class DefaultUrlShortenService(private val shortenedUrlRepository: ShortenedUrlRepository) : UrlShortenService {
    override fun getOriginUrl(request: ShortenUrlSearchRequest): OriginUrlResponse {
        val id = request.shortenUrl
        return OriginUrlResponse(shortenedUrlRepository.findById(id)?.originUrl ?: throw UrlNotFoundException())
    }

    override fun saveShortenUrl(request: ShortenUrlCreateRequest): ShortenedUrlResponse {
        val originUrl = request.originUrl
        if (shortenedUrlRepository.existsByOriginUrl(originUrl)) {
            return ShortenedUrlResponse(shortenedUrlRepository.findByOriginUrl(originUrl)!!.id!!)
        }
        return ShortenedUrlResponse(shortenedUrlRepository.save(ShortenedUrl(originUrl)).id!!)
    }
}
