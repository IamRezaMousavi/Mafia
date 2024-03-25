package com.github.iamrezamousavi.mafia.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.model.Role
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test

class SharedDataTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val sharedData = SharedData

    @Test
    fun test_set_player() {
        val players = arrayListOf(
            Player(name = "Reza"),
            Player(name = "Ali"),
            Player(name = "Alice"),
            Player(name = "Bob"),
            Player(name = "Charlie")
        )
        sharedData.setPlayers(players)
        assertEquals(sharedData.getPlayers(), players)
    }

    @Test
    fun test_set_roles() {
        val roles = arrayListOf(
            Role(name = R.string.simple_citizen),
            Role(name = R.string.doctor),
            Role(name = R.string.detective),
            Role(name = R.string.simple_mafia),
            Role(name = R.string.godfather)
        )
        sharedData.setRoles(roles)
        assertEquals(sharedData.getRoles(), roles)
    }

    @Test
    fun test_shuffled() {
        val roles = arrayListOf(
            Role(name = R.string.simple_citizen),
            Role(name = R.string.doctor),
            Role(name = R.string.detective),
            Role(name = R.string.simple_mafia),
            Role(name = R.string.godfather)
        )
        sharedData.setRoles(roles)

        sharedData.shuffled()

        assertNotEquals(sharedData.getRoles(), roles)
    }

    @Test
    fun test_get_role() {
        val players = arrayListOf(
            Player(name = "Reza"),
            Player(name = "Ali"),
            Player(name = "Alice"),
            Player(name = "Bob"),
            Player(name = "Charlie")
        )
        sharedData.setPlayers(players)
        val roles = arrayListOf(
            Role(name = R.string.simple_citizen),
            Role(name = R.string.doctor),
            Role(name = R.string.detective),
            Role(name = R.string.simple_mafia),
            Role(name = R.string.godfather)
        )
        sharedData.setRoles(roles)

        val player = players[1]
        val role = sharedData.getRole(player)
        val expectedRole = roles[1]
        assertEquals(role, expectedRole)
    }
}
