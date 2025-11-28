package com.example.sqlite

import com.example.sqlite.data.local.Product
import com.example.sqlite.data.remote.RemoteProduct
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductRepositoryMappingTest {

    @Test
    fun `remote product is mapped correctly to local Product`() {
        val remote = RemoteProduct(
            id = 1,
            title = "Torta de chocolate",
            body = "Bizcocho húmedo con cobertura"
        )

        val mapped = Product(
            id = 0,
            name = remote.title,
            description = remote.body,
            price = 10000.0,
            imageUrl = "",
            category = "API",
            stock = 10,
            available = true
        )

        assertEquals("Torta de chocolate", mapped.name)
        assertEquals("Bizcocho húmedo con cobertura", mapped.description)
        assertEquals(10000.0, mapped.price, 0.0)
        assertEquals("API", mapped.category)
        assertEquals(true, mapped.available)
    }
}