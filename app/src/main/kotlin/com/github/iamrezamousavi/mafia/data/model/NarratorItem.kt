package com.github.iamrezamousavi.mafia.data.model

data class NarratorItem(
    val id: Int,
    val player: Player,
    val role: Role,
    var isAlive: Boolean = true,
    var showRole: Boolean = true
)
