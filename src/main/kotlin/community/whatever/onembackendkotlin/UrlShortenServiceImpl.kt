package community.whatever.onembackendkotlin

import org.springframework.stereotype.Service

@Service
class UrlShortenServiceImpl(private val shortenedUrlRepository: ShortenedUrlRepository) : UrlShortenService {
    override fun getOriginUrl(id: String): String {
        return shortenedUrlRepository.findById(id)?.originUrl ?: throw UrlNotFoundException()
    }

    override fun saveShortenUrl(originUrl: String): String {
        if (shortenedUrlRepository.existsByOriginUrl(originUrl)) {
            return shortenedUrlRepository.findByOriginUrl(originUrl)!!.id!!
        }
        return shortenedUrlRepository.save(ShortenedUrl(originUrl)).id!!
    }


}
