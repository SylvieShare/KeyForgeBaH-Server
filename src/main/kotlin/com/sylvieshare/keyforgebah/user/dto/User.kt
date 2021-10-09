package com.sylvieshare.keyforgebah.user.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

data class User(
    override val id: Long,
    val name: String,
    @JsonIgnore val password: String,
    @JsonIgnore val session: UUID?,
    override val role: Int
) : UserId, Role