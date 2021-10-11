package com.sylvieshare.keyforgebah.admin.rest

import com.sylvieshare.keyforgebah.base.Response
import com.sylvieshare.keyforgebah.base.dto.LogDto
import com.sylvieshare.keyforgebah.deck.dao.LogDao
import com.sylvieshare.keyforgebah.user.dto.Roles.SUPER_ADMIN
import com.sylvieshare.keyforgebah.user.services.SessionUserService
import com.sylvieshare.keyforgebah.user.services.SessionUserService.Companion.COOKIE_SESSION
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie

@RestController
@RequestMapping("/logs")
class LogController @Autowired constructor(
    val sessionUserService: SessionUserService,
    val logDao: LogDao
) {

    @GetMapping
    fun getLogs(
        @CookieValue(COOKIE_SESSION) cookieSession: Cookie?,
        @RequestParam startId: Long?
    ): Response<Collection<LogDto>> {
        sessionUserService.session(cookieSession, checkRole = SUPER_ADMIN)

        val logList = logDao.getLogs(startId ?: 0)
        return Response(logList)
    }
}