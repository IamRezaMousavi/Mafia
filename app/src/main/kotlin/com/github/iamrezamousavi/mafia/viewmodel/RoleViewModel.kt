package com.github.iamrezamousavi.mafia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.utils.SharedData
import com.github.iamrezamousavi.mafia.utils.getSide


class RoleViewModel : ViewModel() {

    private val simpleCitizen = R.string.simple_citizen
    private val simpleMafia = R.string.simple_mafia
    private val mafiaSide = R.string.mafia_side

    private val players = ArrayList(SharedData.players.value!!.filter { it.isChecked })
    val playersSize = players.size

    private var _selectedRoles = ArrayList<Role>()

    private val _maxSimpleMafia = MutableLiveData(calculateMaxSimpleMafia())
    val maxSimpleMafia: LiveData<Int>
        get() = _maxSimpleMafia

    private val _minSimpleMafia = MutableLiveData(1)
    val minSimpleMafia: LiveData<Int>
        get() = _minSimpleMafia

    private val _simpleMafiaCounter = MutableLiveData(1)
    val simpleMafiaCounter: LiveData<Int>
        get() = _simpleMafiaCounter

    private val _simpleCitizenCounter = MutableLiveData(1)
    val simpleCitizenCounter: LiveData<Int>
        get() = _simpleCitizenCounter

    fun setSimpleMafiaCounter(value: Int) {
        if (value < minSimpleMafia.value!! && value > maxSimpleMafia.value!!)
            return
        _simpleMafiaCounter.value = value
        _simpleCitizenCounter.value = calculateSimpleCitizenCounter()
    }

    fun setSelectedRoles(selectedRoles: ArrayList<Role>) {
        _selectedRoles = selectedRoles
        val max = calculateMaxSimpleMafia()
        if (max == 0) {
            _minSimpleMafia.value = 0
            _simpleMafiaCounter.value = 0
            _maxSimpleMafia.value = 0
        } else {
            _maxSimpleMafia.value = max
            _simpleMafiaCounter.value = 1
            _minSimpleMafia.value = 1
        }
        _simpleCitizenCounter.value = calculateSimpleCitizenCounter()
    }

    private fun calculateSimpleCitizenCounter(): Int {
        val hasSimpleCitizen =
            _selectedRoles.contains(Role(name = simpleCitizen))
        val hasSimpleMafia = _selectedRoles.contains(Role(name = simpleMafia))
        return when {
            hasSimpleCitizen && hasSimpleMafia ->
                playersSize - _selectedRoles.size - _simpleMafiaCounter.value!! + 2

            hasSimpleCitizen -> playersSize - _selectedRoles.size + 1

            else -> 0
        }
    }

    private fun calculateMaxSimpleMafia(): Int {
        val maxMafia = if (playersSize % 2 == 1) {
            playersSize / 2
        } else {
            playersSize / 2 - 1
        }
        val selectedMafiaRoles = _selectedRoles.filter { getSide(it.name) == mafiaSide }
        val hasSimpleMafia =
            selectedMafiaRoles.contains(Role(name = simpleMafia))
        val selectedMafiaRoleSize = selectedMafiaRoles.size

        val maxSimpleMafia = if (hasSimpleMafia) {
            maxMafia - selectedMafiaRoleSize + 1
        } else {
            0
        }

        return maxSimpleMafia
    }

    fun checkRolesIsOk(selectedRoles: ArrayList<Role>): Boolean {
        val hasSimpleMafia = selectedRoles.contains(Role(name = simpleMafia))
        val hasSimpleCitizen = selectedRoles.contains(Role(name = simpleCitizen))

        return when {
            hasSimpleCitizen && hasSimpleMafia ->
                playersSize == selectedRoles.size +
                        _simpleMafiaCounter.value!! + _simpleCitizenCounter.value!! - 2

            hasSimpleCitizen ->
                playersSize == selectedRoles.size + _simpleCitizenCounter.value!! - 1

            hasSimpleMafia -> playersSize == selectedRoles.size + _simpleMafiaCounter.value!! - 1

            else -> playersSize == selectedRoles.size

        }
    }

    fun generateRoles(selectedRoles: ArrayList<Role>) {
        if (selectedRoles.size < playersSize) {
            for (i in 1.._simpleMafiaCounter.value!!)
                selectedRoles.add(Role(name = simpleMafia))
            for (i in 1.._simpleCitizenCounter.value!!)
                selectedRoles.add(Role(name = simpleCitizen))
        }
        SharedData.setRoles(selectedRoles)
    }

    companion object {

        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                if (modelClass.isAssignableFrom(RoleViewModel::class.java)) {
                    return RoleViewModel() as T
                }
                return super.create(modelClass)
            }
        }

    }
}
