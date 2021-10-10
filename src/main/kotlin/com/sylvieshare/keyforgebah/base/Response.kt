package com.sylvieshare.keyforgebah.base

import com.sylvieshare.keyforgebah.base.dto.ResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

class Response<T>(
    body: T? = null,
    status: HttpStatus = OK,
    statusData: HttpStatus? = null,
    exception: Exception? = null,
    args: Collection<Any>? = null,
) : ResponseEntity<ResponseDto<T>>(
    ResponseDto(body, statusData, exception, args),
    status
)