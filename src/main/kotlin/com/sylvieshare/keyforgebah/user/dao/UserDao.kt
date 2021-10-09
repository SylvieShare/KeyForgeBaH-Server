package com.sylvieshare.keyforgebah.user.dao

import com.sylvieshare.keyforgebah.user.dto.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.util.*

interface UserDao {
    fun get(id: Long): User?
    fun get(name: String): User?
    fun addUser(name: String, password: String)
    fun setSession(id: Long, session: UUID)
}

@Component
class UserDaoImpl @Autowired constructor(
    jdbcTemplate: JdbcTemplate
) : UserDao {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    val rowMapperUserFull: (ResultSet, Int) -> User = { resultSet, _ ->
        User(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("password"),
            resultSet.getString("session")?.let { UUID.fromString(it) },
            resultSet.getInt("role")
        )
    }

    override fun get(id: Long): User? {
        val usersList = namedJdbcTemplate.query(
            "SELECT * FROM kfserv.users WHERE id=:id",
            mapOf("id" to id),
            rowMapperUserFull
        )
        return if (usersList.isNotEmpty()) usersList[0] else null
    }

    override fun get(name: String): User? {
        val usersList = namedJdbcTemplate.query(
            "SELECT * FROM kfserv.users WHERE name=:name",
            mapOf("name" to name),
            rowMapperUserFull
        )
        return if (usersList.isNotEmpty()) usersList[0] else null
    }

    override fun addUser(name: String, password: String) {
        namedJdbcTemplate.update(
            "INSERT INTO kfserv.users (name, password) VALUES (:name, :pass)",
            mapOf(
                "path" to name,
                "pass" to password
            )
        )
    }

    override fun setSession(id: Long, session: UUID) {
        namedJdbcTemplate.update(
            "UPDATE kfserv.users SET session=:session WHERE id=:id",
            mapOf("id" to id, "session" to session)
        )
    }
}