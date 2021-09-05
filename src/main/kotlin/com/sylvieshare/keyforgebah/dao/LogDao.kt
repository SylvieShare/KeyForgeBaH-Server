package com.sylvieshare.keyforgebah.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

interface LogDao {
    fun addLog(path: String?, type: String?, description: String?)
}

@Component
class LogImpl @Autowired constructor(
    jdbcTemplate: JdbcTemplate
) : LogDao {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun addLog(path: String?, type: String?, description: String?) {
        namedJdbcTemplate.update(
            "INSERT INTO logs (path, type, description) VALUES (:path, :type, :description)",
            mapOf(
                "path" to (path ?: "None"),
                "type" to (type ?: ""),
                "description" to (description ?: ""))
        )
    }
}