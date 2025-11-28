package com.example.sqlite.data.remote

import retrofit2.http.GET


//Modelo sencillo para api publica
data class RemoteProduct(
    val id: Int,
    val title: String,
    val body: String
)

//Interfaz api
interface ApiService {
    @GET("/posts")
    suspend fun getRemoteProducts(): List<RemoteProduct>
}