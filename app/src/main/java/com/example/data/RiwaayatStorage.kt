package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.CartItem
import com.example.model.DemoEnquiryRecord
import com.example.model.EnquiryItemSnapshot
import com.example.model.ProductCategory
import com.example.model.Region
import org.json.JSONArray
import org.json.JSONObject

class RiwaayatStorage(context: Context) {

  private val prefs: SharedPreferences =
    context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

  fun loadWishlistIds(): Set<String> {
    val raw = prefs.getString(KEY_WISHLIST, null) ?: return emptySet()
    return runCatching {
        val array = JSONArray(raw)
        val result = LinkedHashSet<String>()
        for (i in 0 until array.length()) {
          val id = array.optString(i, "").trim()
          if (id.isNotEmpty() && ProductCatalog.findProductById(id) != null) {
            result.add(id)
          }
        }
        result
      }
      .getOrElse { emptySet() }
  }

  fun saveWishlistIds(ids: Set<String>) {
    val array = JSONArray()
    ids.forEach { array.put(it) }
    prefs.edit().putString(KEY_WISHLIST, array.toString()).apply()
  }

  fun loadBagItems(): List<CartItem> {
    val raw = prefs.getString(KEY_BAG, null) ?: return emptyList()
    return runCatching {
        val array = JSONArray(raw)
        val items = mutableListOf<CartItem>()
        for (i in 0 until array.length()) {
          val obj = array.optJSONObject(i) ?: continue
          val productId = obj.optString("productId", "").trim()
          val product = ProductCatalog.findProductById(productId) ?: continue
          val size = obj.optString("size", "").trim()
          val color = obj.optString("color", "").trim()
          val quantity = obj.optInt("quantity", 0)
          if (size.isEmpty() || color.isEmpty() || quantity <= 0) continue

          val maxStock = product.stockFor(size, color)
          if (maxStock <= 0) continue
          val clampedQty = quantity.coerceAtMost(maxStock)

          items.add(
            CartItem(
              productId = product.id,
              productName = product.name,
              region = product.region,
              category = product.category,
              size = size,
              color = color,
              unitPriceInr = product.priceInr,
              quantity = clampedQty,
            )
          )
        }
        items
      }
      .getOrElse { emptyList() }
  }

  fun saveBagItems(items: List<CartItem>) {
    val array = JSONArray()
    items.forEach { item ->
      val obj =
        JSONObject()
          .put("productId", item.productId)
          .put("productName", item.productName)
          .put("region", item.region.name)
          .put("category", item.category.name)
          .put("size", item.size)
          .put("color", item.color)
          .put("unitPriceInr", item.unitPriceInr)
          .put("quantity", item.quantity)
      array.put(obj)
    }
    prefs.edit().putString(KEY_BAG, array.toString()).apply()
  }

