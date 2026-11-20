package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.model.CartItem
import com.example.model.EnquiryFormState
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.ErrorCrimsonSoft
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.SoftCream
import com.example.ui.theme.SuccessSage
import com.example.ui.theme.SuccessSageSoft
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberSoft

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnquiryScreen(
  bagItems: List<CartItem>,
  merchandiseSubtotalInr: Int,
  formState: EnquiryFormState,
  storeWhatsAppDigits: String,
  isSubmitting: Boolean,
  onUpdateForm: ((EnquiryFormState) -> EnquiryFormState) -> Unit,
  onSaveDemoEnquiry: () -> String?,
  onBuildWhatsAppMessageIfValid: () -> Pair<Boolean, String>,
  onEnquirySaved: (String) -> Unit,
  onExploreCollection: () -> Unit,
  onOpenDashboard: () -> Unit,
  onNavigateFooter: (String) -> Unit,
) {
  val context = LocalContext.current
  var actionMessage by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
  var manualCopyText by remember { mutableStateOf<String?>(null) }
  val isWhatsAppConfigured = storeWhatsAppDigits.isNotEmpty()

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
        text = "Send an Order Enquiry",
        style = MaterialTheme.typography.headlineLarge,
        color = CharcoalText,
        modifier = Modifier.semantics { heading() },
      )
      Text(
        text =
          "This is an order-enquiry flow, not a payment checkout. Customer contact fields remain only in memory during completion and are never saved in local storage.",
        style = MaterialTheme.typography.bodyMedium,
        color = MutedCharcoalText,
      )

      if (bagItems.isEmpty()) {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.fillMaxWidth().testTag("enquiry_empty_bag_guard"),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Icon(Icons.Outlined.ShoppingBag, contentDescription = null, tint = DeepMaroon)
            Text(
              text = "Your bag is empty",
              style = MaterialTheme.typography.titleLarge,
              color = CharcoalText,
            )
            Text(
              text = "Add at least one item to your shopping bag before sending an order enquiry.",
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
            ) {
              Text("Explore Collection")
            }
          }
        }
      } else {
        // Order Summary
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SoftCream),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.fillMaxWidth(),
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Text(
              text = "Order Summary (${bagItems.sumOf { it.quantity }} item(s))",
              style = MaterialTheme.typography.titleMedium,
              color = DeepMaroon,
            )
            bagItems.forEach { item ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                Text(
                  text = "${item.productName} (${item.size}, ${item.color}) × ${item.quantity}",
                  style = MaterialTheme.typography.bodySmall,
                  color = CharcoalText,
                  modifier = Modifier.weight(1f).padding(end = 8.dp),
                )
                Text(
                  text = Formatters.formatInr(item.lineTotalInr),
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.SemiBold,
                  color = CharcoalText,
                )
              }
            }
            HorizontalDivider(color = SoftBorder)
            SummaryRow("Merchandise subtotal", Formatters.formatInr(merchandiseSubtotalInr))
            SummaryRow("Shipping", "To be confirmed")
            SummaryRow(
              "Estimated merchandise total",
              Formatters.formatInr(merchandiseSubtotalInr),
              emphasize = true,
            )
          }
        }

        // Enquiry Form
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
              text = "Your Enquiry Details",
              style = MaterialTheme.typography.titleLarge,
              color = CharcoalText,
            )

            EnquiryField(
              label = "Full Name *",
              value = formState.fullName,
              onValueChange = { v ->
                onUpdateForm { it.copy(fullName = v, errors = it.errors.copy(fullName = null)) }
              },
              error = formState.errors.fullName,
              placeholder = "e.g., Ananya Sharma",
              tag = "enquiry_name_input",
            )

            EnquiryField(
              label = "Mobile Number (10-digit Indian mobile, optional +91) *",
              value = formState.mobileNumber,
              onValueChange = { v ->
                onUpdateForm {
                  it.copy(mobileNumber = v, errors = it.errors.copy(mobileNumber = null))
                }
              },
              error = formState.errors.mobileNumber,
              placeholder = "e.g., 9876543210 or +91 9876543210",
              keyboardType = KeyboardType.Phone,
              tag = "enquiry_phone_input",
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              EnquiryField(
                label = "City *",
                value = formState.city,
                onValueChange = { v ->
                  onUpdateForm { it.copy(city = v, errors = it.errors.copy(city = null)) }
                },
                error = formState.errors.city,
                placeholder = "e.g., Patna",
                tag = "enquiry_city_input",
                modifier = Modifier.weight(1f),
              )
              EnquiryField(
                label = "State *",
                value = formState.state,
                onValueChange = { v ->
                  onUpdateForm { it.copy(state = v, errors = it.errors.copy(state = null)) }
                },
                error = formState.errors.state,
                placeholder = "e.g., Bihar",
                tag = "enquiry_state_input",
                modifier = Modifier.weight(1f),
              )
            }

            EnquiryField(
              label = "Pincode (6 digits, cannot start with 0) *",
              value = formState.pincode,
              onValueChange = { v ->
                onUpdateForm { it.copy(pincode = v, errors = it.errors.copy(pincode = null)) }
              },
              error = formState.errors.pincode,
              placeholder = "e.g., 800001",
              keyboardType = KeyboardType.Number,
              tag = "enquiry_pincode_input",
            )

            EnquiryField(
              label = "Email (Optional)",
              value = formState.email,
              onValueChange = { v ->
                onUpdateForm { it.copy(email = v, errors = it.errors.copy(email = null)) }
              },
              error = formState.errors.email,
              placeholder = "Optional email address",
              keyboardType = KeyboardType.Email,
              tag = "enquiry_email_input",
            )

            EnquiryField(
              label = "Delivery or Sizing Note (Optional)",
              value = formState.deliveryNote,
              onValueChange = { v -> onUpdateForm { it.copy(deliveryNote = v) } },
              error = null,
              placeholder = "Optional preference or question (no full street address needed)",
              singleLine = false,
              tag = "enquiry_note_input",
            )

            Surface(
              color = SoftCream,
              shape = RoundedCornerShape(10.dp),
              border = BorderStroke(1.dp, SoftBorder),
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
              ) {
                Icon(
                  Icons.Outlined.Info,
                  contentDescription = null,
                  tint = MutedCharcoalText,
                  modifier = Modifier.size(18.dp),
                )
                Text(
                  text =
                    "Format validation checks input structure only; it does not verify phone ownership or delivery serviceability.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MutedCharcoalText,
                )
              }
            }

            // Required acknowledgement
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Row(
                modifier =
                  Modifier.fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clickable {
                      onUpdateForm {
                        it.copy(
                          acknowledged = !it.acknowledged,
                          errors = it.errors.copy(acknowledged = null),
                        )
                      }
                    }
                    .testTag("enquiry_ack_row"),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Checkbox(
                  checked = formState.acknowledged,
                  onCheckedChange = { checked ->
                    onUpdateForm {
                      it.copy(
                        acknowledged = checked,
                        errors = it.errors.copy(acknowledged = null),
                      )
                    }
                  },
                  colors = CheckboxDefaults.colors(checkedColor = DeepMaroon),
                  modifier = Modifier.testTag("enquiry_ack_checkbox"),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text =
                    "I understand this is a prototype enquiry and availability, shipping, and payment details must be confirmed.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = CharcoalText,
                )
              }
              if (formState.errors.acknowledged != null) {
                Text(
                  text = formState.errors.acknowledged,
                  style = MaterialTheme.typography.bodySmall,
                  color = ErrorCrimson,
                  modifier = Modifier.padding(start = 12.dp).testTag("error_ack"),
                )
              }
            }
          }
        }

        // Actions Card: Save Demo Enquiry & WhatsApp / Copy fallback
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
              text = "Choose Next Step",
              style = MaterialTheme.typography.titleMedium,
              color = CharcoalText,
            )

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              Button(
                onClick = {
                  val reference = onSaveDemoEnquiry()
                  if (reference != null) {
                    onEnquirySaved(reference)
                  } else {
                    actionMessage =
                      false to "Please review and fix the highlighted fields above."
                  }
                },
                enabled = !isSubmitting,
                shape = RoundedCornerShape(12.dp),
                colors =
                  ButtonDefaults.buttonColors(
                    containerColor = DeepMaroon,
                    contentColor = WarmIvory,
                  ),
                modifier =
                  Modifier.minimumInteractiveComponentSize().testTag("save_demo_enquiry_button"),
              ) {
                Text(if (isSubmitting) "Saving..." else "Save Demo Enquiry")
              }

              OutlinedButton(
                onClick = {
                  val (valid, messageOrError) = onBuildWhatsAppMessageIfValid()
                  if (!valid) {
                    actionMessage = false to messageOrError
                    return@OutlinedButton
                  }
                  val uriString = Formatters.buildWhatsAppUri(storeWhatsAppDigits, messageOrError)
                  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
                  runCatching { context.startActivity(intent) }
                    .onSuccess {
                      actionMessage =
                        true to
                          "WhatsApp opened with your prepared enquiry message. No order has been saved or paid."
                    }
                    .onFailure {
                      actionMessage =
                        false to
                          "Could not open WhatsApp on this device. Use Copy Enquiry Message below instead."
                    }
                },
                enabled = isWhatsAppConfigured,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isWhatsAppConfigured) DeepMaroon else SoftBorder),
                modifier =
                  Modifier.minimumInteractiveComponentSize().testTag("send_whatsapp_button"),
              ) {
                Icon(
                  Icons.Outlined.Chat,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Send via WhatsApp")
              }

              OutlinedButton(
                onClick = {
                  val (valid, messageOrError) = onBuildWhatsAppMessageIfValid()
                  if (!valid) {
                    actionMessage = false to messageOrError
                    return@OutlinedButton
                  }
                  val copied = copyTextToClipboard(context, messageOrError)
                  if (copied) {
                    actionMessage =
                      true to "Enquiry message copied to clipboard. You can paste and send it manually."
                  } else {
                    manualCopyText = messageOrError
                  }
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SoftBorder),
                modifier =
                  Modifier.minimumInteractiveComponentSize().testTag("copy_enquiry_message_button"),
              ) {
                Icon(
                  Icons.Outlined.ContentCopy,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy Enquiry Message", color = CharcoalText)
              }
            }

            if (!isWhatsAppConfigured) {
              Surface(
                color = WarningAmberSoft,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SoftBorder),
                modifier = Modifier.fillMaxWidth().testTag("whatsapp_unconfigured_notice"),
              ) {
                Column(
                  modifier = Modifier.padding(12.dp),
                  verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  Text(
                    text = "Store WhatsApp number has not been configured for this prototype.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = WarningAmber,
                  )
                  Text(
                    text =
                      "Use \"Copy Enquiry Message\" as a fallback, or configure a WhatsApp number in the Demo Dashboard.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalText,
                  )
                  TextButton(onClick = onOpenDashboard) {
                    Text("Configure in Demo Dashboard", color = DeepMaroon)
                  }
                }
              }
            } else {
              Text(
                text = "WhatsApp opens a prepared message. You must send it yourself.",
                style = MaterialTheme.typography.bodySmall,
                color = MutedCharcoalText,
              )
            }

            if (actionMessage != null) {
              val (ok, msg) = actionMessage!!
              Surface(
                color = if (ok) SuccessSageSoft else ErrorCrimsonSoft,
                shape = RoundedCornerShape(12.dp),
                modifier =
                  Modifier.fillMaxWidth()
                    .semantics { liveRegion = LiveRegionMode.Polite }
                    .testTag("enquiry_action_feedback"),
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Icon(
                    imageVector = if (ok) Icons.Outlined.CheckCircle else Icons.Outlined.Info,
                    contentDescription = null,
                    tint = if (ok) SuccessSage else ErrorCrimson,
                  )
                  Text(
                    text = msg,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (ok) SuccessSage else ErrorCrimson,
                  )
                }
              }
            }
          }
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }

  val fallbackText = manualCopyText
  if (fallbackText != null) {
    Dialog(
      onDismissRequest = { manualCopyText = null },
      properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
    ) {
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = ElevatedCardSurface,
        border = BorderStroke(1.dp, SoftBorder),
        modifier = Modifier.fillMaxWidth().widthIn(max = 520.dp),
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "Copy Enquiry Message",
              style = MaterialTheme.typography.titleLarge,
              color = DeepMaroon,
            )
            IconButton(onClick = { manualCopyText = null }) {
              Icon(Icons.Outlined.Close, contentDescription = "Close dialog")
            }
          }
          Text(
            text =
              "Automatic clipboard access was unavailable. Select and copy the prepared enquiry text below:",
            style = MaterialTheme.typography.bodySmall,
            color = MutedCharcoalText,
          )
          Surface(
            color = SoftCream,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, SoftBorder),
            modifier = Modifier.fillMaxWidth(),
          ) {
            SelectionContainer {
              Text(
                text = fallbackText,
                style = MaterialTheme.typography.bodySmall,
                color = CharcoalText,
                modifier = Modifier.padding(12.dp),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun EnquiryField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  error: String?,
  placeholder: String,
  tag: String,
  modifier: Modifier = Modifier,
  keyboardType: KeyboardType = KeyboardType.Text,
  singleLine: Boolean = true,
) {
  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelLarge,
      color = CharcoalText,
    )
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      placeholder = { Text(placeholder) },
      isError = error != null,
      supportingText =
        if (error != null) {
          { Text(text = error, color = ErrorCrimson) }
        } else null,
      singleLine = singleLine,
      minLines = if (singleLine) 1 else 2,
      keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
      shape = RoundedCornerShape(12.dp),
      colors =
        OutlinedTextFieldDefaults.colors(
          focusedBorderColor = DeepMaroon,
          unfocusedBorderColor = SoftBorder,
          errorBorderColor = ErrorCrimson,
        ),
      modifier = Modifier.fillMaxWidth().testTag(tag),
    )
  }
}

private fun copyTextToClipboard(context: Context, text: String): Boolean {
  return runCatching {
      val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        ?: return false
      clipboard.setPrimaryClip(ClipData.newPlainText("Riwaayat Enquiry Message", text))
      true
    }
    .getOrElse { false }
}
