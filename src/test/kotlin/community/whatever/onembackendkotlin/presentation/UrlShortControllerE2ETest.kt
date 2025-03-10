package community.whatever.onembackendkotlin.presentation

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension::class)
class UrlShortControllerE2ETest {

    @LocalServerPort
    private var port: Int = 8080

    private lateinit var spec: RequestSpecification

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        RestAssured.port = port
        this.spec = RequestSpecBuilder()
            .addFilter(
                RestAssuredRestDocumentation.documentationConfiguration(provider)
            )
            .build()
    }

    @Test
    fun `원본 URL을 등록하고 키로 조회한다`() {
        // docs
        this.spec.filter(
            document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestFields(
                    fieldWithPath("originUrl").description("원본 URL")
                ),
                responseFields(
                    fieldWithPath("shortenedUrl").description("단축 URL")
                )
            )
        )

        // given
        val body = mapOf(
            "originUrl" to "https://www.google.com"
        )

        // when
        val response = RestAssured
            .given(spec)
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .`when`()
            .post("/shorten-url/create")
            .then()
            .log().all()
            .extract()

        // then
        assertThat(response.statusCode()).isEqualTo(200)
    }

    @Test
    fun `키로 검색하면 원본 URL을 반환한다`() {
        // docs
        this.spec.filter(
            document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestFields(
                    fieldWithPath("shortenUrl").description("단축 URL")
                ),
                responseFields(
                    fieldWithPath("originUrl").description("원본 URL")
                )
            )
        )

        // given
        val body = mapOf(
            "originUrl" to "https://www.google.com"
        )

        val response = RestAssured
            .given()
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .`when`()
            .post("/shorten-url/create")
            .then()
            .log().all()
            .extract()

        val key = response.path<String>("shortenedUrl")

        // when
        val response2 = RestAssured
            .given(spec)
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(mapOf("shortenUrl" to key))
            .`when`()
            .post("/shorten-url/search")
            .then()
            .log().all()
            .extract()

        // then
        assertThat(response2.statusCode()).isEqualTo(200)
        assertThat(response2.path<String>("originUrl")).isEqualTo("https://www.google.com")
    }
}
