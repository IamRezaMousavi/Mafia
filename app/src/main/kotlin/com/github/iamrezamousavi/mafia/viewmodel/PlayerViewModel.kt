package com.github.iamrezamousavi.mafia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository

class PlayerViewModel(private val repository: PlayerRepository) : ViewModel() {

    private val _players = MutableLiveData<ArrayList<Player>>()
    val players: LiveData<ArrayList<Player>>
        get() = _players

    fun loadPlayers(): ArrayList<Player> {
        // update LiveData
        val loadedPlayers = repository.getPlayers()
        _players.value = loadedPlayers
        return loadedPlayers
    }

    fun addPlayer(player: Player) {
        val updatedPlayers = ArrayList(_players.value ?: ArrayList())
        val lastIndex = updatedPlayers.lastIndex
        player.id = lastIndex + 1
        updatedPlayers.add(player)
        // update LiveData
        _players.value = updatedPlayers
    }

    fun removePlayer(playerId: Int) {
        val updatedPlayers = ArrayList(_players.value ?: ArrayList())
        updatedPlayers.removeAll { it.id == playerId }
        _players.value = updatedPlayers
    }

    fun updatePlayer(updatedPlayer: Player) {
        val updatedPlayers = ArrayList(_players.value ?: ArrayList())
        val playerIndex = updatedPlayers.indexOfFirst { it.id == updatedPlayer.id }
        if (playerIndex != -1) {
            updatedPlayers[playerIndex] = updatedPlayer
            _players.value = updatedPlayers
        }
    }

    fun selectAllPlayer() {
        val updatedPlayers = _players.value ?: return
        updatedPlayers.forEach { player ->
            player.isChecked = true
        }
        _players.value = updatedPlayers
    }

    fun savePlayers() {
        repository.savePlayers(_players.value ?: ArrayList())
    }
}