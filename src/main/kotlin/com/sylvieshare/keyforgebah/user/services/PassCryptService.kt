package com.sylvieshare.keyforgebah.user.services

import com.sylvieshare.keyforgebah.base.ServerInternalException
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import java.util.regex.Pattern
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.experimental.xor

interface PassCryptService {
    fun hash(password: String): String
    fun authenticate(password: String, token: String): Boolean
}

@Component
class PassCryptServiceImpl : PassCryptService {
    private val random: SecureRandom = SecureRandom()
    private val cost: Int = DEFAULT_COST

    fun hash(password: CharArray): String {
        val salt = ByteArray(SIZE / 8)
        random.nextBytes(salt)
        val dk = pbkdf2(password, salt, 1 shl cost)
        val hash = ByteArray(salt.size + dk.size)
        System.arraycopy(salt, 0, hash, 0, salt.size)
        System.arraycopy(dk, 0, hash, salt.size, dk.size)
        val enc = Base64.getUrlEncoder().withoutPadding()
        return ID + cost + '$' + enc.encodeToString(hash)
    }

    fun authenticate(password: CharArray, token: String): Boolean {
        val m = layout.matcher(token)
        require(m.matches()) { "Invalid token format" }
        val iterations = iterations(m.group(1).toInt())
        val hash = Base64.getUrlDecoder().decode(m.group(2))
        val salt = Arrays.copyOfRange(hash, 0, SIZE / 8)
        val check = pbkdf2(password, salt, iterations)
        var zero = 0
        for (idx in check.indices) zero = zero or (hash[salt.size + idx] xor check[idx]).toInt()
        return zero == 0
    }

    override fun hash(password: String): String {
        return hash(password.toCharArray())
    }

    override fun authenticate(password: String, token: String): Boolean {
        return authenticate(password.toCharArray(), token)
    }

    companion object {
        const val ID = "$31$"
        const val DEFAULT_COST = 16
        private const val ALGORITHM = "PBKDF2WithHmacSHA1"
        private const val SIZE = 128
        private val layout = Pattern.compile("\\$31\\$(\\d\\d?)\\$(.{43})")
        private fun iterations(cost: Int): Int {
            require(!(cost < 0 || cost > 30)) { "cost: $cost" }
            return 1 shl cost
        }

        private fun pbkdf2(password: CharArray, salt: ByteArray, iterations: Int): ByteArray {
            val spec: KeySpec = PBEKeySpec(password, salt, iterations, SIZE)
            return try {
                val f = SecretKeyFactory.getInstance(ALGORITHM)
                f.generateSecret(spec).encoded
            } catch (ex: NoSuchAlgorithmException) {
                throw ServerInternalException("Missing algorithm: $ALGORITHM")
            } catch (ex: InvalidKeySpecException) {
                throw ServerInternalException("Invalid SecretKeyFactory")
            }
        }
    }
}