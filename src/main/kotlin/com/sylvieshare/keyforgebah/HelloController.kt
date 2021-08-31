package com.sylvieshare.keyforgebah

import com.sylvieshare.keyforgebah.dto.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class HelloController @Autowired constructor(
    val userService: UserService
) {

    @GetMapping("/get/{id}")
    fun hello(@PathVariable("id") id: Long): User?
    {
        return userService.get(id)
    }
}
