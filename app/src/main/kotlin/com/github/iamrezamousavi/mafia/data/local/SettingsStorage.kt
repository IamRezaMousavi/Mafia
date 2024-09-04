package com.github.iamrezamousavi.mafia.data.local

import android.content.Context
import com.github.iamrezamousavi.mafia.data.model.Language

const val SETTINGS_SHARED = "settings_prefs"
const val LANG_KEY = "Language"

class SettingsStorage(applicationContext: Context) {
    private val sharedPreferences =
        applicationContext.getSharedPreferences(SETTINGS_SHARED, Context.MODE_PRIVATE)

    fun saveLanguage(language: Language) {
        sharedPreferences
            .edit()
            .putString(LANG_KEY, language.code)
            .apply()
    }

    fun getLanguage(): Language {
        val languageCode = sharedPreferences.getString(LANG_KEY, "fa")!!
        return when (languageCode) {
            Language.EN.code -> Language.EN
            Language.FA.code -> Language.FA

            else -> Language.FA
        }
    }
}
