package com.github.iamrezamousavi.mafia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.ResultType
import com.github.iamrezamousavi.mafia.utils.SharedData
import com.github.iamrezamousavi.mafia.utils.getSide

class RoleViewModel(
    players: ArrayList<Player>
) : ViewModel() {

    var playersSize = players.filter { it.isChecked }.size

    var selectedRoles = ArrayList<Role>()

    private val _selectedRolesSize = MutableLiveData(selectedRoles.size)
    val selectedRolesSize: LiveData<Int>
        get() = _selectedRolesSize

    private val _generatedRoles = MutableLiveData(ArrayList<Role>())
    val generatedRoles: LiveData<ArrayList<Role>>
        get() = _generatedRoles

    private val _mafiaSize = MutableLiveData(1)
    val mafiaSize: LiveData<Int>
        get() = _mafiaSize

    private val _citizenSize = MutableLiveData(1)
    val citizenSize: LiveData<Int>
        get() = _citizenSize

    private fun getIndependentSize(): Int =
        selectedRoles.filter { getSide(it.name) == R.string.independent_side }.size

    private fun calculateCitizenSize(): Int = playersSize - mafiaSize.value!! - getIndependentSize()

    fun calculateMaxMafia(): Int {
        return if (playersSize % 2 == 1) {
            playersSize / 2
        } else {
            playersSize / 2 - 1
        }
    }

    fun calculateMinMafia(): Int {
        val minMafia = selectedRoles
            .filter { getSide(it.name) == R.string.mafia_side }
            .size
        return if (minMafia > 1) minMafia else 1
    }

    fun setMafiaSize(newMafiaSize: Int) {
        _mafiaSize.value = newMafiaSize
        _citizenSize.value = calculateCitizenSize()
        _generatedRoles.value = generateRoles()
    }

    fun checkSelectedRolesIsOk(selectedRoles: ArrayList<Role>): ResultType<Boolean, MafiaError> {
        val mafiaSize = selectedRoles.filter { getSide(it.name) == R.string.mafia_side }.size
        val isMafiaRoleOk = if (playersSize % 2 == 1) {
            mafiaSize <= playersSize / 2
        } else {
            mafiaSize < playersSize / 2
        }

        val isRoleSizeOk = _selectedRolesSize.value!! <= playersSize

        return when {
            isMafiaRoleOk && isRoleSizeOk -> ResultType.success(true)
            isMafiaRoleOk && !isRoleSizeOk -> ResultType.error(MafiaError.SelectedRoleTooMuch)
            else -> ResultType.error(MafiaError.MafiaRoleTooMatch)
        }
    }

    fun setSelectedRoles(selectedRoles: ArrayList<Role>): ResultType<Boolean, MafiaError> {
        this.selectedRoles = selectedRoles
        _selectedRolesSize.value = selectedRoles.size

        val checkResult = checkSelectedRolesIsOk(selectedRoles)
        if (checkResult.isSuccess) {
            _mafiaSize.value = calculateMinMafia()
            _citizenSize.value = calculateCitizenSize()
        }
        return checkResult
    }

    private fun generateRoles(): ArrayList<Role> {
        val roles = ArrayList(selectedRoles)

        var mafiaSizeInSelectedRoles = selectedRoles
            .filter { getSide(it.name) == R.string.mafia_side }
            .size
        while (mafiaSizeInSelectedRoles < mafiaSize.value!!) {
            roles.add(Role(name = R.string.simple_mafia))
            mafiaSizeInSelectedRoles += 1
        }

        var citizenSizeInSelectedRoles = selectedRoles
            .filter { getSide(it.name) == R.string.citizen_side }
            .size
        while (citizenSizeInSelectedRoles < citizenSize.value!!) {
            roles.add(Role(name = R.string.simple_citizen))
            citizenSizeInSelectedRoles += 1
        }

        SharedData.setRoles(roles)

        roles.sortBy { it.name }
        return roles
    }
}
