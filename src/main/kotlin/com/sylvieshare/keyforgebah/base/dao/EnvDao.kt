package com.sylvieshare.keyforgebah.base.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

interface EnvDao {
    fun getValue(key: String): String?
    fun getAllValues(): Map<String, String>
}

@Component
class EnvDaoImpl @Autowired constructor(
    jdbcTemplate: JdbcTemplate
) : EnvDao {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    val rowMapperEnvPair: (ResultSet, Int) -> Pair<String, String> = { resultSet, _ ->
        Pair(
            resultSet.getString("key"),
            resultSet.getString("value")
        )
    }

    override fun getValue(key: String): String? {
        val list = namedJdbcTemplate.query(
            "SELECT value FROM kfserv.env WHERE key=:key",
            mapOf("key" to key)
        ) { rs, _ -> rs.getString("value") }
        return if (list.isNotEmpty()) list[0] else null
    }

    override fun getAllValues(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        namedJdbcTemplate.query(
            "SELECT key, value FROM kfserv.env",
        ) { rs, _ -> map.put(rs.getString("key"), rs.getString("value")) }
        return map
    }
}