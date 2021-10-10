package com.sylvieshare.keyforgebah.base.dto

import org.springframework.http.HttpStatus

data class ResponseDto<T>(
    val data: T?,
    val status: Int?,
    val exception: String?,
    val description: String?,
    val args: Collection<Any>?
) {
    constructor(
        data: T?,
        httpStatus: HttpStatus?,
        exception: Exception?,
        args: Collection<Any>?
    ) : this(
        data,
        httpStatus?.value(),
        exception?.javaClass?.simpleName,
        exception?.message,
        args
    )
}