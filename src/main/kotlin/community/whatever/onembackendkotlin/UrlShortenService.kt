package community.whatever.onembackendkotlin

interface UrlShortenService {

    fun getOriginUrl(request: ShortenUrlSearchRequest): OriginUrlResponse
    fun saveShortenUrl(request: ShortenUrlCreateRequest): ShortenedUrlResponse
}
