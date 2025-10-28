package com.example.sqlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.sqlite.data.local.AppDatabase
import com.example.sqlite.data.repository.UserRepository
import com.example.sqlite.ui.users.UserScreen
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
            MaterialTheme {
                MainContent(factory)
            }
        }
    }
}

@Composable
fun MainContent(factory: UserViewModelFactory) {
    val viewModel: UserViewModel = viewModel(factory = factory)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        UserScreen(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}