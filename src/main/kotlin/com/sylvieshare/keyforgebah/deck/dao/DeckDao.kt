package com.sylvieshare.keyforgebah.deck.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

interface DeckDao {
    fun addDeck(id: UUID, name: String, houses: String, cards: String)
}

@Component
class DeckImpl @Autowired constructor(
    jdbcTemplate: JdbcTemplate
) : DeckDao {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun addDeck(id: UUID, name: String, houses: String, cards: String) {
        namedJdbcTemplate.update(
            "INSERT INTO kfserv.decks (uuid, name, houses, cards) " +
                    "VALUES (:id, :name, :houses, :cards)",
            mapOf(
                "id" to id,
                "name" to name,
                "houses" to houses,
                "cards" to cards
            )
        )
    }
}