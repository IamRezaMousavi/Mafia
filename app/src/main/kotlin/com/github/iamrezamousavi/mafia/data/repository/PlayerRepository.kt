package com.github.iamrezamousavi.mafia.data.repository

import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.local.PlayerStorage

class PlayerRepository(
    private val playerStorage: PlayerStorage
) {

    fun getPlayers(): ArrayList<Player> = playerStorage.getPlayers()

    fun savePlayers(players: ArrayList<Player>) = playerStorage.savePlayers(players)

}
