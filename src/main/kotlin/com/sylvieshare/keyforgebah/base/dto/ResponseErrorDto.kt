package com.sylvieshare.keyforgebah.base.dto

import com.sylvieshare.keyforgebah.base.ServerException

open class ResponseErrorDto(
    val exception: String,
    val desc: String,
    val args: Collection<Any>
) {

    constructor(
        ex: Exception,
        args: Collection<Any> = listOf()
    ) : this(
        ex.javaClass.simpleName,
        ex.message ?: "",
        args
    )

    constructor(
        ex: ServerException
    ) : this(
        ex,
        ex.args.asList()
    )
}