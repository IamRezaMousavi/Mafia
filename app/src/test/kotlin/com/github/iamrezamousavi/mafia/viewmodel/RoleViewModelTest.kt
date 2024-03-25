package com.github.iamrezamousavi.mafia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RoleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val roleViewModel =
        RoleViewModel(ArrayList((1..10).map { Player(name = "Player $it", isChecked = true) }))

    @Test
    fun setSelectedRoles_justSimpleRoles() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_citizen),
                Role(name = R.string.simple_mafia)
            )
        )
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(1, roleViewModel.simpleMafiaCounter.value)
        assertEquals(9, roleViewModel.simpleCitizenCounter.value)
        assertEquals(4, roleViewModel.maxSimpleMafia.value)
        assertEquals(1, roleViewModel.minSimpleMafia.value)
    }

    @Test
    fun setSelectedRoles_justSimpleCitizen() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_citizen),
                Role(name = R.string.godfather)
            )
        )
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(0, roleViewModel.simpleMafiaCounter.value)
        assertEquals(9, roleViewModel.simpleCitizenCounter.value)
        assertEquals(0, roleViewModel.maxSimpleMafia.value)
        assertEquals(0, roleViewModel.minSimpleMafia.value)
    }

    @Test
    fun setSelectedRoles_justSimpleMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.mayor),
                Role(name = R.string.guardian),
                Role(name = R.string.psychologist),
                Role(name = R.string.professional),
                Role(name = R.string.gunman),
                Role(name = R.string.judge),
                Role(name = R.string.simple_mafia)
            )
        )
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(1, roleViewModel.simpleMafiaCounter.value)
        assertEquals(0, roleViewModel.simpleCitizenCounter.value)
        assertEquals(1, roleViewModel.maxSimpleMafia.value)
        assertEquals(1, roleViewModel.minSimpleMafia.value)
    }

    @Test
    fun setSelectedRoles_withoutSimpleRoles() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.mayor),
                Role(name = R.string.guardian),
                Role(name = R.string.psychologist),
                Role(name = R.string.professional),
                Role(name = R.string.gunman),
                Role(name = R.string.judge),
                Role(name = R.string.godfather)
            )
        )
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(0, roleViewModel.simpleMafiaCounter.value)
        assertEquals(0, roleViewModel.simpleCitizenCounter.value)
        assertEquals(0, roleViewModel.maxSimpleMafia.value)
        assertEquals(0, roleViewModel.minSimpleMafia.value)
    }

    @Test
    fun setSelectedRoles_withSomeSimpleCitizenSomeSimpleMafia_errorDontWork() {
        roleViewModel.playersSize = 9
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.silencer),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.unknown)
            )
        )

        // must not effective
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.silencer),
                Role(name = R.string.bomber),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.unknown)
            )
        )

        roleViewModel.generateRoles()
        println(roleViewModel.getSelectedRoles())
        assertEquals(9, roleViewModel.selectedRolesSize.value)
        assertEquals(4, roleViewModel.simpleCitizenCounter.value)
        assertEquals(1, roleViewModel.simpleMafiaCounter.value)
        assertEquals(1, roleViewModel.minSimpleMafia.value)
        assertEquals(1, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }

    @Test
    fun setSimpleMafiaCounter_lessThanMin() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_citizen),
                Role(name = R.string.simple_mafia)
            )
        )

        roleViewModel.setSimpleMafiaCounter(0)
        assertEquals(1, roleViewModel.simpleMafiaCounter.value)
    }

    @Test
    fun setSimpleMafiaCounter_moreThanMax() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_citizen),
                Role(name = R.string.simple_mafia)
            )
        )

        roleViewModel.setSimpleMafiaCounter(6)
        assertEquals(1, roleViewModel.simpleMafiaCounter.value)
    }

    @Test
    fun setSimpleMafiaCounter_correct() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_citizen),
                Role(name = R.string.simple_mafia)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertEquals(2, roleViewModel.simpleMafiaCounter.value)
    }

    @Test
    fun checkRolesIsOk_withNoSimpleMafiaNoSimpleCitizen_correct() {
        roleViewModel.playersSize = 5
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.doctor),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        assertTrue(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withNoSimpleMafiaNoSimpleCitizen_error() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.silencer),
                Role(name = R.string.doctor),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        assertFalse(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withOneSimpleCitizenNoSimpleMafia_correct() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        assertTrue(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withOneSimpleCitizenNoSimpleMafia_error() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.silencer),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.unknown)
            )
        )
        assertFalse(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withSomeSimpleCitizenNoSimpleMafia_correct() {
        roleViewModel.playersSize = 8
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        assertTrue(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withSomeSimpleCitizenNoSimpleMafia_error() {
        roleViewModel.playersSize = 7
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.silencer),
                Role(name = R.string.bomber),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.unknown)
            )
        )
        assertFalse(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withOneSimpleMafiaNoSimpleCitizen_correct() {
        roleViewModel.playersSize = 5
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        assertTrue(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withOneSimpleMafiaNoSimpleCitizen_error() {
        roleViewModel.playersSize = 5
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.doctor),
                Role(name = R.string.unknown)
            )
        )
        assertFalse(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withSomeSimpleMafiaNoSimpleCitizen_correct() {
        roleViewModel.playersSize = 7
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.sniper),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertTrue(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withSomeSimpleMafiaNoSimpleCitizen_error() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertFalse(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withOneSimpleMafiaOneSimpleCitizen_correct() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        assertTrue(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withOneSimpleMafiaOneSimpleCitizen_error() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        assertFalse(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withSomeSimpleMafiaSomeSimpleCitizen_correct() {
        roleViewModel.playersSize = 7
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertTrue(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun checkRolesIsOk_withSomeSimpleMafiaSomeSimpleCitizen_errorDontWork() {
        roleViewModel.playersSize = 7
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertEquals(2, roleViewModel.simpleCitizenCounter.value)
        assertTrue(roleViewModel.checkRolesIsOk())
    }

    @Test
    fun generateRoles_withNoSimpleRoles() {
        roleViewModel.playersSize = 5
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.mayor),
                Role(name = R.string.guardian),
                Role(name = R.string.psychologist),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.generateRoles()
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(0, roleViewModel.simpleCitizenCounter.value)
        assertEquals(0, roleViewModel.simpleMafiaCounter.value)
        assertEquals(0, roleViewModel.minSimpleMafia.value)
        assertEquals(0, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }

    @Test
    fun generateRoles_withOneSimpleMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.mayor),
                Role(name = R.string.guardian),
                Role(name = R.string.psychologist),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.generateRoles()
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(0, roleViewModel.simpleCitizenCounter.value)
        assertEquals(1, roleViewModel.simpleMafiaCounter.value)
        assertEquals(1, roleViewModel.minSimpleMafia.value)
        assertEquals(1, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }

    @Test
    fun generateRoles_withOneSimpleCitizen() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.mayor),
                Role(name = R.string.guardian),
                Role(name = R.string.psychologist),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.generateRoles()
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(1, roleViewModel.simpleCitizenCounter.value)
        assertEquals(0, roleViewModel.simpleMafiaCounter.value)
        assertEquals(0, roleViewModel.minSimpleMafia.value)
        assertEquals(0, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }

    @Test
    fun generateRoles_withSomeSimpleMafias() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.mayor),
                Role(name = R.string.guardian),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        roleViewModel.generateRoles()
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(0, roleViewModel.simpleCitizenCounter.value)
        assertEquals(2, roleViewModel.simpleMafiaCounter.value)
        assertEquals(1, roleViewModel.minSimpleMafia.value)
        assertEquals(2, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }

    @Test
    fun generateRoles_withSomeSimpleCitizens() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.mayor),
                Role(name = R.string.guardian),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.generateRoles()
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(2, roleViewModel.simpleCitizenCounter.value)
        assertEquals(0, roleViewModel.simpleMafiaCounter.value)
        assertEquals(0, roleViewModel.minSimpleMafia.value)
        assertEquals(0, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }

    @Test
    fun generateRoles_withOneSimpleCitizenOneSimpleMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.mayor),
                Role(name = R.string.professional),
                Role(name = R.string.champion),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.generateRoles()
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(1, roleViewModel.simpleCitizenCounter.value)
        assertEquals(1, roleViewModel.simpleMafiaCounter.value)
        assertEquals(1, roleViewModel.minSimpleMafia.value)
        assertEquals(1, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }

    @Test
    fun generateRoles_withSomeSimpleCitizenOneSimpleMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.generateRoles()
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(6, roleViewModel.simpleCitizenCounter.value)
        assertEquals(1, roleViewModel.simpleMafiaCounter.value)
        assertEquals(1, roleViewModel.minSimpleMafia.value)
        assertEquals(3, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }

    @Test
    fun generateRoles_withSomeSimpleCitizenSomeSimpleMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        roleViewModel.generateRoles()
        assertEquals(10, roleViewModel.selectedRolesSize.value)
        assertEquals(5, roleViewModel.simpleCitizenCounter.value)
        assertEquals(2, roleViewModel.simpleMafiaCounter.value)
        assertEquals(1, roleViewModel.minSimpleMafia.value)
        assertEquals(3, roleViewModel.maxSimpleMafia.value)
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_citizen }.size ==
                roleViewModel.simpleCitizenCounter.value
        )
        assertTrue(
            roleViewModel.getSelectedRoles().filter { it.name == R.string.simple_mafia }.size ==
                roleViewModel.simpleMafiaCounter.value
        )
    }
}
