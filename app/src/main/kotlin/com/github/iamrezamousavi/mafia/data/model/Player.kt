package com.github.iamrezamousavi.mafia.data.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class Player
@OptIn(ExperimentalUuidApi::class)
constructor(
    val id: Uuid = Uuid.random(),
    val name: String,
    var isChecked: Boolean = false
)
