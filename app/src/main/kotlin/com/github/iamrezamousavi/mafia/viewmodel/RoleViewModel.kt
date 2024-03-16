package com.github.iamrezamousavi.mafia.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role


class RoleViewModel(context: Context, players: ArrayList<Player>) : ViewModel() {

    private val simpleCitizen = context.getString(R.string.simple_citizen)
    private val simpleMafia = context.getString(R.string.simple_mafia)
    private val citizenSide = context.getString(R.string.citizen_side)
    private val mafiaSide = context.getString(R.string.mafia_side)
    private val independentSide = context.getString(R.string.independent_side)

    private val players = ArrayList(players.filter { it.isChecked })
    private val playersSize = players.size

    private lateinit var selectedRoles: ArrayList<Role>
    private var selectedRolesSize = 1

    private val _roles = MutableLiveData<ArrayList<Role>>()
    val roles: LiveData<ArrayList<Role>>
        get() = _roles

    private val _simpleCitizenCounter = MutableLiveData(1)
    val simpleCitizenCounter: LiveData<Int>
        get() = _simpleCitizenCounter

    private val _simpleMafiaCounter = MutableLiveData(1)
    val simpleMafiaCounter: LiveData<Int>
        get() = _simpleMafiaCounter

    private val _maxSimpleMafia = MutableLiveData(1)
    val maxSimpleMafia: LiveData<Int>
        get() = _maxSimpleMafia

    fun increaseSimpleMafia() {
        setSimpleMafiaCounter(_simpleMafiaCounter.value!! + 1)
        setSimpleCitizenCounter(_simpleCitizenCounter.value!! - 1)
    }

    fun decreaseSimpleMafia() {
        setSimpleMafiaCounter(_simpleMafiaCounter.value!! - 1)
        setSimpleCitizenCounter(_simpleCitizenCounter.value!! + 1)
    }

    fun setSimpleMafiaCounter(value: Int) {
        val oldValue = _simpleMafiaCounter.value
        if (value != oldValue) _simpleMafiaCounter.value = value
    }

    private fun setSimpleCitizenCounter(value: Int) {
        val oldValue = _simpleCitizenCounter.value
        if (value != oldValue) _simpleCitizenCounter.value = value
    }

    private fun calculateSimpleCitizenCounter() {
        val simpleCitizen = playersSize - selectedRolesSize + 1
        val oldValue = _simpleCitizenCounter.value
        if (simpleCitizen != oldValue) _simpleCitizenCounter.value = simpleCitizen
    }

    private fun calculateMaxSimpleMafia(
        selectedMafiaRoleSize: Int, hasSimpleMafia: Boolean
    ) {
        val maxMafia = if (playersSize % 2 == 1) {
            playersSize / 2
        } else {
            playersSize / 2 - 1
        }
        val newValue = if (hasSimpleMafia) {
            maxMafia - selectedMafiaRoleSize
        } else {
            0
        }

        _maxSimpleMafia.value = newValue
    }

    fun setSelectedRoles(selectedRoles: ArrayList<Role>) {
        this.selectedRoles = selectedRoles
        this.selectedRolesSize = selectedRoles.size
        updateVariables()
    }

    private fun updateVariables() {
        // calculate number of simple citizen and simple mafia
        val selectedMafiaRoleSize = selectedRoles.filter { it.side == mafiaSide }.size
        val hasSimpleMafia = selectedRoles.contains(
            Role(name = simpleMafia, side = mafiaSide)
        )

        calculateSimpleCitizenCounter()

        if (hasSimpleMafia) {
            calculateMaxSimpleMafia(selectedMafiaRoleSize, true)
            setSimpleMafiaCounter(1)
        } else {
            calculateMaxSimpleMafia(selectedMafiaRoleSize, false)
            setSimpleMafiaCounter(0)
        }
    }

    fun shuffled() {
        _roles.value = _roles.value?.shuffled()?.let { ArrayList(it) }
    }

    fun generateRoles() {

    }

    fun getRole(player: Player): String {
        val index = players.indexOf(player)
        return if (index < 0) {
            ""
        } else {
            _roles.value!![index].name
        }
    }
}
