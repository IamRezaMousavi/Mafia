package com.github.iamrezamousavi.mafia.data.repository

import com.github.iamrezamousavi.mafia.data.local.PlayerStorage
import com.github.iamrezamousavi.mafia.data.model.Player

class PlayerRepository(
    private val playerStorage: PlayerStorage
) {

    fun getPlayers(): ArrayList<Player> = playerStorage.getPlayers()

    fun savePlayers(players: ArrayList<Player>) = playerStorage.savePlayers(players)
}
