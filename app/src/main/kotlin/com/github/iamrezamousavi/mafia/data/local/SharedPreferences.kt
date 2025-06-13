package com.github.iamrezamousavi.mafia.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.toLanguage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val PREF_PLAYERS = "Players"
const val PREF_APP_LANGUAGE = "AppLanguage"

val gson = Gson()

val Context.preferences: SharedPreferences
    get() = getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)

suspend fun SharedPreferences.savePlayers(players: List<Player>) = withContext(Dispatchers.IO) {
    val playersJson = gson.toJson(players)
    edit {
        putString(PREF_PLAYERS, playersJson)
    }
}

suspend fun SharedPreferences.getPlayers(): List<Player> = withContext(Dispatchers.IO) {
    val playersJson = getString(PREF_PLAYERS, null)
    if (playersJson != null) {
        val type = object : TypeToken<List<Player>>() {}.type
        gson.fromJson(playersJson, type)
    } else {
        emptyList()
    }
}

fun SharedPreferences.saveLanguage(language: Language) = edit {
    putString(PREF_APP_LANGUAGE, language.code)
}

fun SharedPreferences.getLanguage() = getString(PREF_APP_LANGUAGE, Language.FA.code).toLanguage()!!
