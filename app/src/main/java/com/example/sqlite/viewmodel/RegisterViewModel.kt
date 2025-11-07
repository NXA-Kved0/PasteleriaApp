package com.example.sqlite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sqlite.data.local.User
import com.example.sqlite.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String, name: String, age: Int) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                // Verificar si el email ya existe
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _registerState.value = RegisterState.Error("Este email ya está registrado")
                    return@launch
                }

                // Crear nuevo usuario con edad
                val newUser = User(email = email, password = password, name = name, age = age)
                userRepository.insert(newUser)
                _registerState.value = RegisterState.Success
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
