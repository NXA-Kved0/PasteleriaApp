package com.example.sqlite.ui.cart

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share  // ✅ Agregar este import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext  // ✅ Agregar este import
import androidx.compose.ui.unit.dp
import com.example.sqlite.data.local.CartWithProductDetails
import com.example.sqlite.utils.ShareUtils  // ✅ Agregar este import
import com.example.sqlite.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onNavigateBack: () -> Unit
) {
    val cartItemsWithProducts by viewModel.cartItemsWithProducts.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val context = LocalContext.current  //Obtener contexto

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                //Botón de compartir en la barra superior
                actions = {
                    if (cartItemsWithProducts.isNotEmpty()) {
                        IconButton(onClick = {
                            shareCart(context, cartItemsWithProducts, totalPrice)
                        }) {
                            Icon(Icons.Default.Share, "Compartir carrito")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (cartItemsWithProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("El carrito está vacío")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItemsWithProducts) { item ->
                        CartItemCard(
                            item = item,
                            onUpdateQuantity = { newQuantity ->
                                viewModel.updateQuantity(item.cartItem, newQuantity)
                            },
                            onRemove = { viewModel.removeFromCart(item.cartItem) }
                        )
                    }
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total: $${"%.2f".format(totalPrice)}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Button(onClick = { /* Finalizar compra */ }) {
                        Text("Finalizar Compra")
                    }
                }
            }
        }
    }
}

//Funcion auxiliar para compartir el carrito
private fun shareCart(
    context: Context,
    items: List<CartWithProductDetails>,
    total: Double
) {
    val itemsList = items.map { item ->
        "${item.product.name} x${item.cartItem.quantity} - $${"%.2f".format(item.product.price * item.cartItem.quantity)}"
    }

    ShareUtils.shareCart(context, itemsList, total)
}

@Composable
fun CartItemCard(
    item: CartWithProductDetails,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current  //Agregar contexto

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.product.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Precio: $${"%.2f".format(item.product.price)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Subtotal: $${"%.2f".format(item.cartItem.quantity * item.product.price)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onUpdateQuantity(item.cartItem.quantity - 1) }
                    ) {
                        Text("-", style = MaterialTheme.typography.titleLarge)
                    }

                    Text(
                        text = "${item.cartItem.quantity}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    IconButton(
                        onClick = { onUpdateQuantity(item.cartItem.quantity + 1) }
                    ) {
                        Text("+", style = MaterialTheme.typography.titleLarge)
                    }

                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, "Eliminar")
                    }
                }
            }

            //Boton para compartir producto individual
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = {
                    ShareUtils.shareProduct(
                        context,
                        item.product.name,
                        item.product.price
                    )
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Compartir producto")
            }
        }
    }
}
