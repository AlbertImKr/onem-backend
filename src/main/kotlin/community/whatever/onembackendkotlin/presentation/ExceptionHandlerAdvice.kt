package community.whatever.onembackendkotlin.presentation

import community.whatever.onembackendkotlin.application.exception.DomainAlreadyBlockedException
import community.whatever.onembackendkotlin.application.exception.UrlNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlerAdvice {

    private val log = LoggerFactory.getLogger(ExceptionHandlerAdvice::class.java)

    @ExceptionHandler(UrlNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleIllegalArgumentException(e: UrlNotFoundException): String {
        log.error(e.message, e)
        return requireNotNull(e.message)
    }

    @ExceptionHandler(DomainAlreadyBlockedException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleDomainAlreadyBlockedException(e: DomainAlreadyBlockedException): String {
        log.error(e.message, e)
        return requireNotNull(e.message)
    }
}
