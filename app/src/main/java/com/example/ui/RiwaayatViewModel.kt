package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.data.Formatters
import com.example.data.ProductCatalog
import com.example.data.RiwaayatStorage
import com.example.model.CartItem
import com.example.model.CatalogFilterState
import com.example.model.DemoEnquiryRecord
import com.example.model.EnquiryFormState
import com.example.model.EnquiryItemSnapshot
import com.example.model.PriceFilter
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.Region
import com.example.model.SortOption
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface BagActionResult {
  data class Added(val productName: String, val size: String, val color: String, val quantity: Int) :
    BagActionResult

  data class Error(val message: String) : BagActionResult
}

data class RiwaayatUiState(
  val allProducts: List<Product> = ProductCatalog.products,
  val filterState: CatalogFilterState = CatalogFilterState(),
  val wishlistIds: Set<String> = emptySet(),
  val bagItems: List<CartItem> = emptyList(),
  val savedEnquiries: List<DemoEnquiryRecord> = emptyList(),
  val storeWhatsAppDigits: String = "",
  val enquiryForm: EnquiryFormState = EnquiryFormState(),
  val isSubmittingEnquiry: Boolean = false,
) {
  val filteredProducts: List<Product>
    get() {
      val query = filterState.searchQuery.trim().lowercase()
      val filtered =
        allProducts.filter { product ->
          val matchesQuery =
            query.isEmpty() ||
              product.name.lowercase().contains(query) ||
              product.region.displayName.lowercase().contains(query) ||
              product.category.displayName.lowercase().contains(query) ||
              product.shortDescription.lowercase().contains(query)

          val matchesRegion =
            filterState.selectedRegion == null || product.region == filterState.selectedRegion

          val matchesCategory =
            filterState.selectedCategory == null ||
              product.category == filterState.selectedCategory

          val matchesPrice = filterState.selectedPriceFilter.matches(product.priceInr)

          matchesQuery && matchesRegion && matchesCategory && matchesPrice
        }

      return when (filterState.sortOption) {
        SortOption.FEATURED -> filtered.sortedByDescending { it.isFeatured }
        SortOption.PRICE_LOW_TO_HIGH -> filtered.sortedBy { it.priceInr }
        SortOption.PRICE_HIGH_TO_LOW -> filtered.sortedByDescending { it.priceInr }
        SortOption.NAME_A_TO_Z -> filtered.sortedBy { it.name.lowercase() }
      }
    }

  val wishlistProducts: List<Product>
    get() = allProducts.filter { wishlistIds.contains(it.id) }

  val wishlistCount: Int
    get() = wishlistIds.size

  val bagTotalQuantity: Int
    get() = bagItems.sumOf { it.quantity }

  val bagMerchandiseSubtotalInr: Int
    get() = bagItems.sumOf { it.lineTotalInr }

  val dashboardTotalUnitsRequested: Int
    get() = savedEnquiries.sumOf { it.totalUnits }

  val dashboardEstimatedEnquiryValueInr: Int
    get() = savedEnquiries.sumOf { it.estimatedMerchandiseTotalInr }
}

class RiwaayatViewModel(application: Application) : AndroidViewModel(application) {

  private val storage = RiwaayatStorage(application)

  private val _uiState =
    MutableStateFlow(
      RiwaayatUiState(
        wishlistIds = storage.loadWishlistIds(),
        bagItems = storage.loadBagItems(),
        savedEnquiries = storage.loadDemoEnquiries(),
        storeWhatsAppDigits = storage.loadStoreWhatsAppDigits(),
      )
    )
  val uiState: StateFlow<RiwaayatUiState> = _uiState.asStateFlow()

  fun updateSearchQuery(query: String) {
    _uiState.update { state ->
      state.copy(filterState = state.filterState.copy(searchQuery = query))
    }
  }

  fun selectRegionFilter(region: Region?) {
    _uiState.update { state ->
      state.copy(filterState = state.filterState.copy(selectedRegion = region))
    }
  }

  fun openRegionInExplore(region: Region) {
    _uiState.update { state ->
      state.copy(
        filterState =
          CatalogFilterState(
            searchQuery = "",
            selectedRegion = region,
            selectedCategory = null,
            selectedPriceFilter = PriceFilter.ALL,
            sortOption = SortOption.FEATURED,
          )
      )
    }
  }

  fun selectCategoryFilter(category: ProductCategory?) {
    _uiState.update { state ->
      state.copy(filterState = state.filterState.copy(selectedCategory = category))
    }
  }

