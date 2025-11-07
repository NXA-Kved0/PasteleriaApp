package com.example.sqlite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sqlite.data.repository.CartRepository

class CartViewModelFactory(
    private val cartRepository: CartRepository,
    private val userId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(cartRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
