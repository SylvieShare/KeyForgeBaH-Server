package com.sylvieshare.keyforgebah.base

open class ServerException(desc: String, vararg val args: Any) : RuntimeException(desc)

open class ServerInternalException(desc: String) : RuntimeException(desc)