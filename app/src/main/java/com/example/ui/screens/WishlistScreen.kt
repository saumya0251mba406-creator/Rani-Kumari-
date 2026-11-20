package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.WarmIvory

@Composable
fun WishlistScreen(
  wishlistProducts: List<Product>,
  onToggleWishlist: (String) -> Unit,
  onViewProduct: (String) -> Unit,
  onExploreCollection: () -> Unit,
  onNavigateFooter: (String) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().widthIn(max = 1040.dp).padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Your Wishlist (${wishlistProducts.size})",
        style = MaterialTheme.typography.headlineLarge,
        color = CharcoalText,
        modifier = Modifier.semantics { heading() },
      )
      Text(
        text = "Saved pieces stay available on this device without signing in.",
        style = MaterialTheme.typography.bodyMedium,
        color = MutedCharcoalText,
      )

      if (wishlistProducts.isEmpty()) {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.fillMaxWidth().testTag("wishlist_empty_state"),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Icon(
              imageVector = Icons.Outlined.FavoriteBorder,
              contentDescription = null,
              tint = DeepMaroon,
            )
            Text(
              text = "Your wishlist is empty.",
              style = MaterialTheme.typography.titleLarge,
              color = CharcoalText,
            )
            Text(
              text = "Tap the heart icon on any regional piece to save it here for comparison.",
              style = MaterialTheme.typography.bodyMedium,
              color = MutedCharcoalText,
            )
            Button(
              onClick = onExploreCollection,
              shape = RoundedCornerShape(12.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = DeepMaroon,
                  contentColor = WarmIvory,
                ),
              modifier = Modifier.minimumInteractiveComponentSize().testTag("wishlist_explore_cta"),
            ) {
              Text("Explore Collection")
            }
          }
        }
      } else {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
          val columns = if (maxWidth >= 820.dp) 3 else if (maxWidth >= 560.dp) 2 else 1
          val rows = wishlistProducts.chunked(columns)
          Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            rows.forEach { rowItems ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
              ) {
                rowItems.forEach { product ->
                  ProductCard(
                    product = product,
                    isWishlisted = true,
                    onToggleWishlist = { onToggleWishlist(product.id) },
                    onViewDetails = { onViewProduct(product.id) },
                    modifier = Modifier.weight(1f),
                  )
                }
                repeat(columns - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
              }
            }
          }
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }
}
