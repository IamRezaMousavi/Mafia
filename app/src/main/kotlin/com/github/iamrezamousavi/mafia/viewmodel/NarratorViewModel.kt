package com.github.iamrezamousavi.mafia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.data.model.NarratorItem
import com.github.iamrezamousavi.mafia.utils.PlayersData

class NarratorViewModel : ViewModel() {

    private val _playerRoleList = MutableLiveData(
        PlayersData
            .selectedPlayers
            .map { player ->
                NarratorItem(
                    id = player.id,
                    player = player,
                    role = PlayersData.getRole(player),
                    isAlive = true
                )
            }
    )
    val playerRoleList: LiveData<List<NarratorItem>>
        get() = _playerRoleList

    private fun setPlayerRoles(updatedList: List<NarratorItem>) {
        _playerRoleList.value = updatedList
            .sortedBy { it.player.name }
            .sortedBy { !it.isAlive }
    }

    fun updateItem(item: NarratorItem) {
        val updatedItems = _playerRoleList.value!!.toMutableList()
        val itemIndex = updatedItems.indexOfFirst { it.id == item.id }
        if (itemIndex != -1) {
            updatedItems[itemIndex] = item
            setPlayerRoles(updatedItems)
        }
    }
}
