package com.sylvieshare.keyforgebah.services

import com.sylvieshare.keyforgebah.dao.EnvDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

interface EnvService {
    fun getValue(key: String, default: String? = ""): String?
    fun hasFlag(key: String): Boolean
}

@Component
class EnvServiceImpl @Autowired constructor(
    val envDao: EnvDao
) : EnvService {

    override fun getValue(key: String, default: String?): String? {
        return envDao.getValue(key) ?: default
    }

    override fun hasFlag(key: String): Boolean {
        return envDao.getValue(key)?.let { it != "false" } ?: false
    }
}