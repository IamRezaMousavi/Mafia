package com.github.iamrezamousavi.mafia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player

class RoleViewModel : ViewModel() {
    private val players = MutableLiveData<ArrayList<Player>>()
    private val roles = MutableLiveData<ArrayList<String>>()

    fun getPlayers(): LiveData<ArrayList<Player>> = players
    fun getRoles(): LiveData<ArrayList<String>> = roles

    fun setPlayers(players: ArrayList<Player>) {
        this.players.value = ArrayList(
            players.filter { it.isChecked }
        )
    }

    fun setRoles(roles: ArrayList<String>) {
        val playersSize = players.value?.size ?: 0
        var playerRoles = roles
        while (playerRoles.size < playersSize) {
            playerRoles.add(R.string.citizen.toString())
        }
        playerRoles = ArrayList(playerRoles.shuffled())
        playerRoles = ArrayList(playerRoles.shuffled())
        this.roles.value = playerRoles
    }

    fun shuffled() {
        this.roles.value = this.roles.value?.shuffled()?.let { ArrayList(it) }
    }

    fun getRole(player: Player): String {
        val index = players.value!!.indexOf(player)
        return if (index < 0) {
            ""
        } else {
            roles.value!![index]
        }
    }
}
