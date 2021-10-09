package com.sylvieshare.keyforgebah.deck.dto

import java.util.*

data class Deck(
    val uuid : UUID,
    val name: String,
    val houses: Collection<String>,
    val cards: Collection<String>
)