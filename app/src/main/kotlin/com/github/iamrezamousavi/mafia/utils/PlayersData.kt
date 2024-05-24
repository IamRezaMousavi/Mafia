package com.github.iamrezamousavi.mafia.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role

object PlayersData {

    private val _players = MutableLiveData<ArrayList<Player>>()
    val players: LiveData<ArrayList<Player>>
        get() = _players

    val selectedPlayers = ArrayList<Player>()

    private val _roles = MutableLiveData(ArrayList<Role>())
    val roles: LiveData<ArrayList<Role>>
        get() = _roles

    private val playersRoles = mutableMapOf<Player, Role>()

    fun setPlayers(players: ArrayList<Player>) {
        _players.value = players
        selectedPlayers.clear()
        selectedPlayers.addAll(
            players.filter { it.isChecked }
        )
    }

    fun getPlayers(): ArrayList<Player> {
        return ArrayList(_players.value.orEmpty())
    }

    fun setRoles(roles: ArrayList<Role>) {
        _roles.value = roles
    }

    fun getRoles(): ArrayList<Role> = ArrayList(_roles.value.orEmpty())

    fun isPlayersOk(): Boolean {
        val playersList = getPlayers()
        val checkedPlayers = playersList.filter { it.isChecked }
        return checkedPlayers.size >= 3
    }

    fun isAllPlayersGetRoles(): Boolean {
        return selectedPlayers.size == playersRoles.size
    }

    fun refresh() {
        val currentRoles = getRoles()
        var nextRoles: ArrayList<Role>
        do {
            nextRoles = ArrayList(currentRoles.shuffled())
        } while (roleArrayEqual(nextRoles, currentRoles))
        _roles.value = nextRoles
        playersRoles.clear()
    }

    private fun roleArrayEqual(arr1: ArrayList<Role>, arr2: ArrayList<Role>): Boolean {
        if (arr1.size != arr2.size)
            return false
        val pairArr = arr1.zip(arr2)
        return pairArr.all { (e1, e2) -> e1 == e2 }
    }

    fun getRole(player: Player): Role {
        val index = selectedPlayers.indexOf(player)
        val roles = getRoles()
        val role = roles[index]
        playersRoles[player] = role
        return role
    }
}
