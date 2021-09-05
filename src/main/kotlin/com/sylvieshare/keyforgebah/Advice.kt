package com.sylvieshare.keyforgebah

import com.sylvieshare.keyforgebah.dao.LogDao
import com.sylvieshare.keyforgebah.dto.ErrorDto
import com.sylvieshare.keyforgebah.net.ServerException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
        ex: ServerException, request: WebRequest?
    ): ResponseEntity<Any> {
        return ResponseEntity(ErrorDto(ex.javaClass.simpleName, ex.message ?: ""), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleConflictIllegal(
        ex: Exception, request: ServletWebRequest?
    ): ResponseEntity<Any> {
        logDao.addLog(
            request?.request?.servletPath,
            ex.javaClass.simpleName,
            ex.message
        )
        return ResponseEntity(ErrorDto(ex.javaClass.simpleName, ex.message), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}