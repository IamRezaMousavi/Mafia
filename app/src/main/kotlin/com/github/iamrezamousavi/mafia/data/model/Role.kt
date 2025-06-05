package com.github.iamrezamousavi.mafia.data.model

import androidx.annotation.StringRes

enum class RoleSide {
    CITIZEN,
    MAFIA,
    INDEPENDENT
}

data class Role(
    @StringRes
    val name: Int,
    val side: RoleSide
)
