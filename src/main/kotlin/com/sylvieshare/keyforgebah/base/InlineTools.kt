package com.sylvieshare.keyforgebah.base

inline fun <R> interrupt(exception: Exception, action: () -> R): R =
    try {
        action()
    } catch (e: Exception) {
        throw exception
    }
