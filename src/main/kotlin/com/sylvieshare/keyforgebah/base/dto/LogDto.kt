package com.sylvieshare.keyforgebah.base.dto

data class LogDto(
    val id: Long,
    val date: String,
    val path: String?,
    val type: String?,
    val description: String?,
    val trace: String?
)
