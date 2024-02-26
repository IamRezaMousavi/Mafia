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
        players.value = repository.getPlayers()
    }

    fun addPlayer(player: Player) {
        repository.addPlayer(player)
        loadPlayers()
    }

    fun removePlayer(playerId: Int) {
        repository.removePlayer(playerId)
        loadPlayers()
    }

    fun updatePlayer(updatedPlayer: Player) {
        repository.updatePlayer(updatedPlayer)
        loadPlayers()
    }

    fun savePlayers() {
        repository.savePlayers(players.value ?: ArrayList())
        loadPlayers()
    }
}