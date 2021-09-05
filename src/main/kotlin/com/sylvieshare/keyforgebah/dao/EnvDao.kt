package com.sylvieshare.keyforgebah.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

interface EnvDao {
    fun getValue(key: String): String?
}

@Component
class EnvDaoImpl @Autowired constructor(
    jdbcTemplate: JdbcTemplate
) : EnvDao {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun getValue(key: String): String? {
        val list = namedJdbcTemplate.query(
            "SELECT value FROM env WHERE key=:key",
            mapOf("key" to key)
        ) { rs, _ -> rs.getString("value") }
        return if (list.isNotEmpty()) list[0] else null
    }
}