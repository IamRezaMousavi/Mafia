package com.github.iamrezamousavi.mafia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.utils.SharedData


class RoleViewModel(application: Application) : AndroidViewModel(application) {

    private val simpleCitizen = application.getString(R.string.simple_citizen)
    private val simpleMafia = application.getString(R.string.simple_mafia)
    private val citizenSide = application.getString(R.string.citizen_side)
    private val mafiaSide = application.getString(R.string.mafia_side)

    private val players = ArrayList(SharedData.players.value!!.filter { it.isChecked })
    private val playersSize = players.size

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
            _selectedRoles.contains(Role(name = simpleCitizen, side = citizenSide))
        val hasSimpleMafia = _selectedRoles.contains(Role(name = simpleMafia, side = mafiaSide))
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
        val selectedMafiaRoles = _selectedRoles.filter { it.side == mafiaSide }
        val hasSimpleMafia =
            selectedMafiaRoles.contains(Role(name = simpleMafia, side = mafiaSide))
        val selectedMafiaRoleSize = selectedMafiaRoles.size

        val maxSimpleMafia = if (hasSimpleMafia) {
            maxMafia - selectedMafiaRoleSize + 1
        } else {
            0
        }

        return maxSimpleMafia
    }

    fun generateRoles() {

    }

    companion object {

        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                if (modelClass.isAssignableFrom(RoleViewModel::class.java)) {
                    return RoleViewModel(application) as T
                }
                return super.create(modelClass)
            }
        }

    }
}
