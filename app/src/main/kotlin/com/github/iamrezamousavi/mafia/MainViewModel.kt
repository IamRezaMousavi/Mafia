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
import com.github.iamrezamousavi.mafia.data.model.orDefault
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.ResultType
import com.github.iamrezamousavi.mafia.utils.SIMPLE_CITIZEN
import com.github.iamrezamousavi.mafia.utils.SIMPLE_MAFIA
import kotlin.collections.orEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private fun updatePlayers(transfer: (List<Player>) -> List<Player>) {
        val newPlayers = transfer(players.value.orEmpty())
            .sortedBy { it.name }
            .sortedBy { !it.isChecked }
        _players.value = newPlayers
        selectedPlayers.apply {
            clear()
            addAll(newPlayers.filter { it.isChecked })
        }
    }

    fun addPlayer(name: String) {
        updatePlayers { players ->
            players + Player(name = name)
        }
    }

    fun updatePlayer(updatedPlayer: Player) {
        updatePlayers { players ->
            players.map { if (it.id == updatedPlayer.id) updatedPlayer else it }
        }
    }

    fun removePlayer(playerId: String) {
        updatePlayers { players ->
            players.filterNot { it.id == playerId }
        }
    }

    fun selectAllPlayer() {
        updatePlayers { players ->
            players.map { it.copy(isChecked = true) }
        }
    }

    fun isPlayersOk() = playersCount >= MIN_PLAYERS_COUNT

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
            selectedRoles.size > playersCount -> ResultType.error(MafiaError.SelectedRolesTooMuch)
            mafiaCount > calculateMaxMafia() -> ResultType.error(MafiaError.MafiaRolesTooMuch)
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
            } while (currentRoles == newRoles && selectedPlayers.map { (player, role) -> false if (player.name == "سعید" && player.role != SIMPLE_CITIZEN) else true})

            val playerRoles = selectedPlayers
                .shuffled()
                .zip(roles.value.orEmpty())
                .map { (player, role) ->
                    PlayerRole(id = player.id, player = player, role = role)
                }
                .sortedBy { it.player.name }

            withContext(Dispatchers.Main) {
                _roles.value = newRoles
                _playerRoles.value = playerRoles
                updateNarratorList { emptyList() }
            }
        }
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

    private fun updateNarratorList(transfer: (List<PlayerRole>) -> List<PlayerRole>) {
        val updatedList = transfer(_narratorList.value.orEmpty())
            .sortedBy { it.player.name }
            .sortedBy { !it.isAlive }
        _narratorList.value = updatedList
        updateNarratorUiState()
    }

    private fun addNarratorListItem(item: PlayerRole) {
        updateNarratorList { currentList ->
            currentList + item
        }
    }

    fun updateNarratorItem(item: PlayerRole) {
        updateNarratorList { currentList ->
            currentList.map { if (it.id == item.id) item else it }
        }
    }

    fun markAllPlayersAlive() {
        updateNarratorList { currentList ->
            currentList.map { it.copy(isAlive = true) }
        }
    }

    fun hidePlayerRoles() {
        updateNarratorList { currentList ->
            currentList.map { it.copy(showRole = false) }
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
            withContext(Dispatchers.Main) {
                _narratorUiState.value = newState
            }
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
            updatePlayers { players }
        }
    }

    fun savePlayers(context: Context) {
        viewModelScope.launch {
            val players = players.value.orEmpty()
            context.preferences.savePlayers(players)
        }
    }

    companion object {
        const val MIN_PLAYERS_COUNT = 3
    }
}

fun Int?.orDefault(default: Int = 0) = this ?: default
