package com.sylvieshare.keyforgebah.user.rest

import com.sylvieshare.keyforgebah.user.dto.UserBase
import com.sylvieshare.keyforgebah.user.dto.request.RequestAuth
import com.sylvieshare.keyforgebah.base.Response
import com.sylvieshare.keyforgebah.user.dto.User
import com.sylvieshare.keyforgebah.user.dto.request.RequestRegistration
import com.sylvieshare.keyforgebah.user.services.UserService
import com.sylvieshare.keyforgebah.user.services.UserService.Companion.COOKIE_SESSION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(
    val userService: UserService,
) {

    @GetMapping("/id/{id}")
    fun getUserById(
        @PathVariable id: Long
    ): Response<UserBase> {
        val userBase = userService.getUserOpenInfo(id)
        return Response(userBase)
    }

    @PostMapping("/auth")
    fun auth(
        @RequestBody requestAuth: RequestAuth,
        response: HttpServletResponse
    ): Response<User> {
        val user = userService.auth(requestAuth.name, requestAuth.password)
        val cookieSession = userService.generateNewSessionCookie(user)
        response.addCookie(cookieSession)
        return Response(user)
    }

    @PostMapping("/registration")
    fun registration(
        @RequestBody requestRegistration: RequestRegistration,
        response: HttpServletResponse
    ): Response<User> {
        val user = userService.registration(requestRegistration.name, requestRegistration.password)
        val cookieSession = userService.generateNewSessionCookie(user)
        response.addCookie(cookieSession)
        return Response(user)
    }

    @GetMapping("/checkAuth")
    fun checkAuth(
        @CookieValue(COOKIE_SESSION) cookieSession: Cookie?
    ): Response<UserBase> {
        val user = userService.session(cookieSession)
        return Response(UserBase(user))
    }
}
