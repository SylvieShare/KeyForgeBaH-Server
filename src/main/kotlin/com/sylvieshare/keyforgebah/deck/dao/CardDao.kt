package com.sylvieshare.keyforgebah.deck.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sylvieshare.keyforgebah.deck.dto.Card
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.util.*

interface CardDao {
    fun getCard(id: String): Card?
}

@Component
class CardDaoImpl @Autowired constructor(
    jdbcTemplate: JdbcTemplate
) : CardDao {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
    private val objectMapper = ObjectMapper()

    val rowMapperCard: (ResultSet, Int) -> Card = { rs, _ ->
        Card(
            id = rs.getString("id"),
            uuid = rs.getString("uuid")?.let { UUID.fromString(it) },
            artUrl = rs.getString("art_url"),
            originalUrl = rs.getString("original_url"),
            stats = objectMapper.readValue(rs.getString("stats_json")),
            name = objectMapper.readValue(rs.getString("name_json")),
            text = objectMapper.readValue(rs.getString("text_json"))
        )
    }

    override fun getCard(id: String): Card? {
        val cardList = namedJdbcTemplate.query(
            "SELECT * FROM kfserv.cards WHERE id=:id",
            mapOf("id" to id),
            rowMapperCard
        )
        return if (cardList.isNotEmpty()) cardList[0] else null
    }
}