package com.github.iamrezamousavi.mafia.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.iamrezamousavi.mafia.data.model.Language
import java.util.Locale

object LangData {

    private val _language = MutableLiveData(Language.FA)
    val language: LiveData<Language> = _language

    fun setLanguage(language: Language) {
        _language.postValue(language)
    }

    fun getLanguage(): Language {
        return _language.value!!
    }

    @Suppress("DEPRECATION")
    fun getContextWrapper(
        context: Context,
        languageCode: String
    ): ContextWrapper {
        val resources = context.resources
        val configuration = resources.configuration

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val currentLocale =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.locales.get(0)
            } else {
                configuration.locale
            }

        if (currentLocale.language == languageCode) {
            return ContextWrapper(context)
        }

        configuration.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(configuration)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return ContextWrapper(context)
    }

    fun languageFromName(name: String): Language {
        return when (name) {
            Language.FA.nativeName -> Language.FA
            Language.EN.nativeName -> Language.EN

            else -> throw NotImplementedError()
        }
    }

    fun languageFromCode(code: String): Language {
        return when (code) {
            Language.FA.code -> Language.FA
            Language.EN.code -> Language.EN

            else -> throw NotImplementedError()
        }
    }

    @Suppress("DEPRECATION")
    fun getCurrentLanguage(context: Context): Language {
        val resources = context.resources
        val configuration = resources.configuration

        val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0)
        } else {
            configuration.locale
        }

        return languageFromCode(currentLocale.language)
    }
}