  /**
   * Loads saved non-sensitive demo enquiries. Intentionally ignores any phone/address/email fields
   * even if present from older schemas so sensitive customer PII is never exposed or retained.
   */
  fun loadDemoEnquiries(): List<DemoEnquiryRecord> {
    val raw = prefs.getString(KEY_ENQUIRIES, null) ?: return emptyList()
    return runCatching {
        val array = JSONArray(raw)
        val records = mutableListOf<DemoEnquiryRecord>()
        for (i in 0 until array.length()) {
          val obj = array.optJSONObject(i) ?: continue
          val reference = obj.optString("reference", "").trim()
          val createdAtDisplay = obj.optString("createdAtDisplay", "").trim()
          val createdAtEpochMillis = obj.optLong("createdAtEpochMillis", 0L)
          val totalUnits = obj.optInt("totalUnits", 0)
          val estimatedTotal = obj.optInt("estimatedMerchandiseTotalInr", 0)
          val status = obj.optString("status", "Saved locally — not submitted to a seller")
          val isSample = obj.optBoolean("isSample", false)

          if (reference.isEmpty() || totalUnits <= 0) continue

          val itemsArray = obj.optJSONArray("items") ?: JSONArray()
          val snapshots = mutableListOf<EnquiryItemSnapshot>()
          for (j in 0 until itemsArray.length()) {
            val itemObj = itemsArray.optJSONObject(j) ?: continue
            val pId = itemObj.optString("productId", "")
            val pName = itemObj.optString("productName", "")
            val rName = itemObj.optString("regionName", "")
            val size = itemObj.optString("size", "")
            val color = itemObj.optString("color", "")
            val unitPrice = itemObj.optInt("unitPriceInr", 0)
            val qty = itemObj.optInt("quantity", 0)
            val lineTotal = itemObj.optInt("lineTotalInr", unitPrice * qty)
            if (pName.isNotEmpty() && qty > 0) {
              snapshots.add(
                EnquiryItemSnapshot(
                  productId = pId,
                  productName = pName,
                  regionName = rName,
                  size = size,
                  color = color,
                  unitPriceInr = unitPrice,
                  quantity = qty,
                  lineTotalInr = lineTotal,
                )
              )
            }
          }
          if (snapshots.isEmpty()) continue

          records.add(
            DemoEnquiryRecord(
              reference = reference,
              createdAtDisplay =
                createdAtDisplay.ifEmpty { Formatters.formatTimestamp(createdAtEpochMillis) },
              createdAtEpochMillis = createdAtEpochMillis,
              items = snapshots,
              totalUnits = snapshots.sumOf { it.quantity },
              estimatedMerchandiseTotalInr =
                if (estimatedTotal > 0) estimatedTotal else snapshots.sumOf { it.lineTotalInr },
              status = status,
              isSample = isSample,
            )
          )
        }
        records.sortedByDescending { it.createdAtEpochMillis }
      }
      .getOrElse { emptyList() }
  }

  fun saveDemoEnquiries(records: List<DemoEnquiryRecord>) {
    val array = JSONArray()
    records.forEach { rec ->
      val itemsArray = JSONArray()
      rec.items.forEach { item ->
        itemsArray.put(
          JSONObject()
            .put("productId", item.productId)
            .put("productName", item.productName)
            .put("regionName", item.regionName)
            .put("size", item.size)
            .put("color", item.color)
            .put("unitPriceInr", item.unitPriceInr)
            .put("quantity", item.quantity)
            .put("lineTotalInr", item.lineTotalInr)
        )
      }
      array.put(
        JSONObject()
          .put("reference", rec.reference)
          .put("createdAtDisplay", rec.createdAtDisplay)
          .put("createdAtEpochMillis", rec.createdAtEpochMillis)
          .put("items", itemsArray)
          .put("totalUnits", rec.totalUnits)
          .put("estimatedMerchandiseTotalInr", rec.estimatedMerchandiseTotalInr)
          .put("status", rec.status)
          .put("isSample", rec.isSample)
      )
    }
    prefs.edit().putString(KEY_ENQUIRIES, array.toString()).apply()
  }

  fun loadStoreWhatsAppDigits(): String {
    val raw = prefs.getString(KEY_STORE_WHATSAPP, "") ?: ""
    return Formatters.normalizeStoreWhatsAppDigits(raw) ?: ""
  }

  fun saveStoreWhatsAppDigits(digits: String) {
    prefs.edit().putString(KEY_STORE_WHATSAPP, digits).apply()
  }

  fun clearAllDemoData() {
    prefs.edit()
      .remove(KEY_WISHLIST)
      .remove(KEY_BAG)
      .remove(KEY_ENQUIRIES)
      .remove(KEY_STORE_WHATSAPP)
      .apply()
  }

  companion object {
    const val PREFS_NAME = "riwaayat_prototype_storage"
    const val KEY_WISHLIST = "wishlist_ids"
    const val KEY_BAG = "bag_items"
    const val KEY_ENQUIRIES = "demo_enquiries"
    const val KEY_STORE_WHATSAPP = "store_whatsapp_number"
  }
}
