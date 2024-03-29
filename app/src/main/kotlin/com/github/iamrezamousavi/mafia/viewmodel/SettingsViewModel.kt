package com.github.iamrezamousavi.mafia.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.iamrezamousavi.mafia.data.local.SettingsStorage
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    context: Context
) : ViewModel() {
    private val settingsRepository = SettingsRepository(SettingsStorage(context))

    private val _language = MutableLiveData(Language.FA)
    val language: LiveData<Language> = _language

    init {
        loadVariables()
    }

    private fun loadVariables() {
        viewModelScope.launch {
            _language.value = settingsRepository.getLanguage()
        }
    }

    private fun saveVariables() {
        viewModelScope.launch {
            settingsRepository.saveLanguage(_language.value!!)
        }
    }

    fun setLanguage(language: Language) {
        _language.value = language
        saveVariables()
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
