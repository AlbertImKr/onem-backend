package community.whatever.onembackendkotlin.application.exception

class DomainAlreadyBlockedException : RuntimeException(DOMAIN_ALREADY_BLOCKED_MESSAGE) {
    companion object {
        private const val DOMAIN_ALREADY_BLOCKED_MESSAGE = "이미 차단된 도메인입니다."
    }
}
