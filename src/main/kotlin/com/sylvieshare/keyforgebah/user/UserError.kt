package com.sylvieshare.keyforgebah.user

import com.sylvieshare.keyforgebah.base.ServerException
import com.sylvieshare.keyforgebah.base.ServerInternalException


class UserNotFoundByIdException(id: Long) : ServerException("User not found", id)
class UserNotFoundByNameException(name: String) : ServerException("User not found", name)
class PasswordFailException : ServerException("Bad password")

class AuthByPassFailException : ServerException("Authentication by password failed")
class AuthBySessionNoneCookieFailException : ServerException("Authentication cookie not found")
class BadCookieException : ServerException("Authentication cookie is bad")
class AuthBySessionFailException : ServerException("Authentication by cookie failed")

class RegistrationUserNameExistException(name: String) : ServerException("User found", name)

class RoleException : ServerException("Level role is low")

class NotDebugModeException : ServerInternalException("Not debug mode")