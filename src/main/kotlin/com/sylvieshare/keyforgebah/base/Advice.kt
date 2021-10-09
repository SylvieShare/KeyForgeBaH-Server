package com.sylvieshare.keyforgebah.base

import com.sylvieshare.keyforgebah.base.dto.ResponseErrorDto
import com.sylvieshare.keyforgebah.deck.dao.LogDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestResponseEntityExceptionHandler @Autowired constructor(
    val logDao: LogDao
) : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ServerException::class])
    protected fun handleConflict(
        exServ: ServerException, request: WebRequest?
    ): ResponseEntity<Any> {
        return ResponseEntity(
            ResponseErrorDto(exServ),
            BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleConflictInternal(
        ex: Exception, request: ServletWebRequest?
    ): ResponseEntity<Any> {
        logDao.addLog(
            path = request?.request?.servletPath,
            type = ex.javaClass.simpleName,
            description = ex.message,
            trace = ex.stackTraceToString()
        )
        return ResponseEntity(
            ResponseErrorDto(ex),
            INTERNAL_SERVER_ERROR
        )
    }
}