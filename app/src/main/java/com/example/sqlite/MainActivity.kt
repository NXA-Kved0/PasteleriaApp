package com.example.sqlite
import UserScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.sqlite.ui.theme.SQLITETheme
import androidx.room.Room
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sqlite.data.local.AppDatabase
import com.example.sqlite.data.repository.UserRepository
import com.example.sqlite.viewmodel.UserViewModel
import com.example.sqlite.viewmodel.UserViewModelFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_database"
        ).build()
        val repo = UserRepository(db.userDao())
        val factory = UserViewModelFactory(repo)
        setContent {
            val viewModel: UserViewModel = viewModel(factory = factory)
            SQLITETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserScreen(viewModel,
                        Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}