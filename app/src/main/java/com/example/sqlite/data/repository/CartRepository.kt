package com.example.sqlite.data.repository

import com.example.sqlite.data.local.CartDao
import com.example.sqlite.data.local.CartItem
import com.example.sqlite.data.local.CartWithProductDetails
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    fun getCartItemsWithProducts(userId: Int): Flow<List<CartWithProductDetails>> {
        return cartDao.getCartItemsWithProducts(userId)
    }

    fun getCartItems(userId: Int): Flow<List<CartItem>> {
        return cartDao.getCartItems(userId)
    }

    fun getCartItemCount(userId: Int): Flow<Int?> {
        return cartDao.getCartItemCount(userId)
    }

    //Conteo por userId
    fun getTotalCartItemCount(userId: Int): Flow<Int> {
        return cartDao.getTotalCartItemCount(userId)
    }

    suspend fun addToCart(userId: Int, productId: Int) {
        val existingItem = cartDao.getCartItem(userId, productId)
        if (existingItem != null) {
            cartDao.updateCartItem(existingItem.copy(quantity = existingItem.quantity + 1))
        } else {
            cartDao.insertCartItem(CartItem(userId = userId, productId = productId, quantity = 1))
        }
    }

    suspend fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        if (newQuantity > 0) {
            cartDao.updateCartItem(cartItem.copy(quantity = newQuantity))
        } else {
            cartDao.deleteCartItem(cartItem)
        }
    }

    suspend fun removeFromCart(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun clearCart(userId: Int) {
        cartDao.clearCart(userId)
    }
}