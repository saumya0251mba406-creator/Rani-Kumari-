package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotFoundScreen(
  title: String = "Page Not Found",
  message: String = "The requested page does not exist in this Riwaayat prototype.",
  onPrimaryAction: () -> Unit,
  primaryLabel: String = "Explore Collection",
  onSecondaryAction: () -> Unit,
  secondaryLabel: String = "Back to Home",
) {
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
      border = BorderStroke(1.dp, SoftBorder),
      modifier = Modifier.fillMaxWidth().widthIn(max = 520.dp).testTag("not_found_screen"),
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Icon(Icons.Outlined.Explore, contentDescription = null, tint = DeepMaroon)
        Text(
          text = title,
          style = MaterialTheme.typography.headlineMedium,
          color = CharcoalText,
        )
        Text(
          text = message,
          style = MaterialTheme.typography.bodyMedium,
          color = MutedCharcoalText,
        )
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Button(
            onClick = onPrimaryAction,
            shape = RoundedCornerShape(12.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = DeepMaroon,
                contentColor = WarmIvory,
              ),
            modifier = Modifier.minimumInteractiveComponentSize(),
          ) {
            Text(primaryLabel)
          }
          OutlinedButton(
            onClick = onSecondaryAction,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, DeepMaroon),
            modifier = Modifier.minimumInteractiveComponentSize(),
          ) {
            Text(secondaryLabel, color = DeepMaroon)
          }
        }
      }
    }
  }
}
