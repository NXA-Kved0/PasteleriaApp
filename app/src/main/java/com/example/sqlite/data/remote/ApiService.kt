package com.example.sqlite.data.remote

import retrofit2.http.GET


//Modelo sencillo para api publica
data class RemoteProduct(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String
)

//Interfaz api
interface ApiService {
    @GET("/products")
    suspend fun getRemoteProducts(): List<RemoteProduct>
}