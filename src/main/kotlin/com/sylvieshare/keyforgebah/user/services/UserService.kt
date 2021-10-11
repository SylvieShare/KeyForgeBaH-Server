package com.sylvieshare.keyforgebah.user.services

import com.sylvieshare.keyforgebah.user.*
import com.sylvieshare.keyforgebah.user.dao.UserDao
import com.sylvieshare.keyforgebah.user.dto.Role
import com.sylvieshare.keyforgebah.user.dto.User
import com.sylvieshare.keyforgebah.user.dto.UserBase
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
}