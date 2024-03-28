package com.github.iamrezamousavi.mafia.data.local

import android.content.Context
import com.github.iamrezamousavi.mafia.data.model.Language

class SettingsStorage(
    applicationContext: Context
) {
    private val sharedPreferences =
        applicationContext.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
    private val languageKey = "Language"

    fun saveLanguage(language: Language) {
        sharedPreferences
            .edit()
            .putString(languageKey, language.code)
            .apply()
    }

    fun getLanguage(): Language {
        val languageCode = sharedPreferences.getString(languageKey, "en")!!
        return when (languageCode) {
            Language.EN.code -> Language.EN
            Language.FA.code -> Language.FA

            else -> Language.FA
        }
    }
}
