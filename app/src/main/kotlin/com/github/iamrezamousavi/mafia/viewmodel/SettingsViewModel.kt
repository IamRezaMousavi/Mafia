package com.github.iamrezamousavi.mafia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iamrezamousavi.mafia.data.local.SettingsStorage
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.data.repository.SettingsRepository
import com.github.iamrezamousavi.mafia.utils.LangData
import kotlinx.coroutines.launch

class SettingsViewModel(context: Context) : ViewModel() {
    private val settingsRepository = SettingsRepository(SettingsStorage(context))

    var language: Language
        get() = LangData.getLanguage()
        set(value) {
            LangData.setLanguage(value)
            saveVariables()
        }

    init {
        loadLanguage()
    }

    private fun loadLanguage() {
        LangData.setLanguage(settingsRepository.getLanguage())
    }

    private fun saveVariables() {
        viewModelScope.launch {
            settingsRepository.saveLanguage(LangData.language.value!!)
        }
    }
}
