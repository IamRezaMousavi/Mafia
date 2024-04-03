package com.github.iamrezamousavi.mafia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.ResultType
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
    fun calculateMaxMafia_whenPlayerSizeIsOdd() {
        roleViewModel.playersSize = 9
        val maxMafia = roleViewModel.calculateMaxMafia()
        assertEquals(4, maxMafia)
    }

    @Test
    fun calculateMaxMafia_whenPlayerSizeIsEven() {
        roleViewModel.playersSize = 10
        val maxMafia = roleViewModel.calculateMaxMafia()
        assertEquals(4, maxMafia)
    }

    @Test
    fun calculateMinMafia_withMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen)
            )
        )
        val minMafia = roleViewModel.calculateMinMafia()
        assertEquals(2, minMafia)
    }

    @Test
    fun calculateMinMafia_withoutMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_citizen),
                Role(name = R.string.unknown)
            )
        )
        val minMafia = roleViewModel.calculateMinMafia()
        assertEquals(1, minMafia)
    }

    @Test
    fun setMafiaSize_minValue() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor)
            )
        )
        roleViewModel.setMafiaSize(2)
        assertEquals(2, roleViewModel.mafiaSize.value)
        assertEquals(8, roleViewModel.citizenSize.value)
        assertEquals(10, roleViewModel.generatedRoles.value?.size)
    }

    @Test
    fun setMafiaSize_moreThanMin() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor)
            )
        )
        roleViewModel.setMafiaSize(3)
        assertEquals(3, roleViewModel.mafiaSize.value)
        assertEquals(7, roleViewModel.citizenSize.value)
        assertEquals(10, roleViewModel.generatedRoles.value?.size)
    }

    @Test
    fun setMafiaSize_maxValue() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor)
            )
        )
        roleViewModel.setMafiaSize(4)
        assertEquals(4, roleViewModel.mafiaSize.value)
        assertEquals(6, roleViewModel.citizenSize.value)
        assertEquals(10, roleViewModel.generatedRoles.value?.size)
    }

    @Test
    fun checkSelectedRolesIsOk_whenOk() {
        val result = roleViewModel.checkSelectedRolesIsOk(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.silencer),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective),
                Role(name = R.string.sniper),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        assertTrue(result.isSuccess)
    }

    @Test
    fun checkSelectedRolesIsOk_whenNotOk() {
        val result = roleViewModel.checkSelectedRolesIsOk(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.silencer),
                Role(name = R.string.bomber),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.sniper),
                Role(name = R.string.professional),
                Role(name = R.string.unknown)
            )
        )
        assertFalse(result.isSuccess)
        assertEquals(ResultType.error(MafiaError.MafiaRoleTooMatch), result)
    }

    @Test
    fun setSelectedRoles_whenRolesIsOk() {
        val roles = arrayListOf(
            Role(name = R.string.simple_mafia),
            Role(name = R.string.godfather),
            Role(name = R.string.dr_lecter),
            Role(name = R.string.silencer),
            Role(name = R.string.simple_citizen),
            Role(name = R.string.doctor),
            Role(name = R.string.detective),
            Role(name = R.string.sniper),
            Role(name = R.string.professional),
            Role(name = R.string.unknown)
        )
        val result = roleViewModel.setSelectedRoles(roles)
        assertTrue(result.isSuccess)
        assertEquals(roles, roleViewModel.selectedRoles)
        assertEquals(5, roleViewModel.citizenSize.value)
        assertEquals(4, roleViewModel.mafiaSize.value)
    }

    @Test
    fun setSelectedRoles_whenRolesIsWrong() {
        val roles = arrayListOf(
            Role(name = R.string.simple_mafia),
            Role(name = R.string.godfather),
            Role(name = R.string.dr_lecter),
            Role(name = R.string.silencer),
            Role(name = R.string.bomber),
            Role(name = R.string.simple_citizen),
            Role(name = R.string.doctor),
            Role(name = R.string.sniper),
            Role(name = R.string.professional),
            Role(name = R.string.unknown)
        )
        val result = roleViewModel.setSelectedRoles(roles)
        assertFalse(result.isSuccess)
        assertEquals(ResultType.error(MafiaError.MafiaRoleTooMatch), result)

        assertEquals(roles, roleViewModel.selectedRoles)
        assertEquals(10, roleViewModel.selectedRolesSize.value)

        // must be the default values
        assertEquals(1, roleViewModel.citizenSize.value)
        assertEquals(1, roleViewModel.mafiaSize.value)
    }

    @Test
    fun setSelectedRoles_whenSizeIsOver_rolesIsWrong() {
        val roles = arrayListOf(
            Role(name = R.string.simple_mafia),
            Role(name = R.string.godfather),
            Role(name = R.string.dr_lecter),
            Role(name = R.string.silencer),
            Role(name = R.string.simple_citizen),
            Role(name = R.string.doctor),
            Role(name = R.string.sniper),
            Role(name = R.string.professional),
            Role(name = R.string.detective),
            Role(name = R.string.mayor),
            Role(name = R.string.unknown)
        )
        val result = roleViewModel.setSelectedRoles(roles)
        assertFalse(result.isSuccess)
        assertEquals(ResultType.error(MafiaError.SelectedRoleTooMuch), result)

        assertEquals(roles, roleViewModel.selectedRoles)
        assertEquals(11, roleViewModel.selectedRolesSize.value)

        // must be the default values
        assertEquals(1, roleViewModel.citizenSize.value)
        assertEquals(1, roleViewModel.mafiaSize.value)
    }
}
