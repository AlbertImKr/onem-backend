package community.whatever.onembackendkotlin.common.dto

data class ShortenUrlCreateRequest(val originUrl: String)

data class ShortenUrlSearchRequest(val shortenUrl: String)
