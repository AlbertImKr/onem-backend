package community.whatever.onembackendkotlin

data class ShortenUrlCreateRequest(val originUrl: String)

data class ShortenUrlSearchRequest(val key: String)
