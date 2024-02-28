package com.github.iamrezamousavi.mafia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlayerRoleViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerRoleViewModel::class.java)) {
            return PlayerRoleViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}