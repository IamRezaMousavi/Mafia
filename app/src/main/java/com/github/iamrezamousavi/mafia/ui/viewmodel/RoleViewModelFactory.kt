package com.github.iamrezamousavi.mafia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RoleViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoleViewModel::class.java)) {
            return RoleViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}