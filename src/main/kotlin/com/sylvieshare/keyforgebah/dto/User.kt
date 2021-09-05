package com.sylvieshare.keyforgebah.dto

import java.util.*

data class User(
    val id: Long,
    val name: String,
    val password: String,
    val session: UUID?,
    val role: Int
) {
    fun getOpenInfo() = UserBase(id, name, role)
}