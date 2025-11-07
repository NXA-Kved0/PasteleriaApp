package com.example.sqlite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sqlite.data.repository.CartRepository
import com.example.sqlite.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val userId: Int
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

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount

    init {
        loadCartItemCount()
    }

    //Usa userId
    private fun loadCartItemCount() {
        viewModelScope.launch {
            cartRepository.getTotalCartItemCount(userId).collect { count ->
                _cartItemCount.value = count
            }
        }
    }

    //no necesita userId como parámetro
    fun addToCart(productId: Int) {
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