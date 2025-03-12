package community.whatever.onembackendkotlin.application

import community.whatever.onembackendkotlin.application.dto.OriginUrlResponse
import community.whatever.onembackendkotlin.application.dto.ShortenUrlCreateRequest
import community.whatever.onembackendkotlin.application.dto.ShortenUrlSearchRequest
import community.whatever.onembackendkotlin.application.dto.ShortenedUrlResponse

interface UrlShortenService {

    fun getOriginUrl(request: ShortenUrlSearchRequest): OriginUrlResponse
    fun saveShortenUrl(request: ShortenUrlCreateRequest): ShortenedUrlResponse
}
