package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.MutedGoldSoft
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.SoftCream

@Composable
fun AboutScreen(
  onNavigateFooter: (String) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().widthIn(max = 900.dp).padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "About Riwaayat",
        style = MaterialTheme.typography.headlineLarge,
        color = CharcoalText,
        modifier = Modifier.semantics { heading() },
      )
      Text(
        text = "Indian Heritage. Your Everyday Style.",
        style = MaterialTheme.typography.titleMedium,
        color = DeepMaroon,
      )

      Surface(
        color = MutedGoldSoft,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(
          text =
            "Riwaayat is an early-stage student venture concept. Product sourcing, origin verification, pricing, and customer demand remain subject to validation.",
          style = MaterialTheme.typography.bodyMedium,
          color = CharcoalText,
          modifier = Modifier.padding(16.dp),
        )
      }

      AboutSectionCard(
        title = "Venture Vision & Customer Problem",
        body =
          "Many young Indian women—especially college students and early-career professionals aged 18–30—enjoy regional clothing and craft-inspired fashion for campus life, festivals, and gifting. Yet discovering regional styles from different Indian states in one organized space, while understanding fabric, pricing, design background, and origin status, remains fragmented.",
      )

      AboutSectionCard(
        title = "Regional Discovery & Cultural Storytelling",
        body =
          "Riwaayat brings together a small, curated collection with accessible design notes and regional stories. Rather than overwhelming shoppers with thousands of unverified marketplace listings, the concept focuses on clarity, contemporary wearability, and thoughtful presentation.",
      )

      AboutSectionCard(
        title = "Bihar-First Launch & Intended Artisan Participation",
        body =
          "The venture proposes starting with a focused regional-clothing collection in Bihar, followed by planned expansion across Uttar Pradesh, Punjab, Uttarakhand, and Telangana. Over time, Riwaayat aims to create opportunities for local artisans and regional suppliers to showcase their work—subject to supplier onboarding and origin verification prior to commercial launch.",
      )

      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCream),
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Text(
            text = "Prototype Scope & Limitations",
            style = MaterialTheme.typography.titleMedium,
            color = DeepMaroon,
          )
          Text(
            text =
              "• Sample catalogue items, prices, stock numbers, and illustrations are demonstration data only.\n" +
                "• No live payments, shipping carrier integrations, or verified supplier certificates are active.\n" +
                "• Wishlist, bag, WhatsApp setting, and non-sensitive demo enquiry snapshots are stored locally on this device.\n" +
                "• Customer phone numbers, cities, states, pincodes, and notes entered in the enquiry form remain strictly in memory and are never persisted.",
            style = MaterialTheme.typography.bodySmall,
            color = MutedCharcoalText,
          )
          HorizontalDivider(color = SoftBorder, modifier = Modifier.padding(vertical = 4.dp))
          Text(
            text = "College entrepreneurship prototype by Rani Kumari.",
            style = MaterialTheme.typography.labelLarge,
            color = DeepMaroon,
          )
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }
}

@Composable
private fun AboutSectionCard(
  title: String,
  body: String,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
    border = BorderStroke(1.dp, SoftBorder),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(
      modifier = Modifier.padding(18.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = DeepMaroon,
      )
      Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = CharcoalText,
      )
    }
  }
}
