package com.github.iamrezamousavi.mafia

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iamrezamousavi.mafia.data.local.getPlayers
import com.github.iamrezamousavi.mafia.data.local.preferences
import com.github.iamrezamousavi.mafia.data.local.savePlayers
import com.github.iamrezamousavi.mafia.data.model.NarratorItem
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.data.model.RoleSide
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.ResultType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _players = MutableLiveData<List<Player>>(mutableListOf())
    val players: LiveData<List<Player>>
        get() = _players

    val selectedPlayers = ArrayList<Player>()
    val playersCount: Int
        get() = selectedPlayers.size

    val selectedRoles = ArrayList<Role>()

    private val _selectedRolesCount = MutableLiveData(selectedRoles.size)
    val selectedRolesCount: LiveData<Int>
        get() = _selectedRolesCount

    private val _citizenCount = MutableLiveData(1)
    val citizenCount: LiveData<Int>
        get() = _citizenCount

    private val _mafiaCount = MutableLiveData(1)
    val mafiaCount: LiveData<Int>
        get() = _mafiaCount

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

    @OptIn(ExperimentalUuidApi::class)
    fun addPlayer(name: String) {
        val currentPlayer = getPlayers().toMutableList()
        currentPlayer.add(Player(name = name))
        setPlayers(currentPlayer)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun updatePlayer(updatedPlayer: Player) {
        val players = getPlayers().toMutableList()
        val playerIndex = players.indexOfFirst { it.id == updatedPlayer.id }
        if (playerIndex != -1) {
            players[playerIndex] = updatedPlayer
            setPlayers(players)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun removePlayer(playerId: Uuid) {
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
        val checkedPlayers = players.count { it.isChecked }
        @Suppress("MagicNumber")
        return checkedPlayers >= 3
    }

    fun setSelectedRoles(roles: List<Role>) {
        selectedRoles.clear()
        selectedRoles.addAll(roles)
        _selectedRolesCount.value = roles.size
    }

    fun calculateMaxMafia(): Int {
        return if (playersCount % 2 == 1) {
            playersCount / 2
        } else {
            playersCount / 2 - 1
        }
    }

    fun calculateMinMafia(): Int {
        val minMafia = selectedRoles.count { it.side == RoleSide.MAFIA }
        return if (minMafia > 1) minMafia else 1
    }

    private fun calculateCitizenCount() =
        playersCount - mafiaCount.value.orDefault(1) - getIndependentCount()

    private fun getIndependentCount() = selectedRoles.count { it.side == RoleSide.INDEPENDENT }

    fun checkSelectedRolesIsOk(): ResultType<Boolean, MafiaError> {
        val roles = selectedRoles
        val mafiaCount = roles.count { it.side == RoleSide.MAFIA }
        val isMafiaRoleOk = mafiaCount <= calculateMaxMafia()

        val isRoleCountOk = roles.size <= playersCount

        return when {
            isMafiaRoleOk && isRoleCountOk -> ResultType.success(true)
            isMafiaRoleOk && !isRoleCountOk -> ResultType.error(MafiaError.SelectedRoleTooMuch)
            else -> ResultType.error(MafiaError.MafiaRoleTooMatch)
        }
    }

    private fun generateRoles(): List<Role> {
        val roles = selectedRoles.toMutableList()

        var mafiaCountInRoles = roles.count { it.side == RoleSide.MAFIA }
        while (mafiaCountInRoles < mafiaCount.value.orDefault(1)) {
            roles.add(Role(name = R.string.simple_mafia, side = RoleSide.MAFIA))
            mafiaCountInRoles += 1
        }

        var citizenCountInRoles = roles.count { it.side == RoleSide.CITIZEN }
        while (citizenCountInRoles < citizenCount.value.orDefault(1)) {
            roles.add(Role(name = R.string.simple_citizen, side = RoleSide.CITIZEN))
            citizenCountInRoles += 1
        }

        roles.sortBy { it.name }
        return roles
    }

    fun setMafiaCount(newMafiaCount: Int) {
        _mafiaCount.value = newMafiaCount
        _citizenCount.value = calculateCitizenCount()
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

    fun isAllPlayersGetRoles() = playersRoles.size == playersCount

    @OptIn(ExperimentalUuidApi::class)
    fun createNarratorItems() {
        _narratorList.value = selectedPlayers
            .map { player ->
                NarratorItem(
                    id = player.id,
                    player = player,
                    role = getRole(player)
                )
            }
    }

    fun refreshNarratorItems() {
        val items = _narratorList.value.orEmpty().toMutableList()
        items.forEach { it.isAlive = true }
        setNarratorList(items)
    }

    fun hideNarratorItemRoles() {
        val items = _narratorList.value.orEmpty().toMutableList()
        items.forEach { it.showRole = false }
        setNarratorList(items)
    }

    private fun setNarratorList(updatedList: List<NarratorItem>) {
        _narratorList.value = updatedList
            .sortedBy { it.player.name }
            .sortedBy { !it.isAlive }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun updateNarratorItem(item: NarratorItem) {
        val updatedItems = _narratorList.value.orEmpty().toMutableList()
        val itemIndex = updatedItems.indexOfFirst { it.id == item.id }
        if (itemIndex != -1) {
            updatedItems[itemIndex] = item
            setNarratorList(updatedItems)
        }
    }

    fun checkWin(): RoleSide? {
        val players = _narratorList.value.orEmpty().filter { it.isAlive }
        val mafiaCount = players.count { it.role.side == RoleSide.MAFIA }
        val independentCount = players.count { it.role.side == RoleSide.INDEPENDENT }
        val citizenCount = players.count { it.role.side == RoleSide.CITIZEN }

        if (mafiaCount + independentCount == 0) {
            return RoleSide.CITIZEN
        }
        if (citizenCount <= mafiaCount && independentCount == 0) {
            return RoleSide.MAFIA
        }
        if (independentCount == 1 && mafiaCount == 0 && citizenCount <= 2) {
            return RoleSide.INDEPENDENT
        }
        if (independentCount == 1 && mafiaCount <= 2 && citizenCount == 0) {
            return RoleSide.INDEPENDENT
        }
        return null
    }

    fun loadPlayers(context: Context) {
        viewModelScope.launch {
            val players = context.preferences.getPlayers()
            setPlayers(players)
        }
    }

    fun savePlayers(context: Context) {
        viewModelScope.launch {
            val players = getPlayers()
            context.preferences.savePlayers(players)
        }
    }
}

fun Int?.orDefault(default: Int) = this ?: default
