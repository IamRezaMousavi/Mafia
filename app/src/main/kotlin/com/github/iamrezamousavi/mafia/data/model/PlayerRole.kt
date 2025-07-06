package com.github.iamrezamousavi.mafia.data.model

data class PlayerRole(
    val id: String,
    val player: Player,
    val role: Role,
    var isAlive: Boolean = true,
    var showRole: Boolean = false
)
