package com.sylvieshare.keyforgebah.user.services

import com.sylvieshare.keyforgebah.user.*
import com.sylvieshare.keyforgebah.user.dao.UserDao
import com.sylvieshare.keyforgebah.user.dto.Role
import com.sylvieshare.keyforgebah.user.dto.User
import com.sylvieshare.keyforgebah.user.dto.UserBase
import com.sylvieshare.keyforgebah.user.dto.UserId
import com.sylvieshare.keyforgebah.user.services.UserService.Companion.COOKIE_SESSION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.Cookie

interface UserService {

    @Throws(UserNotFoundByIdException::class)
    fun getUserOpenInfo(userId: Long): UserBase

    @Throws(UserNotFoundByNameException::class, AuthByPassFailException::class)
    fun auth(name: String, password: String): User

    @Throws(RegistrationUserNameExistException::class)
    fun registration(name: String, password: String): User

    fun generateNewSessionCookie(userId: UserId): Cookie

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
class UserServiceImpl @Autowired constructor(
    val passCryptService: PassCryptService,
    val userDao: UserDao
) : UserService {

    private fun getUserById(userId: Long) = userDao.get(userId) ?: throw UserNotFoundByIdException(userId)

    private fun getUserByName(userName: String) = userDao.get(userName) ?: throw UserNotFoundByNameException(userName)

    override fun getUserOpenInfo(userId: Long): UserBase = UserBase(getUserById(userId))

    override fun auth(name: String, password: String): User {
        val user = getUserByName(name)
        if (!passCryptService.authenticate(password, user.password)) throw AuthByPassFailException()
        return user
    }

    override fun registration(name: String, password: String): User {
        userDao.get(name)?.let { throw RegistrationUserNameExistException(name) }
        userDao.addUser(name, password)
        return userDao.get(name)!!
    }

    override fun generateNewSessionCookie(userId: UserId): Cookie {
        val uuid = UUID.randomUUID()
        userDao.setSession(userId.id, uuid)
        val cookie = Cookie(COOKIE_SESSION, "${userId.id}:$uuid")
        cookie.path = "/"
        return cookie
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
        val user = getUserById(userId)
        if (user.session != uuid) throw AuthBySessionFailException()
        checkRole?.let { if (user.role < it.role) throw RoleException() }
        return user
    }
}