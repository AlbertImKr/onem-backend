package community.whatever.onembackendkotlin

interface UrlShortenService {
    /**
     * 아이디를 통해 원본 URL을 찾는다.
     *
     * @param id ShortenedUrl의 id이다.
     * @return ShortenedUrl의 id
     * @throws UrlNotFoundException ShortenedUrl의 id에 해당하는 ShortenedUrl이 존재하지 않으면 발생한다.
     */
    fun getOriginUrl(id: String): String

    /**
     * URL이 존재하면 저장된 URL의 id를 반환하고 존재하지 않으면 새롭게 URL을 저장하고 id를 반환한다.
     *
     * @param originUrl 단축할 URL이다.
     * @return 저장된 URL의 id이다.
     */
    fun saveShortenUrl(originUrl: String): String
}
