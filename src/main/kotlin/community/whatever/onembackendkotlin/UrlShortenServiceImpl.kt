package community.whatever.onembackendkotlin

import org.springframework.stereotype.Service

@Service
class UrlShortenServiceImpl(private val shortenedUrlRepository: ShortenedUrlRepository) : UrlShortenService {
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
