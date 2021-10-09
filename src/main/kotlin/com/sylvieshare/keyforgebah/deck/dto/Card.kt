package com.sylvieshare.keyforgebah.deck.dto

import java.util.*

data class Card(
    val id: String,
    val uuid: UUID?,
    val artUrl: String?,
    val originalUrl: String?,
    val stats: Map<String,String>,
    val name: Map<String,String>,
    val text: Map<String,String>
)