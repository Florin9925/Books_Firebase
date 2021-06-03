package com.example.booksfirebase.util

import java.util.*

object HashUtil {
    private const val filtered = " :;,.!?<>()[]\\/{}`'\"~@#$%^&*-+="

    fun makeKey(string: String): String {
        return string.lowercase(Locale.ROOT).filterNot { filtered.indexOf(it) > -1 }
    }
}