package com.sylvieshare.keyforgebah.user.services

import com.sylvieshare.keyforgebah.base.interrupt
import com.sylvieshare.keyforgebah.user.*
import com.sylvieshare.keyforgebah.user.dao.UserDao
import com.sylvieshare.keyforgebah.user.dto.Role
import com.sylvieshare.keyforgebah.user.dto.User
import com.sylvieshare.keyforgebah.user.dto.UserId
import com.sylvieshare.keyforgebah.user.services.SessionUserService.Companion.COOKIE_SESSION
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

interface SessionUserService {

    fun generateSession(userId: UserId, response: HttpServletResponse)

    fun clearSession(response: HttpServletResponse)

    @Throws(
        AuthBySessionNoneCookieFailException::class,
        BadCookieException::class,
        UserNotFoundByIdException::class,
        AuthBySessionFailException::class
    )
    fun session(sessionCookie: Cookie?, checkRole: Role? = null): User

    companion object {
        const val COOKIE_SESSION = "kfserv-session"
    }
}

@Component
class SessionUserServiceImpl(
    val userDao: UserDao
) : SessionUserService {

    override fun generateSession(userId: UserId, response: HttpServletResponse) {
        val uuid = UUID.randomUUID()
        userDao.setSession(userId.id, uuid)
        val cookie = Cookie(COOKIE_SESSION, "${userId.id}:$uuid")
        cookie.path = "/"
        response.addCookie(cookie)
    }

    override fun clearSession(response: HttpServletResponse) {
        val cookie = Cookie(COOKIE_SESSION, "")
        cookie.path = "/"
        response.addCookie(cookie)
    }

    override fun session(sessionCookie: Cookie?, checkRole: Role?): User {
        sessionCookie ?: throw AuthBySessionNoneCookieFailException()
        val userId: Long
        val uuid: UUID
        try {
            val split = sessionCookie.value.split(":")
            userId = split[0].toLong()
            uuid = UUID.fromString(split[1])
        } catch (ex: Exception) {
            throw BadCookieException()
        }
        val user = userDao.get(userId) ?: throw UserNotFoundByIdException(userId)
        if (user.session != uuid) throw AuthBySessionFailException()
        checkRole?.let { if (user.role < it.role) throw RoleException() }
        return user
    }
}