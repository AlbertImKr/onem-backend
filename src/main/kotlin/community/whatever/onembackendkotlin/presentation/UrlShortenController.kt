package community.whatever.onembackendkotlin.presentation

import community.whatever.onembackendkotlin.application.UrlShortenService
import community.whatever.onembackendkotlin.common.dto.OriginUrlResponse
import community.whatever.onembackendkotlin.common.dto.ShortenUrlCreateRequest
import community.whatever.onembackendkotlin.common.dto.ShortenUrlSearchRequest
import community.whatever.onembackendkotlin.common.dto.ShortenedUrlResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UrlShortenController(private val urlShortenService: UrlShortenService) {

    @PostMapping("/shorten-url/search")
    fun shortenUrlSearch(@RequestBody key: ShortenUrlSearchRequest): ResponseEntity<OriginUrlResponse> {
        return ResponseEntity.ok(urlShortenService.getOriginUrl(key))
    }

    @PostMapping("/shorten-url/create")
    fun shortenUrlCreate(@RequestBody originUrl: ShortenUrlCreateRequest): ResponseEntity<ShortenedUrlResponse> {
        return ResponseEntity.ok(urlShortenService.saveShortenUrl(originUrl))
    }
}
