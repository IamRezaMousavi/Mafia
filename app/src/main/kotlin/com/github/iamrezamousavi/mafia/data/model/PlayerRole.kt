package com.github.iamrezamousavi.mafia.data.model

import com.github.iamrezamousavi.mafia.utils.SIMPLE_CITIZEN

data class PlayerRole(
    val id: String,
    val player: Player,
    val role: Role,
    var isAlive: Boolean = true,
    var showRole: Boolean = false
)

fun PlayerRole?.orDefault() = this ?: PlayerRole(
    id = "",
    player = Player(id = "", name = ""),
    role = SIMPLE_CITIZEN
)
