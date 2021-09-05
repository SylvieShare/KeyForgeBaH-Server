package com.sylvieshare.keyforgebah.services

import com.sylvieshare.keyforgebah.dao.UserDao
import com.sylvieshare.keyforgebah.dto.UserBase
import com.sylvieshare.keyforgebah.net.*
import com.sylvieshare.keyforgebah.services.UserService.Companion.COOKIE_NAME_SESSION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.Cookie

interface UserService {

    @Throws(UserNotFoundByIdException::class)
    fun getUserOpenInfo(userId: Long): UserBase

    @Throws(UserNotFoundByNameException::class, AuthByPassFailException::class)
    fun auth(name: String, password: String): Long

    fun generateNewSessionCookie(userId: Long): Cookie

    @Throws(
        AuthBySessionNoneCookieFailException::class,
        UserNotFoundByIdException::class,
        AuthBySessionFailException::class
    )
    fun authBySession(sessionValue: String?): Long


    companion object {
        const val COOKIE_NAME_SESSION = "kfserv-session"
    }
}

@Component
class UserServiceImpl @Autowired constructor(
    val passCryptService: PassCryptService,
    val userDao: UserDao
) : UserService {

    private fun getUserById(userId: Long) = userDao.get(userId) ?: throw UserNotFoundByIdException(userId)

    private fun getUserByName(userName: String) = userDao.get(userName) ?: throw UserNotFoundByNameException(userName)

    override fun getUserOpenInfo(userId: Long): UserBase = getUserById(userId).getOpenInfo()

    override fun auth(name: String, password: String): Long {
        val user = getUserByName(name)
        if (!passCryptService.authenticate(password, user.password)) throw AuthByPassFailException()
        return user.id
    }

    override fun generateNewSessionCookie(userId: Long): Cookie {
        val uuid = UUID.randomUUID()
        userDao.setSession(userId, uuid)
        return Cookie(COOKIE_NAME_SESSION, "$userId:$uuid")
    }

    override fun authBySession(sessionValue: String?): Long {
        sessionValue ?: throw AuthBySessionNoneCookieFailException()
        val split = sessionValue.split(":")
        val userId = split[0].toLong()
        val uuid = UUID.fromString(split[1])
        val user = getUserById(userId)
        if (user.session != uuid) throw AuthBySessionFailException()
        return userId
    }
}