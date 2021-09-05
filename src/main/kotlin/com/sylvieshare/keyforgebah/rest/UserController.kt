package com.sylvieshare.keyforgebah.rest

import com.sylvieshare.keyforgebah.dto.UserBase
import com.sylvieshare.keyforgebah.dto.request.RequestAuth
import com.sylvieshare.keyforgebah.net.Response
import com.sylvieshare.keyforgebah.net.UserNotFoundByIdException
import com.sylvieshare.keyforgebah.services.UserService
import com.sylvieshare.keyforgebah.services.UserService.Companion.COOKIE_NAME_SESSION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(
    val userService: UserService,
) {

    @GetMapping("/id/{id}")
    fun getUserById(
        @PathVariable("id") id: Long
    ): Response<UserBase> {
        val userBase = userService.getUserOpenInfo(id)
        return Response(userBase)
    }

    @PostMapping("/auth")
    fun auth(
        @RequestBody requestAuth: RequestAuth,
        response: HttpServletResponse
    ): Response<UserBase> {
        val userId = userService.auth(requestAuth.name, requestAuth.password)
        val cookieSession = userService.generateNewSessionCookie(userId)
        response.addCookie(cookieSession)
        val userInfo = userService.getUserOpenInfo(userId)
        return Response(userInfo)
    }

    @GetMapping("/checkAuth")
    fun checkAuth(
        response: HttpServletResponse,
        @CookieValue(COOKIE_NAME_SESSION) cookieSessionValue: String?,
    ): Response<UserBase> {
        val userId = userService.authBySession(cookieSessionValue)
        return Response(userService.getUserOpenInfo(userId))
    }
}
