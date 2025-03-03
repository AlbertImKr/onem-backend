package community.whatever.onembackendkotlin

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private const val DEFAULT_404_MESSAGE = "Invalid key"

@RestControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(UrlNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleIllegalArgumentException(e: UrlNotFoundException): String {
        return e.message ?: DEFAULT_404_MESSAGE
    }
}
