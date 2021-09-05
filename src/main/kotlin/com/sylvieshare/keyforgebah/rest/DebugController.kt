package com.sylvieshare.keyforgebah.rest

import com.sylvieshare.keyforgebah.net.NotDebugModeException
import com.sylvieshare.keyforgebah.net.UserNotFoundByIdException
import com.sylvieshare.keyforgebah.services.UserService
import com.sylvieshare.keyforgebah.dao.UserDao
import com.sylvieshare.keyforgebah.net.ServerException
import com.sylvieshare.keyforgebah.services.EnvService
import com.sylvieshare.keyforgebah.services.PassCryptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/debug")
class DebugController @Autowired constructor(
    val userDao: UserDao,
    val passCryptService: PassCryptService,
    val envService: EnvService,
    jdbcTemplate: JdbcTemplate
) {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    @PostMapping("/changePassword")
    fun getUserById(
        @RequestBody map: Map<String, String>
    ) {
        if(!envService.hasFlag("debug")) throw NotDebugModeException()
        val id = map["id"]?.toLong() ?: throw ServerException("Not found field id")
        val password = map["password"] ?: throw ServerException("Not found field password")
        userDao.get(id) ?: throw UserNotFoundByIdException(id)
        val passwordCrypt = passCryptService.hash(password)
        namedJdbcTemplate.update(
            "UPDATE USERS SET password=:password WHERE id=:id",
            mapOf("id" to id, "password" to passwordCrypt)
        )
    }
}
