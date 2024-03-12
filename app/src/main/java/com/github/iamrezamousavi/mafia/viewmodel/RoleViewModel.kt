package com.github.iamrezamousavi.mafia.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player


class RoleViewModel(context: Context) : ViewModel() {

    private var simpleCitizen = context.getString(R.string.simple_citizen)
    private var simpleMafia = context.getString(R.string.simple_mafia)

    private val _players = MutableLiveData<ArrayList<Player>>()
    val players: LiveData<ArrayList<Player>>
        get() = _players

    private val _roles = MutableLiveData<ArrayList<String>>()
    val roles: LiveData<ArrayList<String>>
        get() = _roles

    private val _simpleMafiaCounter = MutableLiveData(1)
    val simpleMafiaCounter: LiveData<Int>
        get() = _simpleMafiaCounter

    private val _maxSimpleMafia = MutableLiveData(1)
    val maxSimpleMafia: LiveData<Int>
        get() = _maxSimpleMafia

    var simpleCitizenCounter = 0

    fun updateSimpleMafiaCounter() {
        val currentValue = _simpleMafiaCounter.value ?: 1
        val maxValue = _maxSimpleMafia.value ?: 1
        val newValue = if (currentValue > maxValue) {
            maxValue
        } else {
            currentValue
        }
        _simpleMafiaCounter.value = newValue
    }

    fun updateMaxSimpleMafia(mafiaCheckedSize: Int, hasSimpleMafia: Boolean = true) {
        val newValue = if (players.value == null || players.value?.size == null) {
            1
        } else {
            val playersSize = players.value?.size!!
            val maxMafia = if (playersSize % 2 == 1) {
                playersSize / 2
            } else {
                playersSize / 2 - 1
            }
            if (hasSimpleMafia) {
                maxMafia - mafiaCheckedSize
            } else {
                0
            }
        }
        _maxSimpleMafia.value = newValue
    }

    fun updateSimpleCitizenCounter() {
        val newValue =
            if (players.value == null || players.value?.size == null || roles.value == null || roles.value?.size == null) {
                1
            } else {
                val playersSize = players.value?.size!!
                val rolesSize = roles.value?.size!!
                playersSize - rolesSize - (simpleMafiaCounter.value ?: 0) + 1
            }
        simpleCitizenCounter = newValue
    }

    fun setSimpleMafiaCounter(value: Int) {
        _simpleMafiaCounter.value = value
    }

    fun setPlayersAndRoles(players: ArrayList<Player>, roles: ArrayList<String>) {
        // set players
        _players.value = ArrayList(players.filter { it.isChecked })

        // set roles
        _roles.value = roles

        // calculate number of simple citizen and simple mafia
        // TODO
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
}
