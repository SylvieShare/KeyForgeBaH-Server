package com.sylvieshare.keyforgebah.user.dto

class UserBase(
    override val id: Long,
    val name: String,
    override val role: Int
) : UserId, Role {
    constructor(user: User) : this(user.id, user.name, user.role)
}