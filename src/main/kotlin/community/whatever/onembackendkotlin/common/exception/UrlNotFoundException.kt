package community.whatever.onembackendkotlin.common.exception

class UrlNotFoundException : RuntimeException(URL_NOT_FOUND_MESSAGE) {
    companion object {
        private const val URL_NOT_FOUND_MESSAGE = "해당 키에 대한 URL이 존재하지 않습니다."
    }
}
