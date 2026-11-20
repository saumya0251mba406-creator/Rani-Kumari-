package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.model.DemoEnquiryRecord
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.SoftCream
import com.example.ui.theme.SuccessSage
import com.example.ui.theme.SuccessSageSoft
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConfirmationScreen(
  record: DemoEnquiryRecord?,
  onContinueExploring: () -> Unit,
  onViewDashboard: () -> Unit,
  onNavigateFooter: (String) -> Unit,
) {
  if (record == null) {
    NotFoundScreen(
      title = "Enquiry Record Not Found",
      message = "No locally saved demo enquiry matches that reference.",
      onPrimaryAction = onContinueExploring,
      primaryLabel = "Continue Exploring",
      onSecondaryAction = onViewDashboard,
      secondaryLabel = "View Demo Dashboard",
    )
    return
  }

  Column(
    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().widthIn(max = 820.dp).padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Spacer(modifier = Modifier.height(8.dp))

      Surface(
        color = SuccessSageSoft,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, SuccessSage),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
          modifier = Modifier.padding(18.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.Top,
        ) {
          Icon(
            Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = SuccessSage,
            modifier = Modifier.size(28.dp),
          )
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = "Demo Enquiry Saved",
              style = MaterialTheme.typography.headlineMedium,
              color = CharcoalText,
              modifier = Modifier.semantics { heading() }.testTag("confirmation_heading"),
            )
            Text(
              text =
                "No payment has been collected. This enquiry is stored only locally on this device for assignment demonstration.",
              style = MaterialTheme.typography.bodyMedium,
              color = CharcoalText,
            )
          }
        }
      }

      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          SummaryRow("Reference", record.reference, emphasize = true)
          SummaryRow("Date", record.createdAtDisplay)
          SummaryRow("Status", record.status)
          HorizontalDivider(color = SoftBorder)

          Text(
            text = "Requested Items (${record.totalUnits} unit(s))",
            style = MaterialTheme.typography.titleMedium,
            color = DeepMaroon,
          )
          record.items.forEach { item ->
            Surface(
              color = SoftCream,
              shape = RoundedCornerShape(10.dp),
              border = BorderStroke(1.dp, SoftBorder),
              modifier = Modifier.fillMaxWidth(),
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = item.productName,
                    style = MaterialTheme.typography.titleSmall,
                    color = CharcoalText,
                  )
                  Text(
                    text = "${item.regionName} • Size: ${item.size} • Color: ${item.color} • Qty: ${item.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedCharcoalText,
                  )
                }
                Text(
                  text = Formatters.formatInr(item.lineTotalInr),
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = DeepMaroon,
                )
              }
            }
          }

          HorizontalDivider(color = SoftBorder)
          SummaryRow(
            label = "Estimated merchandise total",
            value = Formatters.formatInr(record.estimatedMerchandiseTotalInr),
            emphasize = true,
          )
          Text(
            text = "Shipping and final payment terms are not collected in this prototype.",
            style = MaterialTheme.typography.bodySmall,
            color = MutedCharcoalText,
          )
        }
      }

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Button(
          onClick = onContinueExploring,
          shape = RoundedCornerShape(12.dp),
          colors =
            ButtonDefaults.buttonColors(
              containerColor = DeepMaroon,
              contentColor = WarmIvory,
            ),
          modifier = Modifier.minimumInteractiveComponentSize().testTag("confirm_continue_exploring"),
        ) {
          Text("Continue Exploring")
        }
        OutlinedButton(
          onClick = onViewDashboard,
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, DeepMaroon),
          modifier = Modifier.minimumInteractiveComponentSize().testTag("confirm_view_dashboard"),
        ) {
          Text("View Demo Dashboard", color = DeepMaroon)
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }
}
