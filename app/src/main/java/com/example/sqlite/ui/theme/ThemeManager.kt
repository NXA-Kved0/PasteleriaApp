package com.example.sqlite.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object ThemeManager {
    val isDarkMode: MutableState<Boolean> = mutableStateOf(false)

    fun toggleTheme() {
        isDarkMode.value = !isDarkMode.value
    }
}
