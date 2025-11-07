package com.example.sqlite.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sqlite.data.local.CartItem

@Composable
fun CartItemCard(
    cartItem: CartItem,
    productName: String = "Producto",
    productPrice: Double = 0.0,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información del producto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = productName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${String.format("%,.0f", productPrice)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Subtotal: $${String.format("%,.0f", productPrice * cartItem.quantity)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Controles de cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón disminuir
                IconButton(
                    onClick = {
                        if (cartItem.quantity > 1) {
                            onUpdateQuantity(cartItem.quantity - 1)
                        }
                    },
                    enabled = cartItem.quantity > 1
                ) {
                    Text(
                        text = "−",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                // Cantidad
                Text(
                    text = "${cartItem.quantity}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.widthIn(min = 32.dp)
                )

                // Botón aumentar
                IconButton(
                    onClick = { onUpdateQuantity(cartItem.quantity + 1) }
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                // Botón eliminar
                IconButton(
                    onClick = onRemove,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar"
                    )
                }
            }
        }
    }
}