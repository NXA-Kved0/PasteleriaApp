package com.example.sqlite.ui.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sqlite.data.local.User
import com.example.sqlite.viewmodel.UserViewModel
@Composable
fun UserScreen(viewModel: UserViewModel, modifier: Modifier = Modifier) {
    val users by viewModel.users.collectAsState(initial = emptyList())
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
// Estados para el modal de edición
    var showEditDialog by remember { mutableStateOf(false) }
    var userToEdit by remember { mutableStateOf<User?>(null) }
    var newName by remember { mutableStateOf("") }
    var newAge by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Usuarios", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
// Formulario de agregar usuario
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Edad") },
                modifier = Modifier.width(80.dp)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (name.isNotBlank() && age.isNotBlank()) {
                    viewModel.addUser(name, age.toInt())
                    name = ""
                    age = ""
                }
            }) {
                Text("Agregar")
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(users) { user ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${user.name} (${user.age})")
                    Row {
                        IconButton(onClick = {
                            userToEdit = user
                            newName = user.name
                            newAge = user.age.toString()
                            showEditDialog = true
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { viewModel.deleteUser(user) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
// Modal de edición
    if (showEditDialog && userToEdit != null) {
        AlertDialog(onDismissRequest = { showEditDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    userToEdit?.let {
                        viewModel.updateUser(it, newName, newAge.toIntOrNull() ?: it.age)
                    }
                    showEditDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Editar usuario") },
            text = {
                Column {
                    TextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nombre") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = newAge,
                        onValueChange = { newAge = it },
                        label = { Text("Edad") },
                        singleLine = true
                    )
                }
            }
        )
    }
}