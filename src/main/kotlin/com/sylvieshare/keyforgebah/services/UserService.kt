package com.sylvieshare.keyforgebah

import com.sylvieshare.keyforgebah.dto.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

interface UserService {
    fun get(id: Long): User?
}

@Component
class UserServiceImpl @Autowired constructor(
    val jdbcTemplate: JdbcTemplate
) : UserService {

    val ROW_MAPPER: (ResultSet, Int) -> User = { resultSet, _ ->
        User(resultSet.getLong("id"), resultSet.getString("name"))
    }

    override fun get(id: Long): User? {
        val usersList = jdbcTemplate.query("SELECT * FROM USERS WHERE id=${id}", ROW_MAPPER)
        return if(usersList.isNotEmpty()) usersList[0] else null
    }
}