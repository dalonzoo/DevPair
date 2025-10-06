package com.devpair.offline.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpair.offline.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    init {
        // Seed users se necessario
        viewModelScope.launch {
            userRepository.seedUsersIfEmpty()
        }
    }
}
