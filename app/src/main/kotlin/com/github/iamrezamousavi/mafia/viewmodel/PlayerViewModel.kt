package com.github.iamrezamousavi.mafia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager
import com.github.iamrezamousavi.mafia.utils.SharedData

class PlayerViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        PlayerRepository(SharedPreferencesManager(application.applicationContext))

    fun loadPlayers(): ArrayList<Player> {
        // update LiveData
        val loadedPlayers = repository.getPlayers()
        SharedData.setPlayers(loadedPlayers)
        return loadedPlayers
    }

    fun addPlayer(player: Player) {
        val updatedPlayers = SharedData.getPlayers()
        val lastIndex = updatedPlayers.lastIndex
        player.id = lastIndex + 1
        updatedPlayers.add(player)
        // update LiveData
        SharedData.setPlayers(updatedPlayers)
    }

    fun removePlayer(playerId: Int) {
        val updatedPlayers = SharedData.getPlayers()
        updatedPlayers.removeAll { it.id == playerId }
        SharedData.setPlayers(updatedPlayers)
    }

    fun updatePlayer(updatedPlayer: Player) {
        val updatedPlayers = SharedData.getPlayers()
        val playerIndex = updatedPlayers.indexOfFirst { it.id == updatedPlayer.id }
        if (playerIndex != -1) {
            updatedPlayers[playerIndex] = updatedPlayer
            SharedData.setPlayers(updatedPlayers)
        }
    }

    fun selectAllPlayer() {
        val updatedPlayers = SharedData.getPlayers()
        if (updatedPlayers.size == 0) return

        updatedPlayers.forEach { player ->
            player.isChecked = true
        }
        SharedData.setPlayers(updatedPlayers)
    }

    fun savePlayers() {
        repository.savePlayers(SharedData.getPlayers())
    }


    companion object {

        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                    return PlayerViewModel(application) as T
                }
                return super.create(modelClass)
            }
        }

    }
}