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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Close
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
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.Formatters
import com.example.data.ProductCatalog
import com.example.model.Region
import com.example.model.RegionalStory
import com.example.ui.components.ProductArtwork
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.DeepMaroonSoft
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.MutedGoldSoft
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.SoftCream
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoriesScreen(
  onShopRegion: (Region) -> Unit,
  onViewProduct: (String) -> Unit,
  onNavigateFooter: (String) -> Unit,
) {
  var selectedStory by remember { mutableStateOf<RegionalStory?>(null) }

  Column(
    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().widthIn(max = 1040.dp).padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Regional Stories",
        style = MaterialTheme.typography.headlineLarge,
        color = CharcoalText,
        modifier = Modifier.semantics { heading() },
      )
      Text(
        text =
          "Explore the regional aesthetics behind our five planned states. Each card connects sample garments to cautious design notes prepared for prototype demonstration.",
        style = MaterialTheme.typography.bodyMedium,
        color = MutedCharcoalText,
      )

      ProductCatalog.stories.forEach { story ->
        val regionProducts = ProductCatalog.productsForRegion(story.region)
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.fillMaxWidth().testTag("story_card_${story.region.name.lowercase()}"),
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(14.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              ProductArtwork(
                productName = story.region.displayName,
                region = story.region,
                motif = story.motif,
                altText = "${story.region.displayName} regional design motif",
                showProductTitleOverlay = false,
                modifier = Modifier.size(84.dp),
              )
              Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
              ) {
                Surface(color = DeepMaroonSoft, shape = RoundedCornerShape(50)) {
                  Text(
                    text = "${story.region.displayName} • ${story.region.subtitle}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DeepMaroon,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                  )
                }
                Text(
                  text = story.title,
                  style = MaterialTheme.typography.titleLarge,
                  color = CharcoalText,
                )
                Text(
                  text = story.craftFocus,
                  style = MaterialTheme.typography.bodySmall,
                  color = MutedCharcoalText,
                )
              }
            }

            Text(
              text = story.summary,
              style = MaterialTheme.typography.bodyMedium,
              color = CharcoalText,
            )

            Surface(
              color = SoftCream,
              shape = RoundedCornerShape(12.dp),
              border = BorderStroke(1.dp, SoftBorder),
            ) {
              Text(
                text = story.prototypeNote,
                style = MaterialTheme.typography.bodySmall,
                color = MutedCharcoalText,
                modifier = Modifier.padding(12.dp),
              )
            }

            // Linked Sample Products
            if (regionProducts.isNotEmpty()) {
              Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = "Sample pieces from ${story.region.displayName}:",
                  style = MaterialTheme.typography.labelLarge,
                  color = CharcoalText,
                )
                FlowRow(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                  regionProducts.forEach { product ->
                    Surface(
                      shape = RoundedCornerShape(10.dp),
                      color = MutedGoldSoft,
                      border = BorderStroke(1.dp, SoftBorder),
                      modifier =
                        Modifier.minimumInteractiveComponentSize().clickable {
                          onViewProduct(product.id)
                        },
                    ) {
                      Text(
                        text = "${product.name} (${Formatters.formatInr(product.priceInr)})",
                        style = MaterialTheme.typography.labelMedium,
                        color = CharcoalText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                      )
                    }
                  }
                }
              }
            }

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              Button(
                onClick = { onShopRegion(story.region) },
                shape = RoundedCornerShape(12.dp),
                colors =
                  ButtonDefaults.buttonColors(
                    containerColor = DeepMaroon,
                    contentColor = WarmIvory,
                  ),
                modifier =
                  Modifier.minimumInteractiveComponentSize()
                    .testTag("shop_region_${story.region.name.lowercase()}"),
              ) {
                Text("Shop this region")
                Spacer(modifier = Modifier.size(6.dp))
                Icon(
                  Icons.AutoMirrored.Outlined.ArrowForward,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                )
              }

              OutlinedButton(
                onClick = { selectedStory = story },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DeepMaroon),
                modifier =
                  Modifier.minimumInteractiveComponentSize()
                    .testTag("read_story_${story.region.name.lowercase()}"),
              ) {
                Icon(
                  Icons.Outlined.AutoStories,
                  contentDescription = null,
                  tint = DeepMaroon,
                  modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text("Read Story Details", color = DeepMaroon)
              }
            }
          }
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }

  val activeStory = selectedStory
  if (activeStory != null) {
    Dialog(
      onDismissRequest = { selectedStory = null },
      properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
    ) {
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = ElevatedCardSurface,
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth().widthIn(max = 560.dp),
      ) {
        Column(
          modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = activeStory.region.displayName,
              style = MaterialTheme.typography.labelLarge,
              color = DeepMaroon,
            )
            IconButton(
              onClick = { selectedStory = null },
              modifier = Modifier.minimumInteractiveComponentSize().testTag("close_story_modal"),
            ) {
              Icon(Icons.Outlined.Close, contentDescription = "Close story details")
            }
          }

          Text(
            text = activeStory.title,
            style = MaterialTheme.typography.headlineMedium,
            color = CharcoalText,
          )

          ProductArtwork(
            productName = activeStory.region.displayName,
            region = activeStory.region,
            motif = activeStory.motif,
            altText = "${activeStory.region.displayName} motif detail",
            showProductTitleOverlay = false,
            modifier = Modifier.fillMaxWidth().height(160.dp),
          )

          Text(
            text = activeStory.summary,
            style = MaterialTheme.typography.bodyLarge,
            color = CharcoalText,
          )

          HorizontalDivider(color = SoftBorder)

          Text(
            text = "Design Notes in This Prototype",
            style = MaterialTheme.typography.titleMedium,
            color = DeepMaroon,
          )
          activeStory.designHighlights.forEach { bullet ->
            Text(
              text = "• $bullet",
              style = MaterialTheme.typography.bodyMedium,
              color = CharcoalText,
            )
          }

          Surface(
            color = SoftCream,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, SoftBorder),
          ) {
            Text(
              text = activeStory.prototypeNote,
              style = MaterialTheme.typography.bodySmall,
              color = MutedCharcoalText,
              modifier = Modifier.padding(12.dp),
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
          ) {
            Button(
              onClick = {
                val region = activeStory.region
                selectedStory = null
                onShopRegion(region)
              },
              shape = RoundedCornerShape(12.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = DeepMaroon,
                  contentColor = WarmIvory,
                ),
            ) {
              Text("Shop ${activeStory.region.displayName}")
            }
          }
        }
      }
    }
  }
}
