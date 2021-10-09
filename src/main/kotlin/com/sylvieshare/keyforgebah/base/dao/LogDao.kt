package com.sylvieshare.keyforgebah.deck.dao

import com.sylvieshare.keyforgebah.base.dto.LogDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

interface LogDao {
    fun addLog(path: String?, type: String?, description: String?, trace: String?)
    fun getLogs(startId: Long?): Collection<LogDto>
}

@Component
class LogImpl @Autowired constructor(
    jdbcTemplate: JdbcTemplate
) : LogDao {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    val rowMapperLog: (ResultSet, Int) -> LogDto = { resultSet, _ ->
        LogDto(
            resultSet.getLong("id"),
            resultSet.getString("date_created"),
            resultSet.getString("path"),
            resultSet.getString("type"),
            resultSet.getString("description"),
            resultSet.getString("trace")
        )
    }

    override fun addLog(path: String?, type: String?, description: String?, trace: String?) {
        namedJdbcTemplate.update(
            "INSERT INTO kfserv.logs (path, type, description, trace) VALUES (:path, :type, :description, :trace)",
            mapOf(
                "path" to (path ?: "None"),
                "type" to (type ?: ""),
                "description" to (description ?: ""),
                "trace" to trace
            )
        )
    }

    override fun getLogs(startId: Long?): Collection<LogDto> {
        return namedJdbcTemplate.query(
            "SELECT * FROM kfserv.logs WHERE id>=:startId",
            mapOf("startId" to (startId ?: 0)),
            rowMapperLog
        )
    }
}