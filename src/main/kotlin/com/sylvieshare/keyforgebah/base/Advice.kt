package com.sylvieshare.keyforgebah.base

import com.sylvieshare.keyforgebah.deck.dao.LogDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest


@ControllerAdvice
class RestResponseEntityExceptionHandler @Autowired constructor(
    val logDao: LogDao
) : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ServerException::class])
    protected fun handleConflict(
        exServ: ServerException,
        request: ServletWebRequest
    ): Response<Any> {
        val safeStatusMode = getSafeStatusMode(request)
        return Response(
            exception = exServ,
            status = if (safeStatusMode) OK else BAD_REQUEST,
            statusData = BAD_REQUEST.takeIf { safeStatusMode },
            args = exServ.args.toList()
        )
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleConflictInternal(
        ex: Exception,
        request: ServletWebRequest
    ): Response<Any> {
        logDao.addLog(
            path = request.request.servletPath,
            type = ex.javaClass.simpleName,
            description = ex.message,
            trace = ex.stackTraceToString()
        )
        val safeStatusMode = getSafeStatusMode(request)
        return Response(
            exception = ex,
            status = if (safeStatusMode) OK else INTERNAL_SERVER_ERROR,
            statusData = INTERNAL_SERVER_ERROR.takeIf { safeStatusMode },
        )
    }

    companion object{
        const val HEADER_SAFE_STATUS_MODE = "Safe-Status-Mode"

        fun getSafeStatusMode(request: ServletWebRequest) =
            request.getHeader(HEADER_SAFE_STATUS_MODE) == "true"
    }
}