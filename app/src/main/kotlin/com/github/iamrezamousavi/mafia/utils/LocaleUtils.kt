package com.github.iamrezamousavi.mafia.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import com.github.iamrezamousavi.mafia.data.model.Language
import java.util.Locale


@Suppress("DEPRECATION")
fun changeLanguage(context: Context, languageCode: String): ContextWrapper {
    val resources = context.resources
    val configuration = resources.configuration

    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val currentLocale =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            configuration.getLocales().get(0)
        else
            configuration.locale

    if (currentLocale.language == languageCode)
        return ContextWrapper(context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        configuration.setLocale(locale)
    } else {
        configuration.locale = locale
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.createConfigurationContext(configuration)
    }
    resources.updateConfiguration(configuration, resources.displayMetrics)


    return ContextWrapper(context)
}

fun nameToLanguage(name: String): Language {
    return when (name) {
        Language.FA.nativeName -> Language.FA
        Language.EN.nativeName -> Language.EN

        else -> throw NotImplementedError()
    }
}

fun codeToLanguage(code: String): Language {
    return when (code) {
        Language.FA.code -> Language.FA
        Language.EN.code -> Language.EN

        else -> throw NotImplementedError()
    }
}

@Suppress("DEPRECATION")
fun getCurrentLocale(context: Context): Locale {
    val resources = context.resources
    val configuration = resources.configuration

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        configuration.getLocales().get(0)
    else
        configuration.locale
}
