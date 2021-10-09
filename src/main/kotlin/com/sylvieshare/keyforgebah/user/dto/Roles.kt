package com.sylvieshare.keyforgebah.user.dto

enum class Roles(
    override val role: Int
) : Role {
    UNKNOWN(0),
    UN_AUTH_USER(1),
    NORMAL_USER(2),
    BASIC_ADMIN(11),
    SUPER_ADMIN(19),
}