package com.github.iamrezamousavi.mafia.data.repository

import com.github.iamrezamousavi.mafia.data.local.SettingsStorage
import com.github.iamrezamousavi.mafia.data.model.Language

class SettingsRepository(
    private val settingsStorage: SettingsStorage
) {

    fun getLanguage() = settingsStorage.getLanguage()
    fun saveLanguage(language: Language) = settingsStorage.saveLanguage(language)

}
