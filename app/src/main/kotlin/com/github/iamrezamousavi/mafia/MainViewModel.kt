package com.github.iamrezamousavi.mafia

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iamrezamousavi.mafia.data.local.getPlayers
import com.github.iamrezamousavi.mafia.data.local.preferences
import com.github.iamrezamousavi.mafia.data.local.savePlayers
import com.github.iamrezamousavi.mafia.data.model.NarratorUiState
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.PlayerRole
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.data.model.RoleSide
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.ResultType
import com.github.iamrezamousavi.mafia.utils.SIMPLE_CITIZEN
import com.github.iamrezamousavi.mafia.utils.SIMPLE_MAFIA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _players = MutableLiveData(emptyList<Player>())
    val players: LiveData<List<Player>>
        get() = _players

    val selectedPlayers = mutableListOf<Player>()
    val playersCount: Int
        get() = selectedPlayers.size

    val selectedRoles = mutableListOf<Role>()

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

    private val _playerRoles = MutableLiveData(emptyList<PlayerRole>())
    val playerRoles: LiveData<List<PlayerRole>>
        get() = _playerRoles

    private val _narratorList = MutableLiveData(emptyList<PlayerRole>())
    val narratorList: LiveData<List<PlayerRole>>
        get() = _narratorList

    private val _narratorUiState = MutableLiveData(NarratorUiState())
    val narratorUiState: LiveData<NarratorUiState>
        get() = _narratorUiState

    private fun getPlayers() = players.value.orEmpty()

    private fun setPlayers(newPlayers: List<Player>) {
        _players.value = newPlayers
            .sortedBy { it.name }
            .sortedBy { !it.isChecked }
        selectedPlayers.clear()
        selectedPlayers.addAll(
            newPlayers.filter { it.isChecked }
        )
    }

    fun addPlayer(name: String) {
        val currentPlayer = getPlayers().toMutableList()
        currentPlayer.add(Player(name = name))
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

    fun removePlayer(playerId: String) {
        val players = getPlayers()
            .filter { it.id != playerId }
        setPlayers(players)
    }

    fun selectAllPlayer() {
        val players = getPlayers()
            .map { it.copy(isChecked = true) }
        setPlayers(players)
    }

    fun isPlayersOk(): Boolean {
        val players = getPlayers()
        val checkedPlayers = players.count { it.isChecked }
        return checkedPlayers >= MIN_PLAYERS_COUNT
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
        val mafiaCount = selectedRoles.count { it.side == RoleSide.MAFIA }

        return when {
            selectedRoles.size > playersCount -> ResultType.error(MafiaError.SelectedRoleTooMuch)
            mafiaCount > calculateMaxMafia() -> ResultType.error(MafiaError.MafiaRoleTooMatch)
            else -> ResultType.success(true)
        }
    }

    private fun generateRoles(): List<Role> {
        val roles = selectedRoles.toMutableList()

        val mafiaCountInRoles = roles.count { it.side == RoleSide.MAFIA }
        val targetMafiaCount = mafiaCount.value.orDefault(1)
        repeat(targetMafiaCount - mafiaCountInRoles) {
            roles.add(SIMPLE_MAFIA)
        }

        val citizenCountInRoles = roles.count { it.side == RoleSide.CITIZEN }
        val targetCitizenCount = citizenCount.value.orDefault(1)
        repeat(targetCitizenCount - citizenCountInRoles) {
            roles.add(SIMPLE_CITIZEN)
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
        viewModelScope.launch(Dispatchers.Default) {
            val currentRoles = roles.value.orEmpty()
            val newRoles = currentRoles.toMutableList()
            do {
                newRoles.shuffle()
            } while (currentRoles == newRoles)
            _roles.postValue(newRoles)

            _playerRoles.postValue(
                selectedPlayers
                    .shuffled()
                    .zip(roles.value.orEmpty())
                    .map { (player, role) ->
                        PlayerRole(id = player.id, player = player, role = role)
                    }
                    .sortedBy { it.player.name }
            )
        }
        setNarratorList(emptyList())
    }

    fun getRole(player: Player): Role {
        val playerRole = playerRoles.value.orEmpty()
            .find { it.id == player.id }
            .orDefault()
        addNarratorListItem(playerRole.copy(showRole = true))
        deletePlayerRoleItem(playerRole)
        return playerRole.role
    }

    fun deletePlayerRoleItem(item: PlayerRole) {
        _playerRoles.value = _playerRoles.value.orEmpty().filter { it.id != item.id }
    }

    fun isAllPlayersGetRoles() = narratorList.value.orEmpty().size == playersCount

    fun applyAllPlayerAlive() {
        val items = _narratorList.value.orEmpty()
            .map { it.copy(isAlive = true) }
        setNarratorList(items)
    }

    fun hidePlayerRoles() {
        val items = _narratorList.value.orEmpty()
            .map { it.copy(showRole = false) }
        setNarratorList(items)
    }

    private fun addNarratorListItem(item: PlayerRole) {
        val newList = _narratorList.value.orEmpty() + item
        setNarratorList(newList)
    }

    private fun setNarratorList(updatedList: List<PlayerRole>) {
        _narratorList.value = updatedList
            .sortedBy { it.player.name }
            .sortedBy { !it.isAlive }
        updateNarratorUiState()
    }

    fun updateNarratorItem(item: PlayerRole) {
        val updatedItems = narratorList.value.orEmpty().toMutableList()
        val itemIndex = updatedItems.indexOfFirst { it.id == item.id }
        if (itemIndex != -1) {
            updatedItems[itemIndex] = item
            setNarratorList(updatedItems)
        }
    }

    fun updateNarratorUiState() {
        val currentPlayers = narratorList.value.orEmpty()

        viewModelScope.launch(Dispatchers.Default) {
            val alive = currentPlayers.filter { it.isAlive }
            val dead = currentPlayers.filterNot { it.isAlive }

            val newState = NarratorUiState(
                aliveCount = alive.size,
                deadCount = dead.size,
                aliveCitizen = alive.count { it.role.side == RoleSide.CITIZEN },
                aliveMafia = alive.count { it.role.side == RoleSide.MAFIA },
                aliveIndependent = alive.count { it.role.side == RoleSide.INDEPENDENT },
                deadCitizen = dead.count { it.role.side == RoleSide.CITIZEN },
                deadMafia = dead.count { it.role.side == RoleSide.MAFIA },
                deadIndependent = dead.count { it.role.side == RoleSide.INDEPENDENT },
                winner = checkWin(currentPlayers),
                fullList = currentPlayers
            )
            _narratorUiState.postValue(newState)
        }
    }

    fun checkWin(players: List<PlayerRole>): RoleSide? {
        val alive = players.filter { it.isAlive }

        val mafiaCount = alive.count { it.role.side == RoleSide.MAFIA }
        val independentCount = alive.count { it.role.side == RoleSide.INDEPENDENT }
        val citizenCount = alive.count { it.role.side == RoleSide.CITIZEN }

        return when {
            mafiaCount + independentCount == 0 -> RoleSide.CITIZEN
            citizenCount <= mafiaCount && independentCount == 0 -> RoleSide.MAFIA
            independentCount == 1 && mafiaCount == 0 && citizenCount <= 2 -> RoleSide.INDEPENDENT
            independentCount == 1 && mafiaCount <= 2 && citizenCount == 0 -> RoleSide.INDEPENDENT
            else -> null
        }
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

    companion object {
        const val MIN_PLAYERS_COUNT = 3
    }
}

fun Int?.orDefault(default: Int = 0) = this ?: default

fun PlayerRole?.orDefault(): PlayerRole {
    this?.let { return this }
    val player = Player(name = "")
    return PlayerRole(
        id = player.id,
        player = player,
        role = SIMPLE_CITIZEN
    )
}
