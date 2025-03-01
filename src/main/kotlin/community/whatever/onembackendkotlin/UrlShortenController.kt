package community.whatever.onembackendkotlin

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UrlShortenController(private val urlShortenService: UrlShortenService) {

    @PostMapping("/shorten-url/search")
    fun shortenUrlSearch(@RequestBody key: String): String {
        return urlShortenService.getOriginUrl(key)
    }

    @PostMapping("/shorten-url/create")
    fun shortenUrlCreate(@RequestBody originUrl: String): String {
        return urlShortenService.saveShortenUrl(originUrl)
    }
}
