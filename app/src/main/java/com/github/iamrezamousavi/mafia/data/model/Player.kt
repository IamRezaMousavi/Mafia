package com.github.iamrezamousavi.mafia.data.model

data class Player(
    val id: Int,
    val name: String,
    var isChecked: Boolean = false,
)
