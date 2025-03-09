package community.whatever.onembackendkotlin.application.dto

data class ShortenUrlCreateRequest(val originUrl: String)

data class ShortenUrlSearchRequest(val shortenUrl: String)
