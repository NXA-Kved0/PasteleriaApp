package com.example.sqlite.utils

import android.content.Context
import android.content.Intent

object ShareUtils {

    fun shareProduct(context: Context, productName: String, productPrice: Double) {
        val shareText = """
            ¡Mira este delicioso pastel! 🍰
            
            $productName
            Precio: $${"%.2f".format(productPrice)}
            
            ¡Ordena ahora en nuestra pastelería!
        """.trimIndent()

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        context.startActivity(Intent.createChooser(intent, "Compartir producto"))
    }

    fun shareCart(context: Context, items: List<String>, total: Double) {
        val itemsList = items.joinToString("\n") { "• $it" }

        val shareText = """
            Mi pedido en la Pastelería 🍰
            
            $itemsList
            
            Total: $${"%.2f".format(total)}
            
            ¡Te invito a probar estos deliciosos pasteles!
        """.trimIndent()

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        context.startActivity(Intent.createChooser(intent, "Compartir carrito"))
    }
}
