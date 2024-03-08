package com.github.iamrezamousavi.mafia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerRepository
) : ViewModel() {

    private val _players = MutableLiveData<ArrayList<Player>>()
    val players: LiveData<ArrayList<Player>>
        get() = _players

    fun loadPlayers() {
        // update LiveData
        _players.value = repository.getPlayers()
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