  fun selectPriceFilter(priceFilter: PriceFilter) {
    _uiState.update { state ->
      state.copy(filterState = state.filterState.copy(selectedPriceFilter = priceFilter))
    }
  }

  fun selectSortOption(sortOption: SortOption) {
    _uiState.update { state ->
      state.copy(filterState = state.filterState.copy(sortOption = sortOption))
    }
  }

  fun clearAllFilters() {
    _uiState.update { state -> state.copy(filterState = CatalogFilterState()) }
  }

  fun toggleWishlist(productId: String) {
    if (ProductCatalog.findProductById(productId) == null) return
    _uiState.update { state ->
      val updated = state.wishlistIds.toMutableSet()
      if (!updated.add(productId)) {
        updated.remove(productId)
      }
      storage.saveWishlistIds(updated)
      state.copy(wishlistIds = updated)
    }
  }

  fun addToBag(
    product: Product,
    selectedSize: String?,
    selectedColor: String?,
    quantity: Int,
  ): BagActionResult {
    val resolvedSize =
      if (product.category.requiresSizeSelection) {
        selectedSize?.takeIf { it in product.availableSizes }
          ?: return BagActionResult.Error("Please select a size before adding to bag.")
      } else {
        Product.ONE_SIZE
      }

    val resolvedColor =
      selectedColor?.takeIf { colorName ->
        product.availableColors.any { it.name == colorName }
      } ?: return BagActionResult.Error("Please select a color before adding to bag.")

    if (quantity <= 0) {
      return BagActionResult.Error("Quantity must be at least 1.")
    }

    val availableStock = product.stockFor(resolvedSize, resolvedColor)
    if (availableStock <= 0) {
      return BagActionResult.Error("This variant is currently unavailable in demo stock.")
    }

    val currentBag = _uiState.value.bagItems.toMutableList()
    val lineId = "${product.id}|$resolvedSize|$resolvedColor"
    val existingIndex = currentBag.indexOfFirst { it.lineId == lineId }

    if (existingIndex >= 0) {
      val existing = currentBag[existingIndex]
      val combinedQuantity = existing.quantity + quantity
      if (combinedQuantity > availableStock) {
        val remaining = (availableStock - existing.quantity).coerceAtLeast(0)
        return BagActionResult.Error(
          if (remaining == 0) {
            "Maximum demo stock ($availableStock) for this variant is already in your bag."
          } else {
            "Only $remaining more unit(s) available in demo stock for this variant."
          }
        )
      }
      currentBag[existingIndex] = existing.copy(quantity = combinedQuantity)
    } else {
      if (quantity > availableStock) {
        return BagActionResult.Error(
          "Only $availableStock unit(s) available in demo stock for this variant."
        )
      }
      currentBag.add(
        CartItem(
          productId = product.id,
          productName = product.name,
          region = product.region,
          category = product.category,
          size = resolvedSize,
          color = resolvedColor,
          unitPriceInr = product.priceInr,
          quantity = quantity,
        )
      )
    }

    storage.saveBagItems(currentBag)
    _uiState.update { it.copy(bagItems = currentBag) }
    return BagActionResult.Added(
      productName = product.name,
      size = resolvedSize,
      color = resolvedColor,
      quantity = quantity,
    )
  }

  fun updateBagQuantity(lineId: String, delta: Int) {
    val currentBag = _uiState.value.bagItems.toMutableList()
    val index = currentBag.indexOfFirst { it.lineId == lineId }
    if (index < 0) return

    val item = currentBag[index]
    val product = ProductCatalog.findProductById(item.productId)
    val maxStock = product?.stockFor(item.size, item.color) ?: item.quantity
    val nextQty = item.quantity + delta

    if (nextQty <= 0) {
      currentBag.removeAt(index)
    } else if (nextQty <= maxStock) {
      currentBag[index] = item.copy(quantity = nextQty)
    } else {
      return
    }

    storage.saveBagItems(currentBag)
    _uiState.update { it.copy(bagItems = currentBag) }
  }

  fun removeBagItem(lineId: String) {
    val updated = _uiState.value.bagItems.filterNot { it.lineId == lineId }
    storage.saveBagItems(updated)
    _uiState.update { it.copy(bagItems = updated) }
  }

  fun updateEnquiryForm(transform: (EnquiryFormState) -> EnquiryFormState) {
    _uiState.update { state -> state.copy(enquiryForm = transform(state.enquiryForm)) }
  }

  fun validateEnquiryFormNow(): Boolean {
    val currentForm = _uiState.value.enquiryForm
    val errors = Formatters.validateEnquiryForm(currentForm)
    _uiState.update { it.copy(enquiryForm = currentForm.copy(errors = errors)) }
    return !errors.hasErrors
  }

