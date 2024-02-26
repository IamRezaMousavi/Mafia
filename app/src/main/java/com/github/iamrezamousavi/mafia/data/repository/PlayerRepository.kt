package com.github.iamrezamousavi.mafia.data.repository

import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager

class PlayerRepository(private val sharedPreferencesManager: SharedPreferencesManager) {

    fun getPlayers(): ArrayList<Player> = sharedPreferencesManager.getPlayers()

    fun addPlayer(player: Player) = sharedPreferencesManager.addPlayer(player)

    fun removePlayer(playerId: Int) = sharedPreferencesManager.removePlayer(playerId)

    fun updatePlayer(updatedPlayer: Player) = sharedPreferencesManager.updatePlayer(updatedPlayer)

    fun savePlayers(players: ArrayList<Player>) {
        sharedPreferencesManager.savePlayers(players)
    }
}