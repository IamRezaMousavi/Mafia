package com.github.iamrezamousavi.mafia.data.local

import android.content.Context
import androidx.core.content.edit
import com.github.iamrezamousavi.mafia.data.model.Player
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayerStorage(applicationContext: Context) {
    private val sharedPreferences =
        applicationContext.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
    private val playersKey = "Players"

    private val gson = Gson()

    fun savePlayers(players: List<Player>) {
        val playersJson = gson.toJson(players)
        sharedPreferences
            .edit {
                putString(playersKey, playersJson)
            }
    }

    fun getPlayers(): List<Player> {
        val playersJson = sharedPreferences.getString(playersKey, null)
        return if (playersJson != null) {
            val type = object : TypeToken<List<Player>>() {}.type
            gson.fromJson(playersJson, type)
        } else {
            emptyList()
        }
    }
}
