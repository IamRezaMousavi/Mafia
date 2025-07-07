package com.github.iamrezamousavi.mafia

import android.content.Context
import android.content.SharedPreferences
import com.github.iamrezamousavi.mafia.data.local.getPlayers
import com.github.iamrezamousavi.mafia.data.local.preferences
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.PlayerRole
import com.github.iamrezamousavi.mafia.data.model.RoleSide
import com.github.iamrezamousavi.mafia.utils.EXAMPLE_INDEPENDENT
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.ResultType
import com.github.iamrezamousavi.mafia.utils.SIMPLE_CITIZEN
import com.github.iamrezamousavi.mafia.utils.SIMPLE_MAFIA
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = MainViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addPlayer() {
        viewModel.addPlayer("Bob")
        val players = viewModel.players.value.orEmpty()
        assertEquals(1, players.size)
        assertEquals("Bob", players[0].name)
    }

    @Test
    fun updatePlayer() {
        viewModel.addPlayer("Bob")

        val origin = viewModel.players.value?.first()!!
        val updated = origin.copy(isChecked = true)

        viewModel.updatePlayer(updated)
        val updatedPlayer = viewModel.players.value?.first()!!

        assertEquals("Bob", updatedPlayer.name)
    }

    @Test
    fun removePlayer() {
        viewModel.addPlayer("Bob")
        val player = viewModel.players.value?.first()!!
        viewModel.removePlayer(player.id)
        assertTrue(viewModel.players.value.orEmpty().isEmpty())
    }

    @Test
    fun selectAllPlayers() {
        listOf("A", "B", "C").forEach { viewModel.addPlayer(it) }
        viewModel.selectAllPlayer()
        val players = viewModel.players.value.orEmpty()
        assertTrue(players.all { it.isChecked })
    }

    @Test
    fun isPlayerOk_whenLessThan3Players_false() {
        listOf("A", "B", "C").forEach { viewModel.addPlayer(it) }
        assertFalse(viewModel.isPlayersOk())
    }

    @Test
    fun isPlayerOk_when3PlayersOrMore_true() {
        listOf("A", "B", "C").forEach { viewModel.addPlayer(it) }
        viewModel.selectAllPlayer()
        assertTrue(viewModel.isPlayersOk())
    }

    @Test
    fun refreshRoles() = runTest {
        val playerNames = listOf("A", "B", "C")
        playerNames.forEach { viewModel.addPlayer(it) }
        viewModel.selectAllPlayer()

        val roles = listOf(SIMPLE_MAFIA, SIMPLE_CITIZEN, SIMPLE_CITIZEN)
        viewModel.setSelectedRoles(roles)

        viewModel.refreshRoles()
        advanceUntilIdle()

        val assignedRoles = viewModel.playerRoles.value.orEmpty()
        assertEquals(3, assignedRoles.size)
        assertTrue { assignedRoles.all { it.player.name in playerNames } }
    }

    @Test
    fun calculateMaxMafia_whenEvenPlayerCount() {
        repeat(6) { viewModel.addPlayer("P$it") }
        viewModel.selectAllPlayer()
        assertEquals(2, viewModel.calculateMaxMafia())
    }

    @Test
    fun calculateMaxMafia_whenOddPlayerCount() {
        repeat(7) { viewModel.addPlayer("P$it") }
        viewModel.selectAllPlayer()
        assertEquals(3, viewModel.calculateMaxMafia())
    }

    @Test
    fun calculateMinMafia_returnAtLeast1() {
        val roles = listOf(SIMPLE_CITIZEN)
        viewModel.setSelectedRoles(roles)
        assertEquals(1, viewModel.calculateMinMafia())
    }

    @Test
    fun calculateMinMafia_returnMafiaCount() {
        val roles = listOf(
            SIMPLE_CITIZEN,
            SIMPLE_CITIZEN,
            SIMPLE_CITIZEN,
            SIMPLE_MAFIA,
            SIMPLE_MAFIA
        )
        viewModel.setSelectedRoles(roles)
        assertEquals(2, viewModel.calculateMinMafia())
    }

    @Test
    fun checkSelectedRolesIsOk_returnErrorTooManyRoles() {
        repeat(4) { viewModel.addPlayer("P$it") }
        viewModel.selectAllPlayer()

        val roles = listOf(
            SIMPLE_CITIZEN,
            SIMPLE_CITIZEN,
            SIMPLE_CITIZEN,
            SIMPLE_MAFIA,
            SIMPLE_MAFIA
        )
        viewModel.setSelectedRoles(roles)
        val result = viewModel.checkSelectedRolesIsOk()

        assertTrue(result is ResultType.Error)
        assertEquals(MafiaError.SelectedRolesTooMuch, (result as ResultType.Error).error)
    }

    @Test
    fun checkSelectedRolesIsOk_returnErrorMafiaRolesTooMuch() {
        repeat(5) { viewModel.addPlayer("P$it") }
        viewModel.selectAllPlayer()

        val roles = listOf(
            SIMPLE_CITIZEN,
            SIMPLE_CITIZEN,
            SIMPLE_MAFIA,
            SIMPLE_MAFIA,
            SIMPLE_MAFIA
        )
        viewModel.setSelectedRoles(roles)
        val result = viewModel.checkSelectedRolesIsOk()

        assertTrue(result is ResultType.Error)
        assertEquals(MafiaError.MafiaRolesTooMuch, (result as ResultType.Error).error)
    }

    @Test
    fun checkSelectedRolesIsOk_returnSuccess() {
        repeat(5) { viewModel.addPlayer("P$it") }
        viewModel.selectAllPlayer()

        val roles = listOf(
            SIMPLE_CITIZEN,
            SIMPLE_CITIZEN,
            SIMPLE_CITIZEN,
            SIMPLE_MAFIA,
            SIMPLE_MAFIA
        )
        viewModel.setSelectedRoles(roles)
        val result = viewModel.checkSelectedRolesIsOk()

        assertTrue(result is ResultType.Success)
    }

    @Test
    fun checkWin_whenCitizenWin() {
        val playerRoles = listOf(
            Player(name = "P1") to SIMPLE_CITIZEN,
            Player(name = "P2") to SIMPLE_CITIZEN,
            Player(name = "P3") to SIMPLE_CITIZEN,
            Player(name = "P4") to SIMPLE_MAFIA,
            Player(name = "P5") to SIMPLE_MAFIA,
            Player(name = "P6") to EXAMPLE_INDEPENDENT
        ).map { (player, role) ->
            PlayerRole(id = player.id, player = player, role = role).apply {
                isAlive = role != SIMPLE_MAFIA && role != EXAMPLE_INDEPENDENT
            }
        }
        val result = viewModel.checkWin(playerRoles)
        assertEquals(RoleSide.CITIZEN, result)
    }

    @Test
    fun checkWin_whenMafiaWin() {
        val playerRoles = listOf(
            Player(name = "P1") to SIMPLE_CITIZEN,
            Player(name = "P2") to SIMPLE_CITIZEN,
            Player(name = "P3") to SIMPLE_CITIZEN,
            Player(name = "P4") to SIMPLE_MAFIA,
            Player(name = "P5") to SIMPLE_MAFIA,
            Player(name = "P6") to SIMPLE_MAFIA,
            Player(name = "P7") to EXAMPLE_INDEPENDENT
        ).map { (player, role) ->
            PlayerRole(id = player.id, player = player, role = role).apply {
                isAlive = role != EXAMPLE_INDEPENDENT
            }
        }
        val result = viewModel.checkWin(playerRoles)
        assertEquals(RoleSide.MAFIA, result)
    }

    @Test
    fun checkWin_whenJustCitizenAndIndependent_IndependentWin() {
        val playerRoles = listOf(
            Player(name = "P0") to EXAMPLE_INDEPENDENT,
            Player(name = "P1") to SIMPLE_CITIZEN,
            Player(name = "P2") to SIMPLE_CITIZEN
        ).map { (player, role) ->
            PlayerRole(id = player.id, player = player, role = role, isAlive = true)
        }
        val result = viewModel.checkWin(playerRoles)
        assertEquals(RoleSide.INDEPENDENT, result)
    }

    @Test
    fun checkWin_whenJustCitizenAndMafia_IndependentWin() {
        val playerRoles = listOf(
            Player(name = "P0") to EXAMPLE_INDEPENDENT,
            Player(name = "P1") to SIMPLE_MAFIA,
            Player(name = "P2") to SIMPLE_MAFIA
        ).map { (player, role) ->
            PlayerRole(id = player.id, player = player, role = role, isAlive = true)
        }
        val result = viewModel.checkWin(playerRoles)
        assertEquals(RoleSide.INDEPENDENT, result)
    }

    @Test
    fun checkWin_whenCitizenHandShakeWithIndependent_IndependentWin() {
        val playerRoles = listOf(
            Player(name = "P0") to EXAMPLE_INDEPENDENT,
            Player(name = "P1") to SIMPLE_CITIZEN
        ).map { (player, role) ->
            PlayerRole(id = player.id, player = player, role = role, isAlive = true)
        }
        val result = viewModel.checkWin(playerRoles)
        assertEquals(RoleSide.INDEPENDENT, result)
    }

    @Test
    fun checkWin_whenCitizenHandShakeWithMafia_MafiaWin() {
        val playerRoles = listOf(
            Player(name = "P0") to SIMPLE_MAFIA,
            Player(name = "P1") to SIMPLE_CITIZEN
        ).map { (player, role) ->
            PlayerRole(id = player.id, player = player, role = role, isAlive = true)
        }
        val result = viewModel.checkWin(playerRoles)
        assertEquals(RoleSide.MAFIA, result)
    }

    @Test
    fun loadPlayers() = runTest {
        val context = mockk<Context>()
        val prefs = mockk<SharedPreferences>()

        val players = listOf(Player(name = "Bob"), Player(name = "John"))
        coEvery { prefs.getPlayers() } returns players
        every { context.preferences } returns prefs

        viewModel.loadPlayers(context)
        advanceUntilIdle()

        assertEquals(2, viewModel.players.value?.size)
        assertEquals("Bob", viewModel.players.value?.first()?.name)
    }
}
