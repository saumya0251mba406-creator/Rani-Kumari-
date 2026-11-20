package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Product
import com.example.model.Region
import com.example.model.TextileMotif
import com.example.ui.components.ProductArtwork
import com.example.ui.components.ProductCard
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.DeepMaroonDark
import com.example.ui.theme.DeepMaroonSoft
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldSoft
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.SoftCream
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
  featuredProducts: List<Product>,
  wishlistIds: Set<String>,
  onExploreCollection: () -> Unit,
  onDiscoverStories: () -> Unit,
  onSelectRegion: (Region) -> Unit,
  onToggleWishlist: (String) -> Unit,
  onViewProduct: (String) -> Unit,
  onNavigateFooter: (String) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().widthIn(max = 1040.dp).padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
      Spacer(modifier = Modifier.height(4.dp))

      // 1. Hero Section
      Surface(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Box(
          modifier =
            Modifier.background(
              Brush.linearGradient(
                colors = listOf(DeepMaroonSoft, SoftCream, MutedGoldSoft)
              )
            )
              .padding(24.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(
              color = WarmIvory,
              shape = RoundedCornerShape(50),
              border = BorderStroke(1.dp, SoftBorder),
            ) {
              Text(
                text = "RIWAAYAT • INDIAN HERITAGE. YOUR EVERYDAY STYLE.",
                style = MaterialTheme.typography.labelSmall,
                color = DeepMaroon,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              )
            }

            Text(
              text = "Wear a Story. Celebrate a Region.",
              style = MaterialTheme.typography.displayMedium,
              color = CharcoalText,
              modifier = Modifier.semantics { heading() },
            )

            Text(
              text =
                "Explore regional Indian fashion, discover design stories, and find pieces that connect heritage with your everyday style.",
              style = MaterialTheme.typography.bodyLarge,
              color = MutedCharcoalText,
            )

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              Button(
                onClick = onExploreCollection,
                shape = RoundedCornerShape(14.dp),
                colors =
                  ButtonDefaults.buttonColors(
                    containerColor = DeepMaroon,
                    contentColor = WarmIvory,
                  ),
                modifier = Modifier.minimumInteractiveComponentSize().testTag("hero_explore_cta"),
              ) {
                Text("Explore Collection")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                )
              }

              OutlinedButton(
                onClick = onDiscoverStories,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, DeepMaroon),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepMaroon),
                modifier = Modifier.minimumInteractiveComponentSize().testTag("hero_stories_cta"),
              ) {
                Icon(
                  imageVector = Icons.Outlined.AutoStories,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Discover Regional Stories")
              }
            }
          }
        }
      }

      // 2. Shop by Region
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
          title = "Shop by Region",
          subtitle = "Starting in Bihar with planned expansion across four cultural regions.",
        )

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
          val columns = if (maxWidth >= 700.dp) 3 else if (maxWidth >= 460.dp) 2 else 1
          val regions = Region.entries
          val rows = regions.chunked(columns)
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            rows.forEach { rowItems ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
              ) {
                rowItems.forEach { region ->
                  RegionCard(
                    region = region,
                    onClick = { onSelectRegion(region) },
                    modifier = Modifier.weight(1f),
                  )
                }
                repeat(columns - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
              }
            }
          }
        }
      }

      // 3. Featured Clothing
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          SectionHeader(
            title = "Featured Clothing",
            subtitle = "Curated sample pieces from the prototype catalogue.",
            modifier = Modifier.weight(1f),
          )
          OutlinedButton(
            onClick = onExploreCollection,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, SoftBorder),
          ) {
            Text("View All", color = DeepMaroon)
          }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
          val columns = if (maxWidth >= 820.dp) 3 else if (maxWidth >= 560.dp) 2 else 1
          val rows = featuredProducts.chunked(columns)
          Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            rows.forEach { rowProducts ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
              ) {
                rowProducts.forEach { product ->
                  ProductCard(
                    product = product,
                    isWishlisted = wishlistIds.contains(product.id),
                    onToggleWishlist = { onToggleWishlist(product.id) },
                    onViewDetails = { onViewProduct(product.id) },
                    modifier = Modifier.weight(1f),
                  )
                }
                repeat(columns - rowProducts.size) { Spacer(modifier = Modifier.weight(1f)) }
              }
            }
          }
        }
      }

      // 4. How Riwaayat Works
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          SectionHeader(
            title = "How Riwaayat Works",
            subtitle = "A simple, human-assisted enquiry journey built for boutique discovery.",
          )
          val steps =
            listOf(
              "1" to "Explore the collection." to "Browse kurtas, dupattas, stoles, and sets by region, category, or price.",
              "2" to "Read product and design details." to "Inspect illustrative fabrics, care notes, and regional design inspiration.",
              "3" to "Select your preferred variant." to "Pick your size (where applicable), colorway, and quantity within demo stock.",
              "4" to "Send an order enquiry." to "Save a local demo enquiry or prepare a structured WhatsApp enquiry message.",
            )
          steps.forEach { (numberTitle, desc) ->
            Row(
              verticalAlignment = Alignment.Top,
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Surface(
                color = DeepMaroon,
                shape = CircleShape,
                modifier = Modifier.size(30.dp),
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = numberTitle.first,
                    style = MaterialTheme.typography.labelLarge,
                    color = WarmIvory,
                  )
                }
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = numberTitle.second,
                  style = MaterialTheme.typography.titleMedium,
                  color = CharcoalText,
                )
                Text(
                  text = desc,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MutedCharcoalText,
                )
              }
            }
          }
        }
      }

      // 5. Why Riwaayat
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
          title = "Why Riwaayat",
          subtitle = "Designed for students and young professionals seeking clarity and cultural expression.",
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
          val columns = if (maxWidth >= 600.dp) 2 else 1
          val pillars =
            listOf(
              Triple(
                Icons.Outlined.Explore,
                "Regional discovery",
                "Bring garments inspired by Bihar, Uttar Pradesh, Punjab, Uttarakhand, and Telangana into one organized space.",
              ),
              Triple(
                Icons.Outlined.AutoAwesome,
                "Contemporary styling",
                "Curated silhouettes tailored for college wear, workdays, festivals, and thoughtful gifting.",
              ),
              Triple(
                Icons.Outlined.CheckCircle,
                "Clear product information",
                "Transparent pricing, variant availability, care guidance, and honest origin-verification status.",
              ),
              Triple(
                Icons.Outlined.AutoStories,
                "Stories behind designs",
                "Short regional context cards that explain the visual inspiration behind every sample piece.",
              ),
            )
          val rows = pillars.chunked(columns)
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            rows.forEach { rowItems ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
              ) {
                rowItems.forEach { (icon, title, body) ->
                  PillarCard(
                    icon = icon,
                    title = title,
                    body = body,
                    modifier = Modifier.weight(1f),
                  )
                }
                repeat(columns - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
              }
            }
          }
        }
      }

      // 6. Artisan-support intention note
      Surface(
        color = SoftCream,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
          modifier = Modifier.padding(18.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.Top,
        ) {
          Icon(
            imageVector = Icons.Outlined.Handyman,
            contentDescription = null,
            tint = MutedGold,
            modifier = Modifier.size(24.dp),
          )
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "Artisan & Supplier Intention",
              style = MaterialTheme.typography.titleMedium,
              color = DeepMaroonDark,
            )
            Text(
              text =
                "Riwaayat intends to create opportunities for local artisans and regional suppliers to showcase their work as the venture progresses. All sample listings in this prototype are demonstration concepts with supplier origins pending verification.",
              style = MaterialTheme.typography.bodyMedium,
              color = CharcoalText,
            )
          }
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }
}

