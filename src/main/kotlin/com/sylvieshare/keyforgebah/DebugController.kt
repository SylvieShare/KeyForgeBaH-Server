package com.sylvieshare.keyforgebah

import com.sylvieshare.keyforgebah.base.ServerException
import com.sylvieshare.keyforgebah.base.services.EnvService
import com.sylvieshare.keyforgebah.user.NotDebugModeException
import com.sylvieshare.keyforgebah.user.UserNotFoundByIdException
import com.sylvieshare.keyforgebah.user.dao.UserDao
import com.sylvieshare.keyforgebah.user.dto.Roles.SUPER_ADMIN
import com.sylvieshare.keyforgebah.user.services.PassCryptService
import com.sylvieshare.keyforgebah.user.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie

@RestController
@RequestMapping("/debug")
class DebugController @Autowired constructor(
    val userService: UserService,
    val userDao: UserDao,
    val passCryptService: PassCryptService,
    val envService: EnvService,
    jdbcTemplate: JdbcTemplate
) {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    @PostMapping("/changePassword")
    fun getUserById(
        @CookieValue(UserService.COOKIE_SESSION) cookieSession: Cookie?,
        @RequestBody map: Map<String, String>
    ) {
        userService.session(cookieSession, checkRole = SUPER_ADMIN)
        if (!envService.hasFlag("debug")) throw NotDebugModeException()
        val id = map["id"]?.toLong() ?: throw ServerException("Not found field id")
        val password = map["password"] ?: throw ServerException("Not found field password")
        userDao.get(id) ?: throw UserNotFoundByIdException(id)
        val passwordCrypt = passCryptService.hash(password)
        namedJdbcTemplate.update(
            "UPDATE kfserv.users SET password=:password WHERE id=:id",
            mapOf(
                "id" to id,
                "password" to passwordCrypt
            )
        )
    }
}
