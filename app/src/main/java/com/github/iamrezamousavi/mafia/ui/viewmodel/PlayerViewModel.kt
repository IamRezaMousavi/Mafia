package com.github.iamrezamousavi.mafia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository

class PlayerViewModel(private val repository: PlayerRepository) : ViewModel() {
    private val players = MutableLiveData<ArrayList<Player>>()

    fun getPlayers(): LiveData<ArrayList<Player>> = players

    fun loadPlayers() {
        // update LiveData
        players.value = repository.getPlayers()
    }

    fun addPlayer(player: Player) {
        val updatedPlayers = ArrayList(players.value ?: ArrayList())
        val lastIndex = updatedPlayers.lastIndex
        player.id = lastIndex + 1
        updatedPlayers.add(player)
        // update LiveData
        players.value = updatedPlayers
    }

    fun removePlayer(playerId: Int) {
        val updatedPlayers = ArrayList(players.value ?: ArrayList())
        updatedPlayers.removeAll { it.id == playerId }
        players.value = updatedPlayers
    }

    fun updatePlayer(updatedPlayer: Player) {
        val updatedPlayers = ArrayList(players.value ?: ArrayList())
        val playerIndex = updatedPlayers.indexOfFirst { it.id == updatedPlayer.id }
        if (playerIndex != -1) {
            updatedPlayers[playerIndex] = updatedPlayer
            players.value = updatedPlayers
        }
    }

    fun selectAllPlayer() {
        val updatedPlayers = players.value ?: return
        updatedPlayers.forEach { player ->
            player.isChecked = true
        }
        players.value = updatedPlayers
    }

    fun savePlayers() {
        repository.savePlayers(players.value ?: ArrayList())
    }
}