@Composable
private fun SectionHeader(
  title: String,
  subtitle: String,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Text(
      text = title,
      style = MaterialTheme.typography.headlineMedium,
      color = CharcoalText,
      modifier = Modifier.semantics { heading() },
    )
    Text(
      text = subtitle,
      style = MaterialTheme.typography.bodyMedium,
      color = MutedCharcoalText,
    )
  }
}

@Composable
private fun RegionCard(
  region: Region,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val motif =
    when (region) {
      Region.BIHAR -> TextileMotif.MITHILA_LOTUS
      Region.UTTAR_PRADESH -> TextileMotif.BANARAS_BROCADE
      Region.PUNJAB -> TextileMotif.PHULKARI_DIAMOND
      Region.UTTARAKHAND -> TextileMotif.AIPAN_GEOMETRY
      Region.TELANGANA -> TextileMotif.POCHAMPALLY_IKAT
    }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
    border = BorderStroke(1.dp, SoftBorder),
    modifier =
      modifier
        .fillMaxWidth()
        .minimumInteractiveComponentSize()
        .clickable(onClick = onClick)
        .testTag("home_region_${region.name.lowercase()}"),
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ProductArtwork(
        productName = region.displayName,
        region = region,
        motif = motif,
        altText = "${region.displayName} regional motif",
        showProductTitleOverlay = false,
        modifier = Modifier.size(60.dp),
      )
      Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
          text = region.displayName,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.SemiBold,
          color = CharcoalText,
        )
        Text(
          text = region.subtitle,
          style = MaterialTheme.typography.labelSmall,
          color = DeepMaroon,
        )
        Text(
          text = region.motifLabel,
          style = MaterialTheme.typography.bodySmall,
          color = MutedCharcoalText,
        )
      }
      Icon(
        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
        contentDescription = "Explore ${region.displayName}",
        tint = DeepMaroon,
        modifier = Modifier.size(18.dp),
      )
    }
  }
}

@Composable
private fun PillarCard(
  icon: ImageVector,
  title: String,
  body: String,
  modifier: Modifier = Modifier,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
    border = BorderStroke(1.dp, SoftBorder),
    modifier = modifier.fillMaxWidth(),
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = DeepMaroon,
        modifier = Modifier.size(22.dp),
      )
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = CharcoalText,
      )
      Text(
        text = body,
        style = MaterialTheme.typography.bodySmall,
        color = MutedCharcoalText,
      )
    }
  }
}
