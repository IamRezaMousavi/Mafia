package com.github.iamrezamousavi.mafia.data.source

import android.content.Context
import com.github.iamrezamousavi.mafia.data.model.Player
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
    private val playersKey = "Players"

    private val gson = Gson()

    fun savePlayers(players: ArrayList<Player>) {
        val playersJson = gson.toJson(players)
        sharedPreferences
            .edit()
            .putString(playersKey, playersJson)
            .apply()
    }

    fun getPlayers(): ArrayList<Player> {
        val playersJson = sharedPreferences.getString(playersKey, null)
        return if (playersJson != null) {
            val type = object : TypeToken<ArrayList<Player>>() {}.type
            gson.fromJson(playersJson, type)
        } else {
            ArrayList()
        }
    }
}