package com.example.sqlite.ui.cart

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sqlite.data.local.CartWithProductDetails
import com.example.sqlite.utils.ShareUtils
import com.example.sqlite.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onNavigateBack: () -> Unit
) {
    val cartItemsWithProducts by viewModel.cartItemsWithProducts.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("🛒 Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (cartItemsWithProducts.isNotEmpty()) {
                        IconButton(onClick = {
                            shareCart(context, cartItemsWithProducts, totalPrice)
                        }) {
                            Icon(Icons.Default.Share, "Compartir carrito")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (cartItemsWithProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "🛒",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = "El carrito está vacío",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Button(onClick = onNavigateBack) {
                            Text("Ir al catálogo")
                        }
                    }
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

                // Panel inferior con total y botones
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$${"%.2f".format(totalPrice)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    shareCart(context, cartItemsWithProducts, totalPrice)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Compartir")
                            }

                            Button(
                                onClick = {
                                    finalizarCompra(context, cartItemsWithProducts, totalPrice)
                                    kotlinx.coroutines.GlobalScope.launch {
                                        snackbarHostState.showSnackbar("✅ Pedido enviado exitosamente")
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Finalizar Compra")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Función auxiliar para compartir el carrito
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

private fun finalizarCompra(
    context: Context,
    items: List<CartWithProductDetails>,
    total: Double
) {
    val numeroBoleta = (10000..99999).random()
    val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

    val boleta = buildString {
        appendLine("🧁 MIL SABORES")
        appendLine("Pastelería Artesanal")
        appendLine("=" .repeat(40))
        appendLine()
        appendLine("BOLETA ELECTRÓNICA")
        appendLine("Nº $numeroBoleta")
        appendLine("Fecha: $fecha")
        appendLine()
        appendLine("-".repeat(40))
        appendLine()

        items.forEach { item ->
            val nombreCorto = if (item.product.name.length > 25) {
                item.product.name.substring(0, 22) + "..."
            } else {
                item.product.name
            }

            appendLine(nombreCorto)
            appendLine("  ${item.cartItem.quantity} x $${"%.2f".format(item.product.price)} = $${"%.2f".format(item.product.price * item.cartItem.quantity)}")
            appendLine()
        }

        appendLine("-".repeat(40))
        appendLine()
        appendLine("SUBTOTAL:        $${"%.2f".format(total)}")
        appendLine("IVA (19%):       $${"%.2f".format(total * 0.19)}")
        appendLine()
        appendLine("TOTAL A PAGAR:   $${"%.2f".format(total * 1.19)}")
        appendLine()
        appendLine("=" .repeat(40))
        appendLine()
        appendLine("✅ PEDIDO CONFIRMADO")
        appendLine()
        appendLine("📱 Contacto: +56 9 7592 4790")
        appendLine("📧 Email: ventas@milsabores.cl")
        appendLine("🌐 www.milsabores.cl")
        appendLine()
        appendLine("¡Gracias por tu compra!")
        appendLine("Tiempo estimado de entrega: 48-72 hrs")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Boleta #$numeroBoleta - Mil Sabores")
        putExtra(Intent.EXTRA_TEXT, boleta)
    }
    context.startActivity(Intent.createChooser(intent, "Enviar boleta"))
}

@Composable
fun CartItemCard(
    item: CartWithProductDetails,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Precio: $${"%.2f".format(item.product.price)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Subtotal: $${"%.2f".format(item.cartItem.quantity * item.product.price)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onUpdateQuantity(item.cartItem.quantity - 1) },
                        enabled = item.cartItem.quantity > 1
                    ) {
                        Text("-", style = MaterialTheme.typography.titleLarge)
                    }

                    Text(
                        text = "${item.cartItem.quantity}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { onUpdateQuantity(item.cartItem.quantity + 1) }
                    ) {
                        Text("+", style = MaterialTheme.typography.titleLarge)
                    }

                    IconButton(onClick = onRemove) {
                        Icon(
                            Icons.Default.Delete,
                            "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

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