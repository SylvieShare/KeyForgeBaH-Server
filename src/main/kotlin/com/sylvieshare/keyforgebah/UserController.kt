package com.sylvieshare.keyforgebah

import com.sylvieshare.keyforgebah.dto.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(
    val userService: UserService
) {

    @GetMapping("/get/{id}")
    fun hello(@PathVariable("id") id: Long): ResponseEntity<User>{
        val user = userService.get(id) ?: return ResponseEntity(NOT_FOUND)
        return ResponseEntity(user, OK)
    }
}
