package com.github.iamrezamousavi.mafia.data.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class Player
@OptIn(ExperimentalUuidApi::class)
constructor(
    val id: String = Uuid.random().toString(),
    val name: String,
    val isChecked: Boolean = false
)
