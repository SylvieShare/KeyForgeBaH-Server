package com.sylvieshare.keyforgebah.admin.rest

import com.sylvieshare.keyforgebah.base.Response
import com.sylvieshare.keyforgebah.base.dao.EnvDao
import com.sylvieshare.keyforgebah.user.dto.Roles.BASIC_ADMIN
import com.sylvieshare.keyforgebah.user.services.SessionUserService
import com.sylvieshare.keyforgebah.user.services.SessionUserService.Companion.COOKIE_SESSION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie

@RestController
@RequestMapping("/env")
class EnvController @Autowired constructor(
    val sessionUserService: SessionUserService,
    val envDao: EnvDao
) {

    @GetMapping("/all")
    fun getAllEnv(
        @CookieValue(COOKIE_SESSION) cookieSession: Cookie?
    ): Response<Map<String, String>> {
        sessionUserService.session(cookieSession, checkRole = BASIC_ADMIN)

        val mapValues = envDao.getAllValues()
        return Response(mapValues)
    }
}