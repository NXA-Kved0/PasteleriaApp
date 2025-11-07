package com.example.sqlite.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Product::class,
        CartItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pasteleria_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.productDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(productDao: ProductDao) {
            val sampleProducts = listOf(
                Product(
                    name = "Torta de Chocolate",
                    description = "Deliciosa torta de chocolate con crema",
                    price = 15000.0,
                    category = "Tortas",
                    stock = 10,
                    imageUrl = "https://res.cloudinary.com/riqra/image/upload/v1739382926/sellers/tortas-gaby/products/djenpatwiwbywtrzatej.jpg"
                ),
                Product(
                    name = "Cheesecake",
                    description = "Suave cheesecake de frambuesa",
                    price = 12000.0,
                    category = "Tortas",
                    stock = 8,
                    imageUrl = "https://www.paulinacocina.net/wp-content/uploads/2025/01/receta-de-cheesecake-1742898428.jpg"
                ),
                Product(
                    name = "Cupcakes Variados",
                    description = "Pack de 6 cupcakes de diferentes sabores",
                    price = 8000.0,
                    category = "Cupcakes",
                    stock = 20,
                    imageUrl = "https://www.mokacupcakes.com/cdn/shop/products/IMG_8116.jpg?v=1674069616"
                ),
                Product(
                    name = "Torta Naranja",
                    description = "Clásica torta de naranja casera",
                    price = 14000.0,
                    category = "Tortas",
                    stock = 5,
                    imageUrl = "https://hazdeoros.com/familiar/wp-content/uploads/2021/02/torta-casera-de-naranja-con-cubierta-de-chocolate-una-receta-facil-sin-horno.png"
                ),
                Product(
                    name = "Alfajores",
                    description = "Pack de 12 alfajores artesanales",
                    price = 6000.0,
                    category = "Galletas",
                    stock = 30,
                    imageUrl = "https://cdn11.bigcommerce.com/s-3stx4pub31/product_images/uploaded_images/alfajor-negro-relleno-de-dulce-de-leche.jpg"
                ),
                Product(
                    name = "Brownies",
                    description = "Brownies de chocolate con nueces",
                    price = 5000.0,
                    category = "Postres",
                    stock = 15,
                    imageUrl = "https://www.justspices.es/media/recipe/brownie-chocolate.jpg"
                )
            )
            productDao.insertProducts(sampleProducts)
        }
    }
}