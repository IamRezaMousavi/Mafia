package com.github.iamrezamousavi.mafia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iamrezamousavi.mafia.data.local.SettingsStorage
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(context: Context) : ViewModel() {
    private val settingsRepository = SettingsRepository(SettingsStorage(context))

    private var _language = settingsRepository.getLanguage()
    val language = _language

    fun setLanguage(newLanguage: Language) {
        _language = newLanguage
        viewModelScope.launch {
            settingsRepository.saveLanguage(newLanguage)
        }
    }
}
