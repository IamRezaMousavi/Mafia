package com.github.iamrezamousavi.mafia.data.model

data class NarratorUiState(
    val aliveCount: Int = 0,
    val deadCount: Int = 0,
    val aliveCitizen: Int = 0,
    val aliveMafia: Int = 0,
    val aliveIndependent: Int = 0,
    val deadCitizen: Int = 0,
    val deadMafia: Int = 0,
    val deadIndependent: Int = 0,
    val winner: RoleSide? = null,
    val fullList: List<PlayerRole> = emptyList()
)
