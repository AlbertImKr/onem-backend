package community.whatever.onembackendkotlin.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import community.whatever.onembackendkotlin.application.UrlShortenService
import community.whatever.onembackendkotlin.application.dto.OriginUrlResponse
import community.whatever.onembackendkotlin.application.dto.ShortenUrlCreateRequest
import community.whatever.onembackendkotlin.application.dto.ShortenUrlSearchRequest
import community.whatever.onembackendkotlin.application.dto.ShortenedUrlResponse
import community.whatever.onembackendkotlin.application.exception.UrlNotFoundException
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UrlShortenController::class)
class UrlShortenControllerTest(
    @Autowired private val mockMvc: MockMvc,
) {

    @MockitoBean
    private lateinit var urlShortenService: UrlShortenService

    private val objectMapper = ObjectMapper()
    private val faker = Faker()

    @Test
    fun `원본 URL 등록 시 키를 반환한다`() {
        // given
        val originUrl = faker.internet().url()
        val request = ShortenUrlCreateRequest(originUrl)
        val shortenedUrl = faker.idNumber().peselNumber()
        val response = ShortenedUrlResponse(shortenedUrl)
        given(urlShortenService.saveShortenUrl(request)).willReturn(response)

        // when
        val result = mockMvc.perform(
            post("/shorten-url/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        // then
        assertThat(result).isNotBlank()
    }

    @Test
    fun `키로 검색하면 원본 URL을 반환한다`() {
        // given
        val shortenedUrl = faker.idNumber().peselNumber()
        val request = ShortenUrlSearchRequest(shortenedUrl)
        val originUrl = faker.internet().url()
        val response = OriginUrlResponse(originUrl)
        given(urlShortenService.getOriginUrl(request)).willReturn(response)

        // when
        val result = mockMvc.perform(
            post("/shorten-url/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        // then
        assertThat(result).isEqualTo(objectMapper.writeValueAsString(response))
    }

    @Test
    fun `유요하지 않은 키로 검색하면 404 에러를 반환한다`() {
        // given
        val notExistShortenedUrl = faker.idNumber().peselNumber()
        val request = ShortenUrlSearchRequest(notExistShortenedUrl)
        given(urlShortenService.getOriginUrl(request)).willThrow(UrlNotFoundException())

        // when
        mockMvc.perform(
            post("/shorten-url/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `중복된 원본 URL을 등록하면 기존 키를 반환한다`() {
        // given
        val originUrl = "https://www.google.com"
        val request = ShortenUrlCreateRequest(originUrl)
        val shortenedUrl = faker.idNumber().peselNumber()
        val response = ShortenedUrlResponse(shortenedUrl)
        given(urlShortenService.saveShortenUrl(request)).willReturn(response)

        // when
        val result = mockMvc.perform(
            post("/shorten-url/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        // then
        assertThat(result).isEqualTo(objectMapper.writeValueAsString(response))
    }
}
