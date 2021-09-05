package com.sylvieshare.keyforgebah.net

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class Response<T>(body: T) : ResponseEntity<T>(body, HttpStatus.OK) {
}