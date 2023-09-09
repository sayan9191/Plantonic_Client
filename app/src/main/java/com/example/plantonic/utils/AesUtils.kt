package com.example.plantonic.utils

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and


object AesUtils {
    private val keyValue = byteArrayOf(
        'p'.code.toByte(),
        'l'.code.toByte(),
        'a'.code.toByte(),
        'n'.code.toByte(),
        't'.code.toByte(),
        'o'.code.toByte(),
        'n'.code.toByte(),
        'i'.code.toByte(),
        'c'.code.toByte(),
        'a'.code.toByte(),
        'n'.code.toByte(),
        'd'.code.toByte(),
        'b'.code.toByte(),
        'r'.code.toByte(),
        'o'.code.toByte(),
        'z'.code.toByte()
    )

    @Throws(Exception::class)
    fun encrypt(cleartext: String): String {
        val rawKey = rawKey
        val result = encrypt(rawKey, cleartext.toByteArray())
        return toHex(result)
    }

    @Throws(Exception::class)
    fun decrypt(encrypted: String): String {
        val enc = toByte(encrypted)
        val result = decrypt(enc)
        return String(result)
    }

    @get:Throws(Exception::class)
    private val rawKey: ByteArray
        private get() {
            val key: SecretKey =
                SecretKeySpec(keyValue, "AES")
            return key.encoded
        }

    @Throws(Exception::class)
    private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
        val skeySpec: SecretKey = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        return cipher.doFinal(clear)
    }

    @Throws(Exception::class)
    private fun decrypt(encrypted: ByteArray): ByteArray {
        val skeySpec: SecretKey =
            SecretKeySpec(keyValue, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        return cipher.doFinal(encrypted)
    }

    fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] = Integer.valueOf(
            hexString.substring(2 * i, 2 * i + 2),
            16
        ).toByte()
        return result
    }

    fun toHex(buf: ByteArray?): String {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private const val HEX = "0123456789ABCDEF"

    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(HEX[b shr 4 and 0x0f]).append(HEX[(b and 0x0f).toInt()])
    }
}

infix fun Byte.shr(that: Int): Int = this.toInt().shr(that)

