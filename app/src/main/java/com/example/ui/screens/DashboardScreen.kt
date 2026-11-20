package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.Formatters
import com.example.model.DemoEnquiryRecord
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
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberSoft

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
  savedEnquiries: List<DemoEnquiryRecord>,
  totalUnitsRequested: Int,
  estimatedEnquiryValueInr: Int,
  storeWhatsAppDigits: String,
  onSaveStoreWhatsApp: (String) -> Pair<Boolean, String>,
  onLoadSampleEnquiries: () -> Unit,
  onResetDemoData: () -> Unit,
  onNavigateFooter: (String) -> Unit,
) {
  var whatsAppInput by remember(storeWhatsAppDigits) { mutableStateOf(storeWhatsAppDigits) }
  var configFeedback by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
  var selectedEnquiry by remember { mutableStateOf<DemoEnquiryRecord?>(null) }
  var showResetConfirmDialog by remember { mutableStateOf(false) }

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
        text = "Educational Demo Dashboard",
        style = MaterialTheme.typography.headlineLarge,
        color = CharcoalText,
        modifier = Modifier.semantics { heading() },
      )

      Surface(
        color = WarningAmberSoft,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(Icons.Outlined.Info, contentDescription = null, tint = WarningAmber)
          Text(
            text = "Educational demo only. This is not a secure production administration system.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = WarningAmber,
          )
        }
      }

      // Summary Metrics calculated strictly from saved records
      BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val columns = if (maxWidth >= 680.dp) 3 else 1
        val cards =
          listOf(
            Triple(
              "Total saved demo enquiries",
              savedEnquiries.size.toString(),
              "dashboard_metric_count",
            ),
            Triple(
              "Total units requested",
              totalUnitsRequested.toString(),
              "dashboard_metric_units",
            ),
            Triple(
              "Estimated enquiry value",
              Formatters.formatInr(estimatedEnquiryValueInr),
              "dashboard_metric_value",
            ),
          )
        if (columns == 3) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            cards.forEach { (label, value, tag) ->
              MetricCard(label = label, value = value, tag = tag, modifier = Modifier.weight(1f))
            }
          }
        } else {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            cards.forEach { (label, value, tag) ->
              MetricCard(label = label, value = value, tag = tag, modifier = Modifier.fillMaxWidth())
            }
          }
        }
      }

      // Store WhatsApp Number Configuration
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
          Text(
            text = "Store WhatsApp Configuration",
            style = MaterialTheme.typography.titleLarge,
            color = CharcoalText,
          )
          Text(
            text =
              if (storeWhatsAppDigits.isEmpty()) {
                "Current status: Not configured (default). Enquiries use the Copy Message fallback."
              } else {
                "Current status: Configured (+$storeWhatsAppDigits)"
              },
            style = MaterialTheme.typography.bodySmall,
            color = if (storeWhatsAppDigits.isEmpty()) MutedCharcoalText else SuccessSage,
            modifier = Modifier.testTag("whatsapp_config_status"),
          )

          OutlinedTextField(
            value = whatsAppInput,
            onValueChange = {
              whatsAppInput = it
              configFeedback = null
            },
            label = { Text("Store WhatsApp Number") },
            placeholder = { Text("e.g., 9876543210 or +91 9876543210") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(12.dp),
            colors =
              OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DeepMaroon,
                unfocusedBorderColor = SoftBorder,
              ),
            modifier = Modifier.fillMaxWidth().testTag("whatsapp_config_input"),
          )

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Button(
              onClick = { configFeedback = onSaveStoreWhatsApp(whatsAppInput) },
              shape = RoundedCornerShape(12.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = DeepMaroon,
                  contentColor = WarmIvory,
                ),
              modifier = Modifier.minimumInteractiveComponentSize().testTag("save_whatsapp_button"),
            ) {
              Text("Save Number")
            }

            OutlinedButton(
              onClick = {
                whatsAppInput = ""
                configFeedback = onSaveStoreWhatsApp("")
              },
              shape = RoundedCornerShape(12.dp),
              border = BorderStroke(1.dp, SoftBorder),
              modifier =
                Modifier.minimumInteractiveComponentSize().testTag("clear_whatsapp_button"),
            ) {
              Text("Clear Number", color = CharcoalText)
            }
          }

          if (configFeedback != null) {
            val (ok, msg) = configFeedback!!
            Surface(
              color = if (ok) SuccessSageSoft else ErrorCrimsonSoft,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
            ) {
              Text(
                text = msg,
                style = MaterialTheme.typography.bodySmall,
                color = if (ok) SuccessSage else ErrorCrimson,
                modifier = Modifier.padding(10.dp),
              )
            }
          }
        }
      }

      // Demo Controls: Load Sample Enquiries & Reset Demo Data
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        OutlinedButton(
          onClick = onLoadSampleEnquiries,
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, DeepMaroon),
          modifier = Modifier.minimumInteractiveComponentSize().testTag("load_sample_enquiries"),
        ) {
          Text("Load sample enquiries", color = DeepMaroon)
        }

        OutlinedButton(
          onClick = { showResetConfirmDialog = true },
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, ErrorCrimson),
          modifier = Modifier.minimumInteractiveComponentSize().testTag("reset_demo_data_button"),
        ) {
          Icon(
            Icons.Outlined.DeleteSweep,
            contentDescription = null,
            tint = ErrorCrimson,
          )
          Text("Reset Demo Data", color = ErrorCrimson, modifier = Modifier.padding(start = 6.dp))
        }
      }

      // Saved Enquiries List
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Text(
            text = "Recent Saved Enquiries",
            style = MaterialTheme.typography.titleLarge,
            color = CharcoalText,
          )
          Text(
            text =
              "Only non-sensitive reference, date, item snapshots, quantities, and estimated totals are stored. Tap any record to inspect details.",
            style = MaterialTheme.typography.bodySmall,
            color = MutedCharcoalText,
          )

          if (savedEnquiries.isEmpty()) {
            Surface(
              color = SoftCream,
              shape = RoundedCornerShape(12.dp),
              border = BorderStroke(1.dp, SoftBorder),
              modifier = Modifier.fillMaxWidth().testTag("dashboard_empty_enquiries"),
            ) {
              Text(
                text =
                  "No saved demo enquiries yet. Complete the order-enquiry form or load sample enquiries for classroom demonstration.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedCharcoalText,
                modifier = Modifier.padding(16.dp),
              )
            }
          } else {
            savedEnquiries.forEach { rec ->
              Surface(
                color = SoftCream,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, SoftBorder),
                modifier =
                  Modifier.fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clickable { selectedEnquiry = rec }
                    .testTag("dashboard_enquiry_${rec.reference}"),
              ) {
                Column(
                  modifier = Modifier.padding(14.dp),
                  verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Text(
                      text = rec.reference,
                      style = MaterialTheme.typography.titleMedium,
                      color = DeepMaroon,
                    )
                    Surface(
                      color = if (rec.isSample) MutedGoldSoft else DeepMaroonSoft,
                      shape = RoundedCornerShape(50),
                    ) {
                      Text(
                        text = if (rec.isSample) "Sample data" else "Demo record",
                        style = MaterialTheme.typography.labelSmall,
                        color = CharcoalText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                      )
                    }
                  }
                  Text(
                    text = "${rec.createdAtDisplay} • ${rec.totalUnits} unit(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedCharcoalText,
                  )
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                  ) {
                    Text(
                      text = rec.status,
                      style = MaterialTheme.typography.labelSmall,
                      color = MutedCharcoalText,
                    )
                    Text(
                      text = Formatters.formatInr(rec.estimatedMerchandiseTotalInr),
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      color = CharcoalText,
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }

  // Enquiry Detail Modal
  val activeEnquiry = selectedEnquiry
  if (activeEnquiry != null) {
    Dialog(
      onDismissRequest = { selectedEnquiry = null },
      properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
    ) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = ElevatedCardSurface,
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth().widthIn(max = 540.dp),
      ) {
        Column(
          modifier = Modifier.verticalScroll(rememberScrollState()).padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = activeEnquiry.reference,
              style = MaterialTheme.typography.titleLarge,
              color = DeepMaroon,
            )
            IconButton(onClick = { selectedEnquiry = null }) {
              Icon(Icons.Outlined.Close, contentDescription = "Close enquiry details")
            }
          }
          Surface(
            color = if (activeEnquiry.isSample) MutedGoldSoft else DeepMaroonSoft,
            shape = RoundedCornerShape(50),
          ) {
            Text(
              text = if (activeEnquiry.isSample) "Sample data" else "Demo record",
              style = MaterialTheme.typography.labelSmall,
              color = CharcoalText,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            )
          }
          SummaryRow("Date", activeEnquiry.createdAtDisplay)
          SummaryRow("Status", activeEnquiry.status)
          HorizontalDivider(color = SoftBorder)
          activeEnquiry.items.forEach { item ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(item.productName, style = MaterialTheme.typography.titleSmall)
                Text(
                  text = "${item.regionName} • ${item.size} • ${item.color} × ${item.quantity}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MutedCharcoalText,
                )
              }
              Text(
                text = Formatters.formatInr(item.lineTotalInr),
                style = MaterialTheme.typography.titleSmall,
                color = DeepMaroon,
              )
            }
          }
          HorizontalDivider(color = SoftBorder)
          SummaryRow(
            label = "Estimated merchandise total",
            value = Formatters.formatInr(activeEnquiry.estimatedMerchandiseTotalInr),
            emphasize = true,
          )
        }
      }
    }
  }

  // Reset Confirmation Dialog
  if (showResetConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showResetConfirmDialog = false },
      title = { Text("Reset Demo Data?") },
      text = {
        Text(
          "This clears your locally saved wishlist, shopping bag, saved demo enquiries, and configured store WhatsApp number."
        )
      },
      confirmButton = {
        Button(
          onClick = {
            onResetDemoData()
            whatsAppInput = ""
            configFeedback = null
            showResetConfirmDialog = false
          },
          colors =
            ButtonDefaults.buttonColors(
              containerColor = ErrorCrimson,
              contentColor = WarmIvory,
            ),
          modifier = Modifier.testTag("confirm_reset_button"),
        ) {
          Text("Reset All")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirmDialog = false }) { Text("Cancel") }
      },
    )
  }
}

@Composable
private fun MetricCard(
  label: String,
  value: String,
  tag: String,
  modifier: Modifier = Modifier,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
    border = BorderStroke(1.dp, SoftBorder),
    modifier = modifier.testTag(tag),
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = MutedCharcoalText,
      )
      Text(
        text = value,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = DeepMaroon,
      )
    }
  }
}
