package com.github.iamrezamousavi.mafia.data.model

import java.util.Locale

enum class Language(
    val code: String,
    val nativeName: String
) {
    FA("fa", "فارسی"),
    EN("en", "English");

    fun asSystemLocale() = Locale(code)
}

fun String?.toLanguage() = Language.entries.find { it.code == this }

fun String?.toNativeLanguage() = Language.entries.find { it.nativeName == this }
