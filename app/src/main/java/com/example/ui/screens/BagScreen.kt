package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Formatters
import com.example.data.ProductCatalog
import com.example.model.CartItem
import com.example.ui.components.ProductArtwork
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.SoftCream
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BagScreen(
  bagItems: List<CartItem>,
  merchandiseSubtotalInr: Int,
  onUpdateQuantity: (String, Int) -> Unit,
  onRemoveItem: (String) -> Unit,
  onViewProduct: (String) -> Unit,
  onContinueShopping: () -> Unit,
  onProceedToEnquiry: () -> Unit,
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
        text = "Shopping Bag (${bagItems.sumOf { it.quantity }})",
        style = MaterialTheme.typography.headlineLarge,
        color = CharcoalText,
        modifier = Modifier.semantics { heading() },
      )

      if (bagItems.isEmpty()) {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.fillMaxWidth().testTag("bag_empty_state"),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Icon(
              imageVector = Icons.Outlined.ShoppingBag,
              contentDescription = null,
              tint = DeepMaroon,
            )
            Text(
              text = "Your shopping bag is empty.",
              style = MaterialTheme.typography.titleLarge,
              color = CharcoalText,
            )
            Text(
              text =
                "Browse the curated collection and select your preferred size and colorway to build an order enquiry.",
              style = MaterialTheme.typography.bodyMedium,
              color = MutedCharcoalText,
            )
            Button(
              onClick = onContinueShopping,
              shape = RoundedCornerShape(12.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = DeepMaroon,
                  contentColor = WarmIvory,
                ),
              modifier = Modifier.minimumInteractiveComponentSize().testTag("bag_explore_cta"),
            ) {
              Text("Explore Collection")
            }
          }
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          bagItems.forEach { item ->
            val product = ProductCatalog.findProductById(item.productId)
            val maxStock = product?.stockFor(item.size, item.color) ?: item.quantity
            val accentHex =
              product?.availableColors?.firstOrNull { it.name == item.color }?.swatchHex

            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
              border = BorderStroke(1.dp, SoftBorder),
              modifier = Modifier.fillMaxWidth().testTag("bag_item_${item.lineId}"),
            ) {
              Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                  verticalAlignment = Alignment.Top,
                ) {
                  if (product != null) {
                    ProductArtwork(
                      productName = product.name,
                      region = product.region,
                      motif = product.motif,
                      altText = product.altText,
                      accentHex = accentHex,
                      showProductTitleOverlay = false,
                      modifier =
                        Modifier.size(76.dp).clickable { onViewProduct(product.id) },
                    )
                  }
                  Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                  ) {
                    Text(
                      text = item.productName,
                      style = MaterialTheme.typography.titleMedium,
                      color = CharcoalText,
                      modifier = Modifier.clickable { onViewProduct(item.productId) },
                    )
                    Text(
                      text = "${item.region.displayName} • Size: ${item.size} • Color: ${item.color}",
                      style = MaterialTheme.typography.bodySmall,
                      color = MutedCharcoalText,
                    )
                    Text(
                      text = "Unit price: ${Formatters.formatInr(item.unitPriceInr)}",
                      style = MaterialTheme.typography.bodySmall,
                      color = CharcoalText,
                    )
                  }
                }

                HorizontalDivider(color = SoftBorder)

                FlowRow(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                      shape = RoundedCornerShape(10.dp),
                      color = SoftCream,
                      border = BorderStroke(1.dp, SoftBorder),
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                          onClick = { onUpdateQuantity(item.lineId, -1) },
                          modifier =
                            Modifier.minimumInteractiveComponentSize()
                              .testTag("bag_qty_minus_${item.lineId}"),
                        ) {
                          Icon(Icons.Outlined.Remove, contentDescription = "Decrease quantity")
                        }
                        Text(
                          text = item.quantity.toString(),
                          style = MaterialTheme.typography.titleMedium,
                          color = CharcoalText,
                          modifier = Modifier.padding(horizontal = 8.dp),
                        )
                        IconButton(
                          onClick = { onUpdateQuantity(item.lineId, 1) },
                          enabled = item.quantity < maxStock,
                          modifier =
                            Modifier.minimumInteractiveComponentSize()
                              .testTag("bag_qty_plus_${item.lineId}"),
                        ) {
                          Icon(Icons.Outlined.Add, contentDescription = "Increase quantity")
                        }
                      }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "Max $maxStock",
                      style = MaterialTheme.typography.labelSmall,
                      color = MutedCharcoalText,
                    )
                  }

                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                  ) {
                    Text(
                      text = Formatters.formatInr(item.lineTotalInr),
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.Bold,
                      color = DeepMaroon,
                    )
                    TextButton(
                      onClick = { onRemoveItem(item.lineId) },
                      modifier = Modifier.testTag("bag_remove_${item.lineId}"),
                    ) {
                      Icon(
                        Icons.Outlined.DeleteOutline,
                        contentDescription = null,
                        tint = ErrorCrimson,
                        modifier = Modifier.size(18.dp),
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Remove", color = ErrorCrimson)
                    }
                  }
                }
              }
            }
          }
        }

        // Order Summary Card
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = SoftCream),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.fillMaxWidth(),
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Text(
              text = "Summary",
              style = MaterialTheme.typography.titleLarge,
              color = CharcoalText,
            )
            SummaryRow(
              label = "Merchandise subtotal",
              value = Formatters.formatInr(merchandiseSubtotalInr),
            )
            SummaryRow(label = "Shipping", value = "To be confirmed")
            HorizontalDivider(color = SoftBorder)
            SummaryRow(
              label = "Estimated merchandise total",
              value = Formatters.formatInr(merchandiseSubtotalInr),
              emphasize = true,
            )
            Text(
              text =
                "Shipping charges, availability, and final payable amount are confirmed before payment.",
              style = MaterialTheme.typography.bodySmall,
              color = MutedCharcoalText,
            )

            FlowRow(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              OutlinedButton(
                onClick = onContinueShopping,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DeepMaroon),
                modifier = Modifier.minimumInteractiveComponentSize(),
              ) {
                Text("Continue Shopping", color = DeepMaroon)
              }
              Button(
                onClick = onProceedToEnquiry,
                shape = RoundedCornerShape(12.dp),
                colors =
                  ButtonDefaults.buttonColors(
                    containerColor = DeepMaroon,
                    contentColor = WarmIvory,
                  ),
                modifier = Modifier.minimumInteractiveComponentSize().testTag("proceed_to_enquiry"),
              ) {
                Text("Proceed to Enquiry")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  Icons.AutoMirrored.Outlined.ArrowForward,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                )
              }
            }
          }
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }
}

@Composable
fun SummaryRow(
  label: String,
  value: String,
  emphasize: Boolean = false,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = label,
      style =
        if (emphasize) MaterialTheme.typography.titleMedium
        else MaterialTheme.typography.bodyMedium,
      color = CharcoalText,
    )
    Text(
      text = value,
      style =
        if (emphasize) MaterialTheme.typography.titleLarge
        else MaterialTheme.typography.bodyMedium,
      fontWeight = if (emphasize) FontWeight.Bold else FontWeight.Medium,
      color = if (emphasize) DeepMaroon else CharcoalText,
    )
  }
}
