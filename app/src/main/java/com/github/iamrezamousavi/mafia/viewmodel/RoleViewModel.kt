package com.github.iamrezamousavi.mafia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.data.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RoleViewModel @Inject constructor() : ViewModel() {

    private val _players = MutableLiveData<ArrayList<Player>>()
    val players: LiveData<ArrayList<Player>>
        get() = _players

    private val _roles = MutableLiveData<ArrayList<String>>()
    val roles: LiveData<ArrayList<String>>
        get() = _roles

    private var simpleCitizen = ""

    fun setPlayers(players: ArrayList<Player>) {
        _players.value = ArrayList(
            players.filter { it.isChecked }
        )
    }

    fun setRoles(roles: ArrayList<String>) {
        val playersSize = _players.value?.size ?: 0
        var playerRoles = roles
        while (playerRoles.size < playersSize) {
            playerRoles.add(simpleCitizen)
        }
        playerRoles = ArrayList(playerRoles.shuffled())
        playerRoles = ArrayList(playerRoles.shuffled())
        _roles.value = playerRoles
    }

    fun shuffled() {
        _roles.value = _roles.value?.shuffled()?.let { ArrayList(it) }
    }

    fun getRole(player: Player): String {
        val index = _players.value!!.indexOf(player)
        return if (index < 0) {
            ""
        } else {
            _roles.value!![index]
        }
    }

    fun setSimpleCitizenText(text: String) {
        this.simpleCitizen = text
    }
}
