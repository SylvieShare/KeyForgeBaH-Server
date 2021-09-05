package com.sylvieshare.keyforgebah.dto

data class ResponseDto<T>(val status: Boolean, val res: T?, val error: ErrorDto?) {
}