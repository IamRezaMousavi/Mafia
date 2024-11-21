package com.github.iamrezamousavi.mafia.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.data.model.toLanguage

const val PREF_APP_LANGUAGE = "AppLanguage"

val Context.preferences: SharedPreferences
    get() = getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)

fun SharedPreferences.saveLanguage(language: Language) = edit {
    putString(PREF_APP_LANGUAGE, language.code)
}

fun SharedPreferences.getLanguage() = getString(PREF_APP_LANGUAGE, Language.FA.code).toLanguage()!!
