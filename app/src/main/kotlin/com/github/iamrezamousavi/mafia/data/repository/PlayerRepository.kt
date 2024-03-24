package com.github.iamrezamousavi.mafia.data.repository

import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager

class PlayerRepository(
    private val sharedPreferencesManager: SharedPreferencesManager
) {

    fun getPlayers(): ArrayList<Player> = sharedPreferencesManager.getPlayers()

    fun savePlayers(players: ArrayList<Player>) {
        sharedPreferencesManager.savePlayers(players)
    }
}
