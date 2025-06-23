package com.github.iamrezamousavi.mafia.data.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class NarratorItem
@OptIn(ExperimentalUuidApi::class)
constructor(
    val id: Uuid,
    val player: Player,
    val role: Role,
    var isAlive: Boolean = true,
    var showRole: Boolean = true
)
