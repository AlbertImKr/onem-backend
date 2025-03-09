package community.whatever.onembackendkotlin.domain

interface ShortenedUrlRepository {
    /**
     * 아이디를 통해 원본 URL을 찾는다.
     *
     * @param id ShortenedUrl의 id이다.
     * @return 존재하면 ShortenedUrl을 반환하고 존재하지 않으면 null을 반환한다.
     */
    fun findById(id: String): ShortenedUrl?

    /**
     * ShortenedUrl을 저장한다.
     *
     * @param shortenedUrl 저장할 ShortenedUrl이다.
     * @return 저장된 ShortenedUrl을 반환한다.
     */
    fun save(shortenedUrl: ShortenedUrl): ShortenedUrl

    /**
     * 원본 URL을 통해 ShortenedUrl을 찾는다.
     *
     * @param originUrl ShortenedUrl의 원본 URL이다.
     * @return 존재하면 ShortenedUrl을 반환하고 존재하지 않으면 null을 반환한다.
     */
    fun existsByOriginUrl(originUrl: String): Boolean

    /**
     * 원본 URL을 통해 ShortenedUrl을 찾는다.
     *
     * @param originUrl ShortenedUrl의 원본 URL이다.
     * @return 존재하면 ShortenedUrl을 반환하고 존재하지 않으면 null을 반환한다.
     */
    fun findByOriginUrl(originUrl: String): ShortenedUrl?

    /**
     * 모든 ShortenedUrl을 삭제한다.
     */
    fun deleteAll()
}
