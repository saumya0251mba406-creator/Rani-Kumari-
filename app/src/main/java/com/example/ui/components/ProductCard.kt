package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.Formatters
import com.example.model.Product
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.DeepMaroonSoft
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.WarmIvory

@Composable
fun ProductCard(
  product: Product,
  isWishlisted: Boolean,
  onToggleWishlist: () -> Unit,
  onViewDetails: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
    border = BorderStroke(1.dp, SoftBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier.fillMaxWidth().clickable(onClick = onViewDetails),
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Box {
        ProductArtwork(
          productName = product.name,
          region = product.region,
          motif = product.motif,
          altText = product.altText,
          accentHex = product.availableColors.firstOrNull()?.swatchHex,
          modifier = Modifier.fillMaxWidth().aspectRatio(1.15f),
          showProductTitleOverlay = true,
        )
        Surface(
          color = WarmIvory.copy(alpha = 0.94f),
          shape = RoundedCornerShape(50),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
        ) {
          IconButton(
            onClick = onToggleWishlist,
            modifier =
              Modifier.minimumInteractiveComponentSize().testTag("wishlist_toggle_${product.id}"),
          ) {
            Icon(
              imageVector =
                if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription =
                if (isWishlisted) "Remove ${product.name} from wishlist"
                else "Save ${product.name} to wishlist",
              tint = if (isWishlisted) DeepMaroon else CharcoalText,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(
          color = DeepMaroonSoft,
          shape = RoundedCornerShape(50),
        ) {
          Text(
            text = product.region.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = DeepMaroon,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
          )
        }
        Text(
          text = product.category.displayName,
          style = MaterialTheme.typography.labelSmall,
          color = MutedCharcoalText,
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = product.name,
        style = MaterialTheme.typography.titleLarge,
        color = CharcoalText,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = product.shortDescription,
        style = MaterialTheme.typography.bodySmall,
        color = MutedCharcoalText,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = Formatters.formatInr(product.priceInr),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DeepMaroon,
          )
          Text(
            text =
              if (product.category.requiresSizeSelection) "Sizes: ${product.availableSizes.joinToString(", ")}"
              else "One Size",
            style = MaterialTheme.typography.labelSmall,
            color = MutedCharcoalText,
          )
        }

        Button(
          onClick = onViewDetails,
          shape = RoundedCornerShape(12.dp),
          colors =
            ButtonDefaults.buttonColors(
              containerColor = DeepMaroon,
              contentColor = WarmIvory,
            ),
          modifier = Modifier.minimumInteractiveComponentSize().testTag("view_details_${product.id}"),
        ) {
          Text(text = "View Details")
        }
      }
    }
  }
}
