package com.github.iamrezamousavi.mafia.data.local

import android.content.Context
import com.github.iamrezamousavi.mafia.data.model.Language

const val SettingsSP = "settings_prefs"
const val LanguageKey = "Language"

class SettingsStorage(applicationContext: Context) {
    private val sharedPreferences =
        applicationContext.getSharedPreferences(SettingsSP, Context.MODE_PRIVATE)

    fun saveLanguage(language: Language) {
        sharedPreferences
            .edit()
            .putString(LanguageKey, language.code)
            .apply()
    }

    fun getLanguage(): Language {
        val languageCode = sharedPreferences.getString(LanguageKey, "fa")!!
        return when (languageCode) {
            Language.EN.code -> Language.EN
            Language.FA.code -> Language.FA

            else -> Language.FA
        }
    }
}
