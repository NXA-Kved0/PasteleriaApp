package com.example.sqlite.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val price: Double,
    val imageUrl: String = "",
    val category: String = "Pasteles",
    val stock: Int = 0,
    val available: Boolean = true
)