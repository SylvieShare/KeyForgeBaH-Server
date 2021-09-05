package com.sylvieshare.keyforgebah.net


class UserNotFoundByIdException(id: Long) : ServerException("User id=$id not found")
class UserNotFoundByNameException(name: String) : ServerException("User $name not found")
class PasswordFailException : ServerException("Bad password")
class AuthByPassFailException : ServerException("Authentication by password failed")
class AuthBySessionNoneCookieFailException : ServerException("Authentication cookie not found")
class AuthBySessionFailException : ServerException("Authentication by cookie failed")

class NotDebugModeException : ServerInternalException("Not debug mode")