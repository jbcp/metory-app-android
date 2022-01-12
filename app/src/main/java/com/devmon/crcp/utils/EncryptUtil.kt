package com.devmon.crcp.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object EncryptUtil {

    fun sha256(origin: String): String {
        if (origin.isEmpty()) return origin

        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(origin.toByteArray(Charsets.UTF_8))

            val builder = StringBuilder()

            for (b in digest.digest()) {
                builder.append(String.format("%02x", b))
            }
            builder.toString()
        } catch (e: NoSuchAlgorithmException) {
            origin
        }
    }
}