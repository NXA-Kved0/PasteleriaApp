package com.example.sqlite.data.repository

import com.example.sqlite.data.local.Product
import com.example.sqlite.data.local.ProductDao
import com.example.sqlite.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }

    suspend fun getProductById(productId: Int): Product? {
        return productDao.getProductById(productId)
    }

    suspend fun insert(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun update(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun delete(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun syncProductsFromApi() {
        withContext(Dispatchers.IO) {
            try {
                // Llama a la API definida en RetrofitInstance
                val remoteList = RetrofitInstance.api.getRemoteProducts()

                // Mapear RemoteProduct -> Product (ajusta según tu entity)
                val mapped = remoteList.map { remote ->
                    Product(
                        id = 0,                       // Room autogenera
                        name = remote.title,
                        description = remote.body,
                        price = 10000.0,
                        imageUrl = "",
                        category = "API",
                        stock = 10,
                        available = true
                    )
                }

                // Limpiar e insertar
                productDao.deleteAll()
                productDao.insertProducts(mapped)

            } catch (e: Exception) {
                // ignorar o loguear para la demo
            }
        }
    }
}