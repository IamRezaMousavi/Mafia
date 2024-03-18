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


class RoleViewModel(application: Application) :
    AndroidViewModel(application) {

    private val simpleCitizen = application.getString(R.string.simple_citizen)
    private val simpleMafia = application.getString(R.string.simple_mafia)
    private val citizenSide = application.getString(R.string.citizen_side)
    private val mafiaSide = application.getString(R.string.mafia_side)
    private val independentSide = application.getString(R.string.independent_side)

    private val players = ArrayList(SharedData.players.value!!.filter { it.isChecked })
    private val playersSize = players.size

    private var _selectedRoles = ArrayList<Role>()

    private val _maxSimpleMafia: MutableLiveData<Int> by lazy {
        MutableLiveData(calculateMaxSimpleMafia())
    }
    val maxSimpleMafia: LiveData<Int>
        get() = _maxSimpleMafia

    fun setSelectedRoles(selectedRoles: ArrayList<Role>) {
        _selectedRoles = selectedRoles
        _maxSimpleMafia.value = calculateMaxSimpleMafia()
    }

    private fun calculateMaxSimpleMafia(): Int {
        val maxMafia = if (playersSize % 2 == 1) {
            playersSize / 2
        } else {
            playersSize / 2 - 1
        }
        val selectedMafiaRoles = _selectedRoles.filter { it.side == mafiaSide }
        val hasSimpleMafia = selectedMafiaRoles.contains(Role(name = simpleMafia, side = mafiaSide))
        val selectedMafiaRoleSize = selectedMafiaRoles.size

        val maxSimpleMafia = if (hasSimpleMafia) {
            maxMafia - selectedMafiaRoleSize
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
