package com.github.iamrezamousavi.mafia.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role

object SharedData {

    private val _players = MutableLiveData<ArrayList<Player>>()
    val players: LiveData<ArrayList<Player>>
        get() = _players

    private val _roles = MutableLiveData(ArrayList<Role>())
    val roles: LiveData<ArrayList<Role>>
        get() = _roles

    fun setPlayers(players: ArrayList<Player>) {
        _players.value = players
    }

    fun getPlayers(): ArrayList<Player> {
        return ArrayList(_players.value ?: ArrayList())
    }

    fun setRoles(roles: ArrayList<Role>) {
        _roles.value = roles
    }

    fun getRoles(): ArrayList<Role> = _roles.value!!

    fun isPlayersOk(): Boolean {
        val playersList = getPlayers()
        val checkedPlayers = playersList.filter { it.isChecked }
        return checkedPlayers.size >= 3
    }

    fun shuffled() {
        val currentRoles = _roles.value
        var nextRoles: ArrayList<Role>
        do {
            nextRoles = ArrayList(_roles.value!!.shuffled())
        } while (nextRoles == currentRoles)
        _roles.value = nextRoles
    }

    fun getRole(player: Player): Role {
        val index = _players.value!!.indexOf(player)
        return _roles.value!![index]
    }
}
