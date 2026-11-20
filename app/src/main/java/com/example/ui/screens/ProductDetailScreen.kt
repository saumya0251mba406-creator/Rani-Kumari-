package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalShipping
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Formatters
import com.example.data.ProductCatalog
import com.example.model.Product
import com.example.ui.BagActionResult
import com.example.ui.components.ProductArtwork
import com.example.ui.components.ProductCard
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.DeepMaroonSoft
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.ErrorCrimsonSoft
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.MutedGoldSoft
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.SoftCream
import com.example.ui.theme.SuccessSage
import com.example.ui.theme.SuccessSageSoft
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailScreen(
  product: Product?,
  isWishlisted: Boolean,
  wishlistIds: Set<String>,
  onBack: () -> Unit,
  onExploreCollection: () -> Unit,
  onToggleWishlist: (String) -> Unit,
  onAddToBag: (Product, String?, String?, Int) -> BagActionResult,
  onViewBag: () -> Unit,
  onViewRelatedProduct: (String) -> Unit,
  onNavigateFooter: (String) -> Unit,
) {
  if (product == null) {
    NotFoundScreen(
      title = "Product Not Found",
      message = "The requested product could not be found in the Riwaayat prototype catalogue.",
      onPrimaryAction = onExploreCollection,
      primaryLabel = "Explore Collection",
      onSecondaryAction = onBack,
      secondaryLabel = "Go Back",
    )
    return
  }

  val defaultVariant = remember(product.id) { product.defaultVariant() }
  var selectedSize by remember(product.id) {
    mutableStateOf(
      if (product.category.requiresSizeSelection) defaultVariant.size else Product.ONE_SIZE
    )
  }
  var selectedColor by remember(product.id) { mutableStateOf(defaultVariant.color) }
  var quantity by remember(product.id) { mutableIntStateOf(1) }
  var actionFeedback by remember(product.id) { mutableStateOf<BagActionResult?>(null) }

  val currentStock = product.stockFor(selectedSize, selectedColor)
  val selectedColorOption =
    product.availableColors.firstOrNull { it.name == selectedColor }
      ?: product.availableColors.firstOrNull()

  Column(
    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().widthIn(max = 1040.dp).padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
      Spacer(modifier = Modifier.height(4.dp))

      TextButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, tint = DeepMaroon)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Back to Catalogue", color = DeepMaroon)
      }

      BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val isWide = maxWidth >= 760.dp
        if (isWide) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top,
          ) {
            DetailArtworkBlock(
              product = product,
              accentHex = selectedColorOption?.swatchHex,
              modifier = Modifier.weight(0.95f),
            )
            DetailPurchaseBlock(
              product = product,
              selectedSize = selectedSize,
              selectedColor = selectedColor,
              quantity = quantity,
              currentStock = currentStock,
              isWishlisted = isWishlisted,
              actionFeedback = actionFeedback,
              onSelectSize = {
                selectedSize = it
                quantity = 1
                actionFeedback = null
              },
              onSelectColor = {
                selectedColor = it
                quantity = 1
                actionFeedback = null
              },
              onChangeQuantity = { next ->
                quantity = next
                actionFeedback = null
              },
              onAddToBag = {
                actionFeedback = onAddToBag(product, selectedSize, selectedColor, quantity)
              },
              onToggleWishlist = { onToggleWishlist(product.id) },
              onViewBag = onViewBag,
              modifier = Modifier.weight(1.05f),
            )
          }
        } else {
          Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            DetailArtworkBlock(
              product = product,
              accentHex = selectedColorOption?.swatchHex,
              modifier = Modifier.fillMaxWidth(),
            )
            DetailPurchaseBlock(
              product = product,
              selectedSize = selectedSize,
              selectedColor = selectedColor,
              quantity = quantity,
              currentStock = currentStock,
              isWishlisted = isWishlisted,
              actionFeedback = actionFeedback,
              onSelectSize = {
                selectedSize = it
                quantity = 1
                actionFeedback = null
              },
              onSelectColor = {
                selectedColor = it
                quantity = 1
                actionFeedback = null
              },
              onChangeQuantity = { next ->
                quantity = next
                actionFeedback = null
              },
              onAddToBag = {
                actionFeedback = onAddToBag(product, selectedSize, selectedColor, quantity)
              },
              onToggleWishlist = { onToggleWishlist(product.id) },
              onViewBag = onViewBag,
              modifier = Modifier.fillMaxWidth(),
            )
          }
        }
      }

      // Specifications, Inspiration, Origin & Policies
      Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        InfoSectionCard(
          title = "Materials & Care",
          badge = product.specificationNote,
        ) {
          Text(
            text = product.illustrativeMaterial,
            style = MaterialTheme.typography.bodyMedium,
            color = CharcoalText,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = product.careInstructions,
            style = MaterialTheme.typography.bodyMedium,
            color = MutedCharcoalText,
          )
        }

        InfoSectionCard(
          title = "Design Inspiration & Origin Status",
          badge = product.originStatus,
        ) {
          Text(
            text = product.designInspiration,
            style = MaterialTheme.typography.bodyMedium,
            color = CharcoalText,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text =
              "Riwaayat presents sample designs inspired by regional Indian aesthetics. Supplier sourcing, craft techniques, and origin details will be verified before commercial launch.",
            style = MaterialTheme.typography.bodySmall,
            color = MutedCharcoalText,
          )
        }

        InfoSectionCard(title = "Delivery & Returns Information") {
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(
              Icons.Outlined.LocalShipping,
              contentDescription = null,
              tint = DeepMaroon,
              modifier = Modifier.size(20.dp),
            )
            Text(
              text = product.deliveryNote,
              style = MaterialTheme.typography.bodyMedium,
              color = CharcoalText,
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(
              Icons.Outlined.Info,
              contentDescription = null,
              tint = MutedCharcoalText,
              modifier = Modifier.size(20.dp),
            )
            Text(
              text = product.returnsNote,
              style = MaterialTheme.typography.bodyMedium,
              color = MutedCharcoalText,
            )
          }
        }
      }

      // Related Products
      val related = remember(product.id) { ProductCatalog.relatedProducts(product, limit = 3) }
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          text = "Related Regional Pieces",
          style = MaterialTheme.typography.headlineMedium,
          color = CharcoalText,
          modifier = Modifier.semantics { heading() },
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
          val columns = if (maxWidth >= 820.dp) 3 else if (maxWidth >= 560.dp) 2 else 1
          val rows = related.chunked(columns)
          Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            rows.forEach { rowItems ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
              ) {
                rowItems.forEach { rel ->
                  ProductCard(
                    product = rel,
                    isWishlisted = wishlistIds.contains(rel.id),
                    onToggleWishlist = { onToggleWishlist(rel.id) },
                    onViewDetails = { onViewRelatedProduct(rel.id) },
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

@Composable
private fun DetailArtworkBlock(
  product: Product,
  accentHex: Long?,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
    ProductArtwork(
      productName = product.name,
      region = product.region,
      motif = product.motif,
      altText = product.altText,
      accentHex = accentHex,
      modifier = Modifier.fillMaxWidth().aspectRatio(1.05f),
      showProductTitleOverlay = true,
    )
    Text(
      text = product.altText,
      style = MaterialTheme.typography.bodySmall,
      color = MutedCharcoalText,
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailPurchaseBlock(
  product: Product,
  selectedSize: String,
  selectedColor: String,
  quantity: Int,
  currentStock: Int,
  isWishlisted: Boolean,
  actionFeedback: BagActionResult?,
  onSelectSize: (String) -> Unit,
  onSelectColor: (String) -> Unit,
  onChangeQuantity: (Int) -> Unit,
  onAddToBag: () -> Unit,
  onToggleWishlist: () -> Unit,
  onViewBag: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      Surface(color = DeepMaroonSoft, shape = RoundedCornerShape(50)) {
        Text(
          text = product.region.displayName,
          style = MaterialTheme.typography.labelMedium,
          color = DeepMaroon,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
        )
      }
      Surface(color = MutedGoldSoft, shape = RoundedCornerShape(50)) {
        Text(
          text = product.category.displayName,
          style = MaterialTheme.typography.labelMedium,
          color = CharcoalText,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
        )
      }
    }

    Text(
      text = product.name,
      style = MaterialTheme.typography.headlineLarge,
      color = CharcoalText,
      modifier = Modifier.semantics { heading() },
    )

    Text(
      text = Formatters.formatInr(product.priceInr),
      style = MaterialTheme.typography.headlineMedium,
      color = DeepMaroon,
      fontWeight = FontWeight.Bold,
    )

    Text(
      text = product.fullDescription,
      style = MaterialTheme.typography.bodyLarge,
      color = CharcoalText,
    )

    HorizontalDivider(color = SoftBorder)

    // Size Selection
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(
        text =
          if (product.category.requiresSizeSelection) "Select Size: $selectedSize"
          else "Size: ${Product.ONE_SIZE}",
        style = MaterialTheme.typography.titleSmall,
        color = CharcoalText,
      )
      if (product.category.requiresSizeSelection) {
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          product.availableSizes.forEach { size ->
            val selected = selectedSize == size
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (selected) DeepMaroon else ElevatedCardSurface,
              border = BorderStroke(1.dp, if (selected) DeepMaroon else SoftBorder),
              modifier =
                Modifier.minimumInteractiveComponentSize()
                  .clickable { onSelectSize(size) }
                  .testTag("size_option_$size"),
            ) {
              Text(
                text = size,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) WarmIvory else CharcoalText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              )
            }
          }
        }
      } else {
        Text(
          text = "Draped accessory — standardized One Size for this prototype.",
          style = MaterialTheme.typography.bodySmall,
          color = MutedCharcoalText,
        )
      }
    }

    // Color Selection
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(
        text = "Select Color: $selectedColor",
        style = MaterialTheme.typography.titleSmall,
        color = CharcoalText,
      )
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        product.availableColors.forEach { option ->
          val selected = selectedColor == option.name
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (selected) DeepMaroonSoft else ElevatedCardSurface,
            border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) DeepMaroon else SoftBorder),
            modifier =
              Modifier.minimumInteractiveComponentSize()
                .clickable { onSelectColor(option.name) }
                .testTag("color_option_${option.name}"),
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              Box(
                modifier =
                  Modifier.size(18.dp)
                    .clip(CircleShape)
                    .background(Color(option.swatchHex))
                    .border(1.dp, SoftBorder, CircleShape)
              )
              Text(
                text = option.name,
                style = MaterialTheme.typography.labelLarge,
                color = CharcoalText,
              )
            }
          }
        }
      }
    }

    // Variant Demo Stock Status
    Surface(
      color = if (currentStock > 0) SuccessSageSoft else ErrorCrimsonSoft,
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier.fillMaxWidth().testTag("variant_stock_badge"),
    ) {
      Text(
        text =
          if (currentStock > 0) {
            "Demo availability ($selectedSize • $selectedColor): $currentStock unit(s) available"
          } else {
            "Unavailable ($selectedSize • $selectedColor): 0 in demo stock"
          },
        style = MaterialTheme.typography.labelLarge,
        color = if (currentStock > 0) SuccessSage else ErrorCrimson,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
      )
    }

    // Quantity Selector
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
        text = "Quantity",
        style = MaterialTheme.typography.titleSmall,
        color = CharcoalText,
      )
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = ElevatedCardSurface,
        border = BorderStroke(1.dp, SoftBorder),
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { if (quantity > 1) onChangeQuantity(quantity - 1) },
            enabled = currentStock > 0 && quantity > 1,
            modifier = Modifier.minimumInteractiveComponentSize().testTag("detail_qty_minus"),
          ) {
            Icon(Icons.Outlined.Remove, contentDescription = "Decrease quantity")
          }
          Text(
            text = if (currentStock == 0) "0" else quantity.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = CharcoalText,
            modifier = Modifier.padding(horizontal = 10.dp).testTag("detail_qty_value"),
          )
          IconButton(
            onClick = { if (quantity < currentStock) onChangeQuantity(quantity + 1) },
            enabled = currentStock > 0 && quantity < currentStock,
            modifier = Modifier.minimumInteractiveComponentSize().testTag("detail_qty_plus"),
          ) {
            Icon(Icons.Outlined.Add, contentDescription = "Increase quantity")
          }
        }
      }
    }

    // Primary Actions
    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Button(
        onClick = onAddToBag,
        enabled = currentStock > 0,
        shape = RoundedCornerShape(14.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = DeepMaroon,
            contentColor = WarmIvory,
          ),
        modifier = Modifier.minimumInteractiveComponentSize().testTag("add_to_bag_button"),
      ) {
        Icon(Icons.Outlined.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (currentStock > 0) "Add to Bag" else "Variant Unavailable")
      }

      OutlinedButton(
        onClick = onToggleWishlist,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, DeepMaroon),
        modifier = Modifier.minimumInteractiveComponentSize().testTag("detail_wishlist_button"),
      ) {
        Icon(
          imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
          contentDescription = null,
          tint = DeepMaroon,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isWishlisted) "Saved in Wishlist" else "Save to Wishlist",
          color = DeepMaroon,
        )
      }
    }

    // Add to bag feedback banner
    if (actionFeedback != null) {
      val isSuccess = actionFeedback is BagActionResult.Added
      Surface(
        color = if (isSuccess) SuccessSageSoft else ErrorCrimsonSoft,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, if (isSuccess) SuccessSage else ErrorCrimson),
        modifier =
          Modifier.fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite }
            .testTag("add_to_bag_feedback"),
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = if (isSuccess) Icons.Outlined.CheckCircle else Icons.Outlined.ErrorOutline,
              contentDescription = null,
              tint = if (isSuccess) SuccessSage else ErrorCrimson,
            )
            Text(
              text =
                when (actionFeedback) {
                  is BagActionResult.Added ->
                    "Added ${actionFeedback.quantity} × ${actionFeedback.productName} (${actionFeedback.size}, ${actionFeedback.color}) to your bag."
                  is BagActionResult.Error -> actionFeedback.message
                },
              style = MaterialTheme.typography.bodyMedium,
              color = if (isSuccess) SuccessSage else ErrorCrimson,
            )
          }
          if (isSuccess) {
            TextButton(onClick = onViewBag, modifier = Modifier.testTag("feedback_view_bag")) {
              Text("View Bag", color = DeepMaroon, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun InfoSectionCard(
  title: String,
  badge: String? = null,
  content: @Composable () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SoftCream),
    border = BorderStroke(1.dp, SoftBorder),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = DeepMaroon,
      )
      if (badge != null) {
        Surface(color = WarmIvory, shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, SoftBorder)) {
          Text(
            text = badge,
            style = MaterialTheme.typography.labelSmall,
            color = MutedCharcoalText,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
          )
        }
      }
      Spacer(modifier = Modifier.height(2.dp))
      content()
    }
  }
}
