package com.example.sqlite.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: Int): Flow<List<CartItem>>

    @Transaction
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItemsWithProducts(userId: Int): Flow<List<CartWithProductDetails>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun getCartItem(userId: Int, productId: Int): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)

    @Query("SELECT SUM(quantity) FROM cart_items WHERE userId = :userId")
    fun getCartItemCount(userId: Int): Flow<Int?>

    //Conteo por userId
    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE userId = :userId")
    fun getTotalCartItemCount(userId: Int): Flow<Int>
}