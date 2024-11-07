package com.github.iamrezamousavi.mafia

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.iamrezamousavi.mafia.data.local.PlayerStorage
import com.github.iamrezamousavi.mafia.data.model.NarratorItem
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.ResultType
import com.github.iamrezamousavi.mafia.utils.getSide

class MainViewModel : ViewModel() {

    private val _players = MutableLiveData<List<Player>>(mutableListOf())
    val players: LiveData<List<Player>>
        get() = _players

    val selectedPlayers = ArrayList<Player>()
    val playersSize: Int
        get() = selectedPlayers.size

    val selectedRoles = ArrayList<Role>()

    private val _selectedRolesSize = MutableLiveData(selectedRoles.size)
    val selectedRolesSize: LiveData<Int>
        get() = _selectedRolesSize

    private val _citizenSize = MutableLiveData(1)
    val citizenSize: LiveData<Int>
        get() = _citizenSize

    private val _mafiaSize = MutableLiveData(1)
    val mafiaSize: LiveData<Int>
        get() = _mafiaSize

    private val _roles = MutableLiveData(emptyList<Role>())
    val roles: LiveData<List<Role>>
        get() = _roles

    private val playersRoles = mutableMapOf<Player, Role>()

    private val _narratorList = MutableLiveData(emptyList<NarratorItem>())
    val narratorList: LiveData<List<NarratorItem>>
        get() = _narratorList

    private fun getPlayers() = players.value.orEmpty()

    private fun setPlayers(newPlayers: List<Player>) {
        val players = newPlayers.toMutableList()
        players.sortBy { it.name }
        players.sortBy { !it.isChecked }
        _players.value = players
        selectedPlayers.clear()
        selectedPlayers.addAll(
            players.filter { it.isChecked }
        )
    }

    fun addPlayer(name: String) {
        val currentPlayer = getPlayers().toMutableList()
        val lastIndex = currentPlayer.lastIndex
        currentPlayer.add(Player(id = lastIndex + 1, name = name))
        setPlayers(currentPlayer)
    }

    fun updatePlayer(updatedPlayer: Player) {
        val players = getPlayers().toMutableList()
        val playerIndex = players.indexOfFirst { it.id == updatedPlayer.id }
        if (playerIndex != -1) {
            players[playerIndex] = updatedPlayer
            setPlayers(players)
        }
    }

    fun removePlayer(playerId: Int) {
        val players = getPlayers().toMutableList()
        players.removeAll { it.id == playerId }
        setPlayers(players)
    }

    fun selectAllPlayer() {
        val players = getPlayers().toMutableList()
        players.forEach { player ->
            player.isChecked = true
        }
        setPlayers(players)
    }

    fun isPlayersOk(): Boolean {
        val players = getPlayers()
        val checkedPlayers = players.filter { it.isChecked }
        @Suppress("MagicNumber")
        return checkedPlayers.size >= 3
    }

    fun setSelectedRoles(roles: List<Role>) {
        selectedRoles.clear()
        selectedRoles.addAll(roles)
        _selectedRolesSize.value = roles.size
    }

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

    private fun calculateCitizenSize(): Int = playersSize - mafiaSize.value!! - getIndependentSize()

    private fun getIndependentSize(): Int =
        selectedRoles.filter { getSide(it.name) == R.string.independent_side }.size

    fun checkSelectedRolesIsOk(): ResultType<Boolean, MafiaError> {
        val roles = selectedRoles
        val mafiaSize = roles.filter { getSide(it.name) == R.string.mafia_side }.size
        val isMafiaRoleOk = mafiaSize <= calculateMaxMafia()

        val isRoleSizeOk = roles.size <= playersSize

        return when {
            isMafiaRoleOk && isRoleSizeOk -> ResultType.success(true)
            isMafiaRoleOk && !isRoleSizeOk -> ResultType.error(MafiaError.SelectedRoleTooMuch)
            else -> ResultType.error(MafiaError.MafiaRoleTooMatch)
        }
    }

    private fun generateRoles(): List<Role> {
        val roles = selectedRoles.toMutableList()

        var mafiaSizeInRoles = roles
            .filter { getSide(it.name) == R.string.mafia_side }
            .size
        while (mafiaSizeInRoles < mafiaSize.value!!) {
            roles.add(Role(name = R.string.simple_mafia))
            mafiaSizeInRoles += 1
        }

        var citizenSizeInRoles = roles
            .filter { getSide(it.name) == R.string.citizen_side }
            .size
        while (citizenSizeInRoles < citizenSize.value!!) {
            roles.add(Role(name = R.string.simple_citizen))
            citizenSizeInRoles += 1
        }

        roles.sortBy { it.name }
        return roles
    }

    fun setMafiaSize(newMafiaSize: Int) {
        _mafiaSize.value = newMafiaSize
        _citizenSize.value = calculateCitizenSize()
        _roles.value = generateRoles()
    }

    fun refreshRoles() {
        val currentRoles = roles.value.orEmpty()
        var nextRoles: List<Role>
        do {
            nextRoles = currentRoles.shuffled()
        } while (nextRoles == currentRoles)
        _roles.value = nextRoles
        playersRoles.clear()
    }

    fun getRole(player: Player): Role {
        val index = selectedPlayers.indexOf(player)
        val roles = roles.value.orEmpty()
        val role = roles[index]
        playersRoles[player] = role
        return role
    }

    fun isAllPlayersGetRoles(): Boolean {
        return playersRoles.size == playersSize
    }

    fun createNarratorItems() {
        _narratorList.value = selectedPlayers
            .map { player ->
                NarratorItem(
                    id = player.id,
                    player = player,
                    role = getRole(player),
                    isAlive = true
                )
            }
    }

    private fun setNarratorList(updatedList: List<NarratorItem>) {
        _narratorList.value = updatedList
            .sortedBy { it.player.name }
            .sortedBy { !it.isAlive }
    }

    fun updateNarratorItem(item: NarratorItem) {
        val updatedItems = _narratorList.value.orEmpty().toMutableList()
        val itemIndex = updatedItems.indexOfFirst { it.id == item.id }
        if (itemIndex != -1) {
            updatedItems[itemIndex] = item
            setNarratorList(updatedItems)
        }
    }

    fun loadPlayers(context: Context) {
        val storage = PlayerStorage(context)
        setPlayers(storage.getPlayers())
    }

    fun savePlayers(context: Context) {
        val storage = PlayerStorage(context)
        return storage.savePlayers(getPlayers())
    }
}
