package com.github.iamrezamousavi.mafia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.utils.SharedData
import com.github.iamrezamousavi.mafia.utils.getSide

class RoleViewModel(
    players: ArrayList<Player>
) : ViewModel() {

    private val simpleCitizen = R.string.simple_citizen
    private val simpleMafia = R.string.simple_mafia
    private val mafiaSide = R.string.mafia_side
    private val citizenSide = R.string.citizen_side

    var playersSize = players.filter { it.isChecked }.size

    var selectedRoles = ArrayList<Role>()

    private val _selectedRolesSize = MutableLiveData(selectedRoles.size)
    val selectedRolesSize: LiveData<Int>
        get() = _selectedRolesSize

    private val _generatedRoles = MutableLiveData<ArrayList<Role>>()
    val generatedRoles: LiveData<ArrayList<Role>>
        get() = _generatedRoles

    private val _mafiaSize = MutableLiveData(1)
    val mafiaSize: LiveData<Int>
        get() = _mafiaSize

    private val _citizenSize = MutableLiveData(calculateCitizenSize())
    val citizenSize: LiveData<Int>
        get() = _citizenSize

    fun setMafiaSize(newMafiaSize: Int) {
        _mafiaSize.value = newMafiaSize
        _generatedRoles.value = selectedRoles
    }

    fun setSelectedRoles(selectedRoles: ArrayList<Role>): Boolean {
        if (!checkSelectedRolesIsOk(selectedRoles)) {
            return false
        }
        this.selectedRoles = selectedRoles
        _selectedRolesSize.value = selectedRoles.size
        _citizenSize.value = calculateCitizenSize()
        return true
    }

    fun checkSelectedRolesIsOk(selectedRoles: ArrayList<Role>): Boolean {
        val mafiaSize = selectedRoles.filter { getSide(it.name) == R.string.mafia_side }.size
        return if (playersSize % 2 ==
            1
        ) {
            mafiaSize <= playersSize / 2
        } else {
            mafiaSize < playersSize / 2
        }
    }

    private fun calculateMaxSimpleMafia(): Int {
        val maxMafia = if (playersSize % 2 == 1) {
            playersSize / 2
        } else {
            playersSize / 2 - 1
        }
        val selectedMafiaRoles = selectedRoles.filter { getSide(it.name) == mafiaSide }
        val hasSimpleMafia =
            selectedMafiaRoles.contains(Role(name = simpleMafia))
        val selectedMafiaRoleSize = selectedMafiaRoles.size

        if (hasSimpleMafia) {
            var maxSimpleMafia = maxMafia - selectedMafiaRoleSize + 1
            if (playersSize - selectedRoles.size < maxSimpleMafia) {
                maxSimpleMafia = playersSize - selectedRoles.size + 1
            }
            return maxSimpleMafia
        }
        return 0
    }

    private fun getIndependentSize(): Int =
        selectedRoles.filter { getSide(it.name) == R.string.independent_side }.size

    private fun calculateCitizenSize(): Int = playersSize - mafiaSize.value!! - getIndependentSize()

    fun generateRoles(
        simpleMafiaCounter: Int,
        simpleCitizenCounter: Int
    ) {
        if (selectedRoles.size < playersSize) {
            for (i in 1..<simpleMafiaCounter)
                selectedRoles.add(Role(name = simpleMafia))
            for (i in 1..<simpleCitizenCounter)
                selectedRoles.add(Role(name = simpleCitizen))
        }
        SharedData.setRoles(selectedRoles)
    }

    companion object {

        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                if (modelClass.isAssignableFrom(RoleViewModel::class.java)) {
                    return RoleViewModel(SharedData.getPlayers()) as T
                }
                return super.create(modelClass)
            }
        }
    }
}
