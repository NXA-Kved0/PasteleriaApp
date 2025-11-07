package com.example.sqlite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sqlite.data.local.Product
import com.example.sqlite.data.repository.CartRepository
import com.example.sqlite.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    val products = productRepository.allProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Idle)
    val addToCartState: StateFlow<AddToCartState> = _addToCartState

    fun addToCart(userId: Int, productId: Int) {
        viewModelScope.launch {
            _addToCartState.value = AddToCartState.Loading
            try {
                cartRepository.addToCart(userId, productId)
                _addToCartState.value = AddToCartState.Success
            } catch (e: Exception) {
                _addToCartState.value = AddToCartState.Error("Error al agregar: ${e.message}")
            }
        }
    }

    fun filterByCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun resetAddToCartState() {
        _addToCartState.value = AddToCartState.Idle
    }
}

sealed class AddToCartState {
    object Idle : AddToCartState()
    object Loading : AddToCartState()
    object Success : AddToCartState()
    data class Error(val message: String) : AddToCartState()
}