package com.github.iamrezamousavi.mafia.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.iamrezamousavi.mafia.data.local.SettingsStorage
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.data.repository.SettingsRepository
import com.github.iamrezamousavi.mafia.utils.LangData
import kotlinx.coroutines.launch

class SettingsViewModel(
    context: Context
) : ViewModel() {
    private val settingsRepository = SettingsRepository(SettingsStorage(context))

    var language: Language
        get() = LangData.getLanguage()
        set(value) {
            LangData.setLanguage(value)
            saveVariables()
        }

    init {
        Log.d("TAG", "LANG: SettingViewModel initialised")
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

    companion object {

        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    return SettingsViewModel(application.baseContext) as T
                }
                return super.create(modelClass)
            }
        }
    }
}