  /**
   * Saves a non-sensitive local demo enquiry snapshot, clears the bag only after save succeeds,
   * resets the in-memory form, and returns the generated reference ID.
   * Guarded against rapid double submissions.
   */
  fun saveDemoEnquiry(): String? {
    val state = _uiState.value
    if (state.isSubmittingEnquiry || state.bagItems.isEmpty()) return null

    val errors = Formatters.validateEnquiryForm(state.enquiryForm)
    if (errors.hasErrors) {
      _uiState.update { it.copy(enquiryForm = it.enquiryForm.copy(errors = errors)) }
      return null
    }

    _uiState.update { it.copy(isSubmittingEnquiry = true) }

    val now = System.currentTimeMillis()
    val uniqueSuffix = UUID.randomUUID().toString().substring(0, 6).uppercase()
    val reference = "RWY-$uniqueSuffix"

    val snapshots =
      state.bagItems.map { item ->
        EnquiryItemSnapshot(
          productId = item.productId,
          productName = item.productName,
          regionName = item.region.displayName,
          size = item.size,
          color = item.color,
          unitPriceInr = item.unitPriceInr,
          quantity = item.quantity,
          lineTotalInr = item.lineTotalInr,
        )
      }

    val record =
      DemoEnquiryRecord(
        reference = reference,
        createdAtDisplay = Formatters.formatTimestamp(now),
        createdAtEpochMillis = now,
        items = snapshots,
        totalUnits = snapshots.sumOf { it.quantity },
        estimatedMerchandiseTotalInr = snapshots.sumOf { it.lineTotalInr },
        status = "Saved locally — not submitted to a seller",
        isSample = false,
      )

    val updatedEnquiries = listOf(record) + state.savedEnquiries
    storage.saveDemoEnquiries(updatedEnquiries)
    storage.saveBagItems(emptyList())

    _uiState.update {
      it.copy(
        savedEnquiries = updatedEnquiries,
        bagItems = emptyList(),
        enquiryForm = EnquiryFormState(),
        isSubmittingEnquiry = false,
      )
    }

    return reference
  }

  fun findEnquiryByReference(reference: String): DemoEnquiryRecord? =
    _uiState.value.savedEnquiries.firstOrNull { it.reference == reference }

  fun buildWhatsAppMessageIfValid(): Pair<Boolean, String> {
    val state = _uiState.value
    if (state.bagItems.isEmpty()) {
      return false to "Your bag is empty."
    }
    val errors = Formatters.validateEnquiryForm(state.enquiryForm)
    _uiState.update { it.copy(enquiryForm = it.enquiryForm.copy(errors = errors)) }
    if (errors.hasErrors) {
      return false to "Please complete all required fields before preparing the enquiry message."
    }
    val message =
      Formatters.buildWhatsAppEnquiryMessage(
        form = state.enquiryForm,
        items = state.bagItems,
        merchandiseSubtotalInr = state.bagMerchandiseSubtotalInr,
      )
    return true to message
  }

  fun updateStoreWhatsAppNumber(rawInput: String): Pair<Boolean, String> {
    val trimmed = rawInput.trim()
    if (trimmed.isEmpty()) {
      storage.saveStoreWhatsAppDigits("")
      _uiState.update { it.copy(storeWhatsAppDigits = "") }
      return true to "Store WhatsApp number cleared."
    }
    val normalized = Formatters.normalizeStoreWhatsAppDigits(trimmed)
    if (normalized == null) {
      return false to
        "Enter a valid 10-digit Indian mobile number or 11–15 digit WhatsApp number with country code."
    }
    storage.saveStoreWhatsAppDigits(normalized)
    _uiState.update { it.copy(storeWhatsAppDigits = normalized) }
    return true to "Store WhatsApp number saved (+$normalized)."
  }

  fun loadSampleEnquiries() {
    _uiState.update { state ->
      val existingRefs = state.savedEnquiries.map { it.reference }.toSet()
      val samplesToAdd =
        ProductCatalog.createSampleEnquiries().filterNot { existingRefs.contains(it.reference) }
      if (samplesToAdd.isEmpty()) return@update state
      val merged = (state.savedEnquiries + samplesToAdd).sortedByDescending { it.createdAtEpochMillis }
      storage.saveDemoEnquiries(merged)
      state.copy(savedEnquiries = merged)
    }
  }

  fun resetAllDemoData() {
    storage.clearAllDemoData()
    _uiState.value = RiwaayatUiState()
  }
}
