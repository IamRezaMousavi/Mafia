package com.github.iamrezamousavi.mafia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RoleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val roleViewModel = RoleViewModel(ArrayList())


    @Test
    fun calculateSelectedRolesSize_withNoSimpleRoles_matchCase() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSelectedRolesSize(), 6)
    }

    @Test
    fun calculateSelectedRolesSize_withNoSimpleRoles_noMatchCase() {
        roleViewModel.playersSize = 8
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSelectedRolesSize(), 6)
    }

    @Test
    fun calculateSelectedRolesSize_withSomeSimpleMafias_matchCase() {
        roleViewModel.playersSize = 8
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.professional),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertEquals(roleViewModel.calculateSelectedRolesSize(), 8)
    }

    @Test
    fun calculateSelectedRolesSize_withSomeSimpleMafias_noMatchCase() {
        roleViewModel.playersSize = 10
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.professional),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertEquals(roleViewModel.calculateSelectedRolesSize(), 8)
    }

    @Test
    fun calculateSelectedRolesSize_withSomeSimpleCitizens() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.doctor),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSelectedRolesSize(), 6)
    }

    @Test
    fun calculateSelectedRolesSize_withOneSimpleMafiaSomeSimpleCitizens() {
        roleViewModel.playersSize = 7
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSelectedRolesSize(), 7)
    }

    @Test
    fun calculateSelectedRolesSize_withSomeSimpleMafiasSomeSimpleCitizens() {
        roleViewModel.playersSize = 9
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_mafia),
                Role(name = R.string.doctor),
                Role(name = R.string.simple_citizen)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertEquals(roleViewModel.calculateSelectedRolesSize(), 9)
    }

    @Test
    fun calculateSimpleCitizenCounter_withZeroSimpleCitizenOneSimpleMafia() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 0)
    }

    @Test
    fun calculateSimpleCitizenCounter_withOneSimpleCitizenOneSimpleMafia() {
        roleViewModel.playersSize = 6
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 1)
    }

    @Test
    fun calculateSimpleCitizenCounter_withSomeSimpleCitizensOneSimpleMafia() {
        roleViewModel.playersSize = 10
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 5)
    }

    @Test
    fun calculateSimpleCitizenCounter_withZeroSimpleCitizen() {
        roleViewModel.playersSize = 5
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 0)
    }

    @Test
    fun calculateSimpleCitizenCounter_withOneSimpleCitizen() {
        roleViewModel.playersSize = 5
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 1)
    }

    @Test
    fun calculateSimpleCitizenCounter_withSomeSimpleCitizens() {
        roleViewModel.playersSize = 10
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.unknown)
            )
        )
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 6)
    }

    @Test
    fun calculateSimpleCitizenCounter_withZeroSimpleCitizenSomeSimpleMafias() {
        roleViewModel.playersSize = 8
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 0)
    }

    @Test
    fun calculateSimpleCitizenCounter_withOneSimpleCitizenSomeSimpleMafias() {
        roleViewModel.playersSize = 9
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 1)
    }

    @Test
    fun calculateSimpleCitizenCounter_withSomeSimpleCitizensSomeSimpleMafias() {
        roleViewModel.playersSize = 10
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.unknown)
            )
        )
        roleViewModel.setSimpleMafiaCounter(2)
        assertEquals(roleViewModel.calculateSimpleCitizenCounter(), 3)
    }

    @Test
    fun calculateMaxSimpleMafia_withSimpleMafia_playersSizeIsEven() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.simple_citizen),
            )
        )
        for (i in 4..20 step 2) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), i / 2 - 1)
        }
    }

    @Test
    fun calculateMaxSimpleMafia_withSimpleMafiaOtherRoles_playersSizeIsEven() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.unknown)
            )
        )
        for (i in 6..20 step 2) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), i / 2 - 2)
        }
    }

    @Test
    fun calculateMaxSimpleMafia_withSimpleMafia_playersSizeIsOdd() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.simple_citizen),
            )
        )
        for (i in 3..21 step 2) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), i / 2)
        }
    }

    @Test
    fun calculateMaxSimpleMafia_withSimpleMafiaOtherRoles_playersSizeIsOdd() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.unknown)
            )
        )
        for (i in 5..21 step 2) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), i / 2 - 1)
        }
    }

    @Test
    fun calculateMaxSimpleMafia_withNoSimpleMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_citizen),
                Role(name = R.string.godfather),
                Role(name = R.string.unknown)
            )
        )
        for (i in 3..20) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), 0)
        }
    }

    @Test
    fun calculateMaxSimpleMafia_withNoSimpleMafiaOtherRoles() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.unknown)
            )
        )
        for (i in 5..20) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), 0)
        }
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
    fun checkRolesIsOk_withSomeSimpleMafiaSomeSimpleCitizen_error() {
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
        assertFalse(roleViewModel.checkRolesIsOk())
    }
}