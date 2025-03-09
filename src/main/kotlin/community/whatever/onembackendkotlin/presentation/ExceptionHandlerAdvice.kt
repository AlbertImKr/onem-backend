package community.whatever.onembackendkotlin.presentation

import community.whatever.onembackendkotlin.application.exception.UrlNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlerAdvice {

    companion object {
        const val DEFAULT_404_MESSAGE = "URL을 찾을 수 없습니다."
    }

    @ExceptionHandler(UrlNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleIllegalArgumentException(e: UrlNotFoundException): String {
        return e.message ?: DEFAULT_404_MESSAGE
    }
}
