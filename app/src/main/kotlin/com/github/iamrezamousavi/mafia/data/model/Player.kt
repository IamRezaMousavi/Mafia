package com.github.iamrezamousavi.mafia.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Int,
    val name: String,
    var isChecked: Boolean = false
)
