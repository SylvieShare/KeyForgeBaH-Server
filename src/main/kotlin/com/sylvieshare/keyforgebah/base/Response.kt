package com.sylvieshare.keyforgebah.base

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class Response<T>(body: T) : ResponseEntity<T>(body, HttpStatus.OK)