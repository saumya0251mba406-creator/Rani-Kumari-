package com.example.data

import com.example.model.CartItem
import com.example.model.EnquiryFormErrors
import com.example.model.EnquiryFormState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {
  private val inrLocale = Locale.Builder().setLanguage("en").setRegion("IN").build()

  fun formatInr(amount: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(inrLocale)
    formatter.maximumFractionDigits = 0
    formatter.minimumFractionDigits = 0
    return formatter.format(amount)
  }

  fun formatTimestamp(epochMillis: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", inrLocale)
    return sdf.format(Date(epochMillis))
  }

  /**
   * Normalizes a standard 10-digit Indian mobile number (with optional +91, 91, or leading 0)
   * into "+91 XXXXXXXXXX". Returns null if invalid.
   */
  fun normalizeIndianMobile(raw: String): String? {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return null
    val digitsOnly = trimmed.filter { it.isDigit() }
    val tenDigits =
      when {
        digitsOnly.length == 10 -> digitsOnly
        digitsOnly.length == 11 && digitsOnly.startsWith("0") -> digitsOnly.substring(1)
        digitsOnly.length == 12 && digitsOnly.startsWith("91") -> digitsOnly.substring(2)
        else -> return null
      }
    if (!tenDigits.matches(Regex("^[6-9][0-9]{9}$"))) return null
    return "+91 $tenDigits"
  }

  fun isValidPincode(raw: String): Boolean {
    val trimmed = raw.trim()
    return trimmed.matches(Regex("^[1-9][0-9]{5}$"))
  }

  fun isValidOptionalEmail(raw: String): Boolean {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return true
    return trimmed.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
  }

  /**
   * Validates and normalizes the store WhatsApp number to digits only for wa.me links.
   * Accepts:
   * - 10-digit Indian mobile (normalized to 91XXXXXXXXXX)
   * - +91XXXXXXXXXX / 91XXXXXXXXXX
   * - International digits-only E.164 (11 to 15 digits, cannot start with 0)
   */
  fun normalizeStoreWhatsAppDigits(raw: String): String? {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return null
    val digitsOnly = trimmed.filter { it.isDigit() }
    return when {
      digitsOnly.matches(Regex("^[6-9][0-9]{9}$")) -> "91$digitsOnly"
      digitsOnly.matches(Regex("^91[6-9][0-9]{9}$")) -> digitsOnly
      digitsOnly.matches(Regex("^[1-9][0-9]{10,14}$")) -> digitsOnly
      else -> null
    }
  }

  fun validateEnquiryForm(form: EnquiryFormState): EnquiryFormErrors {
    val nameError =
      if (form.fullName.trim().length < 2) {
        "Please enter your full name."
      } else {
        null
      }

    val mobileError =
      if (form.mobileNumber.isBlank()) {
        "Please enter a 10-digit Indian mobile number."
      } else if (normalizeIndianMobile(form.mobileNumber) == null) {
        "Enter a valid 10-digit Indian mobile number (starting with 6–9, optional +91)."
      } else {
        null
      }

    val cityError = if (form.city.trim().isEmpty()) "Please enter your city." else null
    val stateError = if (form.state.trim().isEmpty()) "Please enter your state." else null

    val pincodeError =
      if (form.pincode.isBlank()) {
        "Please enter a 6-digit pincode."
      } else if (!isValidPincode(form.pincode)) {
        "Pincode must be 6 digits and cannot begin with 0."
      } else {
        null
      }

    val emailError =
      if (!isValidOptionalEmail(form.email)) {
        "Enter a valid email address or leave this field blank."
      } else {
        null
      }

    val ackError =
      if (!form.acknowledged) {
        "Please confirm that you understand this is a prototype enquiry."
      } else {
        null
      }

    return EnquiryFormErrors(
      fullName = nameError,
      mobileNumber = mobileError,
      city = cityError,
      state = stateError,
      pincode = pincodeError,
      email = emailError,
      acknowledged = ackError,
    )
  }

  fun buildWhatsAppEnquiryMessage(
    form: EnquiryFormState,
    items: List<CartItem>,
    merchandiseSubtotalInr: Int,
  ): String {
    val normalizedPhone = normalizeIndianMobile(form.mobileNumber) ?: form.mobileNumber.trim()
    val sb = StringBuilder()
    sb.appendLine("Riwaayat Order Enquiry (Prototype)")
    sb.appendLine("--------------------------------")
    sb.appendLine("Customer Name: ${form.fullName.trim()}")
    sb.appendLine("Contact Number: $normalizedPhone")
    sb.appendLine(
      "Location: ${form.city.trim()}, ${form.state.trim()} - ${form.pincode.trim()}"
    )
    if (form.email.trim().isNotEmpty()) {
      sb.appendLine("Email: ${form.email.trim()}")
    }
    sb.appendLine()
    sb.appendLine("Requested Items:")
    items.forEachIndexed { index, item ->
      sb.appendLine(
        "${index + 1}. ${item.productName} (${item.size}, ${item.color}) x ${item.quantity} @ ${formatInr(item.unitPriceInr)} = ${formatInr(item.lineTotalInr)}"
      )
    }
    sb.appendLine()
    sb.appendLine("Merchandise Subtotal: ${formatInr(merchandiseSubtotalInr)}")
    sb.appendLine("Shipping and final payment amount to be confirmed.")
    if (form.deliveryNote.trim().isNotEmpty()) {
      sb.appendLine("Customer Note: ${form.deliveryNote.trim()}")
    }
    return sb.toString().trim()
  }

  fun buildWhatsAppUri(storeDigits: String, message: String): String {
    val cleanDigits = storeDigits.filter { it.isDigit() }
    val encoded = URLEncoder.encode(message, StandardCharsets.UTF_8.toString()).replace("+", "%20")
    return "https://wa.me/$cleanDigits?text=$encoded"
  }
}
