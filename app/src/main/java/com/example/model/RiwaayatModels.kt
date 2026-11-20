package com.example.model

enum class Region(
  val displayName: String,
  val subtitle: String,
  val motifLabel: String,
) {
  BIHAR(
    displayName = "Bihar",
    subtitle = "Initial launch market",
    motifLabel = "Mithila & Bhagalpur Motifs",
  ),
  UTTAR_PRADESH(
    displayName = "Uttar Pradesh",
    subtitle = "Planned expansion",
    motifLabel = "Banaras & Lucknow Motifs",
  ),
  PUNJAB(
    displayName = "Punjab",
    subtitle = "Planned expansion",
    motifLabel = "Phulkari Stitch Geometry",
  ),
  UTTARAKHAND(
    displayName = "Uttarakhand",
    subtitle = "Planned expansion",
    motifLabel = "Aipan Sacred Patterns",
  ),
  TELANGANA(
    displayName = "Telangana",
    subtitle = "Planned expansion",
    motifLabel = "Pochampally Diamond Grid",
  ),
}

enum class ProductCategory(
  val displayName: String,
  val requiresSizeSelection: Boolean,
) {
  KURTAS("Kurtas", requiresSizeSelection = true),
  DUPATTAS("Dupattas", requiresSizeSelection = false),
  STOLES("Stoles", requiresSizeSelection = false),
  SETS("Sets", requiresSizeSelection = true),
}

enum class PriceFilter(val label: String) {
  ALL("All Prices"),
  UNDER_2000("Under ₹2,000"),
  BETWEEN_2000_2500("₹2,000 – ₹2,500"),
  ABOVE_2500("Above ₹2,500");

  fun matches(priceInr: Int): Boolean =
    when (this) {
      ALL -> true
      UNDER_2000 -> priceInr < 2000
      BETWEEN_2000_2500 -> priceInr in 2000..2500
      ABOVE_2500 -> priceInr > 2500
    }
}

enum class SortOption(val label: String) {
  FEATURED("Featured"),
  PRICE_LOW_TO_HIGH("Price: Low to High"),
  PRICE_HIGH_TO_LOW("Price: High to Low"),
  NAME_A_TO_Z("Name: A–Z"),
}

enum class TextileMotif {
  MITHILA_LOTUS,
  BHAGALPUR_WEAVE,
  BANARAS_BROCADE,
  LUCKNOW_TRELLIS,
  PHULKARI_DIAMOND,
  AIPAN_GEOMETRY,
  POCHAMPALLY_IKAT,
  REGIONAL_FUSION,
}

data class ProductColorOption(
  val name: String,
  val swatchHex: Long,
)

data class ProductVariantKey(
  val size: String,
  val color: String,
)

data class Product(
  val id: String,
  val name: String,
  val region: Region,
  val category: ProductCategory,
  val priceInr: Int,
  val shortDescription: String,
  val fullDescription: String,
  val illustrativeMaterial: String,
  val availableSizes: List<String>,
  val availableColors: List<ProductColorOption>,
  val variantStock: Map<ProductVariantKey, Int>,
  val careInstructions: String,
  val deliveryNote: String,
  val returnsNote: String,
  val originStatus: String,
  val designInspiration: String,
  val specificationNote: String,
  val altText: String,
  val motif: TextileMotif,
  val isFeatured: Boolean = false,
) {
  fun stockFor(size: String, color: String): Int =
    variantStock[ProductVariantKey(size = size, color = color)] ?: 0

  fun totalDemoStock(): Int = variantStock.values.sum()

  fun defaultVariant(): ProductVariantKey {
    val firstInStock = variantStock.entries.firstOrNull { it.value > 0 }?.key
    return firstInStock
      ?: ProductVariantKey(
        size = availableSizes.firstOrNull() ?: ONE_SIZE,
        color = availableColors.firstOrNull()?.name ?: "",
      )
  }

  companion object {
    const val ONE_SIZE = "One Size"
  }
}

data class CartItem(
  val productId: String,
  val productName: String,
  val region: Region,
  val category: ProductCategory,
  val size: String,
  val color: String,
  val unitPriceInr: Int,
  val quantity: Int,
) {
  val lineId: String
    get() = "$productId|$size|$color"

  val lineTotalInr: Int
    get() = unitPriceInr * quantity
}

data class EnquiryItemSnapshot(
  val productId: String,
  val productName: String,
  val regionName: String,
  val size: String,
  val color: String,
  val unitPriceInr: Int,
  val quantity: Int,
  val lineTotalInr: Int,
)

data class DemoEnquiryRecord(
  val reference: String,
  val createdAtDisplay: String,
  val createdAtEpochMillis: Long,
  val items: List<EnquiryItemSnapshot>,
  val totalUnits: Int,
  val estimatedMerchandiseTotalInr: Int,
  val status: String,
  val isSample: Boolean = false,
)

data class EnquiryFormErrors(
  val fullName: String? = null,
  val mobileNumber: String? = null,
  val city: String? = null,
  val state: String? = null,
  val pincode: String? = null,
  val email: String? = null,
  val acknowledged: String? = null,
) {
  val hasErrors: Boolean
    get() =
      fullName != null ||
        mobileNumber != null ||
        city != null ||
        state != null ||
        pincode != null ||
        email != null ||
        acknowledged != null
}

data class EnquiryFormState(
  val fullName: String = "",
  val mobileNumber: String = "",
  val city: String = "",
  val state: String = "",
  val pincode: String = "",
  val email: String = "",
  val deliveryNote: String = "",
  val acknowledged: Boolean = false,
  val errors: EnquiryFormErrors = EnquiryFormErrors(),
)

data class CatalogFilterState(
  val searchQuery: String = "",
  val selectedRegion: Region? = null,
  val selectedCategory: ProductCategory? = null,
  val selectedPriceFilter: PriceFilter = PriceFilter.ALL,
  val sortOption: SortOption = SortOption.FEATURED,
) {
  val hasActiveFilters: Boolean
    get() =
      searchQuery.isNotBlank() ||
        selectedRegion != null ||
        selectedCategory != null ||
        selectedPriceFilter != PriceFilter.ALL ||
        sortOption != SortOption.FEATURED
}

data class RegionalStory(
  val region: Region,
  val title: String,
  val craftFocus: String,
  val summary: String,
  val prototypeNote: String,
  val designHighlights: List<String>,
  val motif: TextileMotif,
)
