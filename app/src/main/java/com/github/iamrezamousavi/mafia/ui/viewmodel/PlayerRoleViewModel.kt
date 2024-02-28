package com.github.iamrezamousavi.mafia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.data.model.Player

class PlayerRoleViewModel : ViewModel() {
    private val players = MutableLiveData<ArrayList<Player>>()
    fun getPlayers(): LiveData<ArrayList<Player>> = players

    fun setPlayers(players: ArrayList<Player>) {
        this.players.value = players
    }
}
