package com.github.iamrezamousavi.mafia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository

class PlayerViewModelFactory(
    private val repository: PlayerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(repository) as T
        }
        return super.create(modelClass)
    }
}