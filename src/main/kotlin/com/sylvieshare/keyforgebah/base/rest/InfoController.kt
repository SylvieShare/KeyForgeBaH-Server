package com.sylvieshare.keyforgebah.base.rest

import com.sylvieshare.keyforgebah.base.Response
import com.sylvieshare.keyforgebah.base.RestResponseEntityExceptionHandler.Companion.SAFE_STATUS_MODE
import com.sylvieshare.keyforgebah.user.services.SessionUserService.Companion.COOKIE_SESSION
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/tools")
class InfoController {

    @GetMapping("/ping")
    fun ping(
        @CookieValue(COOKIE_SESSION) cookieSession: Cookie?
    ): Response<Any> {
        return Response()
    }

    @GetMapping("/ssm")
    fun getSSMCookie(
        @CookieValue(COOKIE_SESSION) cookieSession: Cookie?,
        response: HttpServletResponse
    ): Response<Any> {
        response.addCookie(Cookie(SAFE_STATUS_MODE, "true"))
        return Response()
    }
}