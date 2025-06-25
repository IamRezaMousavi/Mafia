package com.github.iamrezamousavi.mafia.utils

import android.content.Context
import android.content.ContextWrapper
import com.github.iamrezamousavi.mafia.data.local.getLanguage
import com.github.iamrezamousavi.mafia.data.local.preferences
import java.util.Locale

@Suppress("DEPRECATION")
fun getContextWrapper(context: Context): ContextWrapper {
    val resources = context.resources
    val configuration = resources.configuration

    val language = context.preferences.getLanguage()
    val locale = language.asSystemLocale()
    Locale.setDefault(locale)

    val currentLocale =
        if (isAtLeastAndroid7) {
            configuration.locales.get(0)
        } else {
            configuration.locale
        }

    if (currentLocale.language == language.code) {
        return ContextWrapper(context)
    }

    configuration.setLocale(locale)

    if (isAtLeastAndroid7) {
        context.createConfigurationContext(configuration)
    }
    resources.updateConfiguration(configuration, resources.displayMetrics)

    return ContextWrapper(context)
}
