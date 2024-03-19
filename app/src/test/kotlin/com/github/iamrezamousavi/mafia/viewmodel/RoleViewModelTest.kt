package com.github.iamrezamousavi.mafia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RoleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val roleViewModel = RoleViewModel(ArrayList())

    @Test
    fun calculateMaxSimpleMafia_whenHasSimpleMafia_sizeIsEven() {
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

        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor)
            )
        )
        for (i in 6..20 step 2) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), i / 2 - 2)
        }

        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective)
            )
        )
        for (i in 8..20 step 2) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), i / 2 - 3)
        }
    }

    @Test
    fun calculateMaxSimpleMafia_whenHasSimpleMafia_sizeIsOdd() {
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

        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor)
            )
        )
        for (i in 5..21 step 2) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), i / 2 - 1)
        }

        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective)
            )
        )
        for (i in 7..20 step 2) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), i / 2 - 2)
        }
    }

    @Test
    fun calculateMaxSimpleMafia_whenHasNotSimpleMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_citizen),
                Role(name = R.string.godfather)
            )
        )
        for (i in 3..20) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), 0)
        }

        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor)
            )
        )
        for (i in 5..20) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), 0)
        }

        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective)
            )
        )
        for (i in 5..20) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateMaxSimpleMafia(), 0)
        }
    }

    @Test
    fun calculateSimpleCitizenCounter_whenHasSimpleCitizenAndMafia() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.simple_citizen)
            )

        )
        for (i in 3..20) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateSimpleCitizenCounter(), i - 1)
        }
        roleViewModel.setSimpleMafiaCounter(3)
        for (i in 7..20) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateSimpleCitizenCounter(), i - 3)
        }

        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.simple_citizen),
                Role(name = R.string.doctor),
                Role(name = R.string.detective)
            )
        )
        for (i in 7..20) {
            roleViewModel.playersSize = i
            assertEquals(roleViewModel.calculateSimpleCitizenCounter(), i - 5)
        }
    }

    @Test
    fun calculateSimpleCitizenCounter_whenHasNotSimpleCitizen() {
        roleViewModel.setSelectedRoles(
            arrayListOf(
                Role(name = R.string.simple_mafia),
                Role(name = R.string.godfather),
                Role(name = R.string.dr_lecter),
                Role(name = R.string.doctor),
                Role(name = R.string.detective)
            )
        )

        roleViewModel.playersSize = 
    }
}