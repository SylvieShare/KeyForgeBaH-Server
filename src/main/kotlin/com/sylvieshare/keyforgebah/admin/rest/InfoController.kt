package com.sylvieshare.keyforgebah.admin.rest

import com.sylvieshare.keyforgebah.base.Response
import com.sylvieshare.keyforgebah.user.services.UserService.Companion.COOKIE_SESSION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie

@RestController
@RequestMapping("/ping")
class InfoController @Autowired constructor(
) {

    @GetMapping
    fun ping(
        @CookieValue(COOKIE_SESSION) cookieSession: Cookie?,
    ): Response<Any> {
        return Response()
    }
}