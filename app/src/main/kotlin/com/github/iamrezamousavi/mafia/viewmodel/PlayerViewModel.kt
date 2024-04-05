package com.github.iamrezamousavi.mafia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.iamrezamousavi.mafia.data.local.PlayerStorage
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import com.github.iamrezamousavi.mafia.utils.SharedData
import kotlinx.coroutines.launch

class PlayerViewModel(
    context: Context
) : ViewModel() {

    private val repository = PlayerRepository(PlayerStorage(context))

    fun loadPlayers() {
        viewModelScope.launch {
            val loadedPlayers = repository.getPlayers()
            SharedData.setPlayers(loadedPlayers)
        }
    }

    fun addPlayer(name: String) {
        val updatedPlayers = SharedData.getPlayers()
        val lastIndex = updatedPlayers.lastIndex
        updatedPlayers.add(
            Player(id = lastIndex + 1, name = name)
        )
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
        updatedPlayers.forEach { player ->
            player.isChecked = true
        }
        SharedData.setPlayers(updatedPlayers)
    }

    fun savePlayers() {
        viewModelScope.launch {
            repository.savePlayers(SharedData.getPlayers())
        }
    }

    companion object {

        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                    return PlayerViewModel(application.baseContext) as T
                }
                return super.create(modelClass)
            }
        }
    }
}
