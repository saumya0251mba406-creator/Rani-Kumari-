package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FilterListOff
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import com.example.model.CatalogFilterState
import com.example.model.PriceFilter
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.Region
import com.example.model.SortOption
import com.example.ui.components.ProductCard
import com.example.ui.components.RiwaayatFooter
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.DeepMaroonSoft
import com.example.ui.theme.ElevatedCardSurface
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
  filteredProducts: List<Product>,
  totalCatalogCount: Int,
  filterState: CatalogFilterState,
  wishlistIds: Set<String>,
  onSearchQueryChange: (String) -> Unit,
  onSelectRegion: (Region?) -> Unit,
  onSelectCategory: (ProductCategory?) -> Unit,
  onSelectPriceFilter: (PriceFilter) -> Unit,
  onSelectSortOption: (SortOption) -> Unit,
  onClearFilters: () -> Unit,
  onToggleWishlist: (String) -> Unit,
  onViewProduct: (String) -> Unit,
  onNavigateFooter: (String) -> Unit,
) {
  var sortExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().widthIn(max = 1040.dp).padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Explore Collection",
        style = MaterialTheme.typography.headlineLarge,
        color = CharcoalText,
        modifier = Modifier.semantics { heading() },
      )
      Text(
        text =
          "Discover regional silhouettes, filter by craft region or budget, and view full product specifications.",
        style = MaterialTheme.typography.bodyMedium,
        color = MutedCharcoalText,
      )

      // Search input
      OutlinedTextField(
        value = filterState.searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = { Text("Search by product name, region, or category") },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
        trailingIcon = {
          if (filterState.searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchQueryChange("") }) {
              Icon(Icons.Outlined.Clear, contentDescription = "Clear search text")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors =
          OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DeepMaroon,
            unfocusedBorderColor = SoftBorder,
            focusedContainerColor = ElevatedCardSurface,
            unfocusedContainerColor = ElevatedCardSurface,
          ),
        modifier = Modifier.fillMaxWidth().testTag("explore_search_input"),
      )

      // Region filters
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = "Region",
          style = MaterialTheme.typography.labelLarge,
          color = CharcoalText,
        )
        Row(
          modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          FilterChip(
            selected = filterState.selectedRegion == null,
            onClick = { onSelectRegion(null) },
            label = { Text("All Regions") },
            colors = chipColors(),
          )
          Region.entries.forEach { region ->
            FilterChip(
              selected = filterState.selectedRegion == region,
              onClick = {
                onSelectRegion(if (filterState.selectedRegion == region) null else region)
              },
              label = { Text(region.displayName) },
              colors = chipColors(),
              modifier = Modifier.testTag("filter_region_${region.name.lowercase()}"),
            )
          }
        }
      }

      // Category filters
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = "Category",
          style = MaterialTheme.typography.labelLarge,
          color = CharcoalText,
        )
        Row(
          modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          FilterChip(
            selected = filterState.selectedCategory == null,
            onClick = { onSelectCategory(null) },
            label = { Text("All Categories") },
            colors = chipColors(),
          )
          ProductCategory.entries.forEach { category ->
            FilterChip(
              selected = filterState.selectedCategory == category,
              onClick = {
                onSelectCategory(if (filterState.selectedCategory == category) null else category)
              },
              label = { Text(category.displayName) },
              colors = chipColors(),
              modifier = Modifier.testTag("filter_category_${category.name.lowercase()}"),
            )
          }
        }
      }

      // Price filters
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = "Price Range",
          style = MaterialTheme.typography.labelLarge,
          color = CharcoalText,
        )
        Row(
          modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          PriceFilter.entries.forEach { priceFilter ->
            FilterChip(
              selected = filterState.selectedPriceFilter == priceFilter,
              onClick = { onSelectPriceFilter(priceFilter) },
              label = { Text(priceFilter.label) },
              colors = chipColors(),
              modifier = Modifier.testTag("filter_price_${priceFilter.name.lowercase()}"),
            )
          }
        }
      }

      // Results count, Sort dropdown, and Clear Filters
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Text(
          text = "Showing ${filteredProducts.size} of $totalCatalogCount pieces",
          style = MaterialTheme.typography.titleSmall,
          color = DeepMaroon,
          modifier =
            Modifier.align(Alignment.CenterVertically)
              .semantics { liveRegion = LiveRegionMode.Polite }
              .testTag("explore_result_count"),
        )

        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          if (filterState.hasActiveFilters) {
            TextButton(
              onClick = onClearFilters,
              modifier = Modifier.testTag("explore_clear_filters_button"),
            ) {
              Text("Clear all filters", color = DeepMaroon)
            }
          }

          BoxWrapperForSortMenu(
            selectedSort = filterState.sortOption,
            expanded = sortExpanded,
            onToggle = { sortExpanded = !sortExpanded },
            onDismiss = { sortExpanded = false },
            onSelect = {
              onSelectSortOption(it)
              sortExpanded = false
            },
          )
        }
      }

      // Product Grid or Empty State
      if (filteredProducts.isEmpty()) {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = ElevatedCardSurface),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.fillMaxWidth().testTag("explore_empty_state"),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Icon(
              imageVector = Icons.Outlined.FilterListOff,
              contentDescription = null,
              tint = DeepMaroon,
            )
            Text(
              text = "No products match your selection.",
              style = MaterialTheme.typography.titleLarge,
              color = CharcoalText,
            )
            Text(
              text = "Try clearing one or more filters to browse the full curated collection.",
              style = MaterialTheme.typography.bodyMedium,
              color = MutedCharcoalText,
            )
            Button(
              onClick = onClearFilters,
              shape = RoundedCornerShape(12.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = DeepMaroon,
                  contentColor = WarmIvory,
                ),
              modifier = Modifier.minimumInteractiveComponentSize().testTag("empty_clear_filters"),
            ) {
              Text("Clear filters")
            }
          }
        }
      } else {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
          val columns = if (maxWidth >= 820.dp) 3 else if (maxWidth >= 560.dp) 2 else 1
          val rows = filteredProducts.chunked(columns)
          Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            rows.forEach { rowProducts ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
              ) {
                rowProducts.forEach { product ->
                  ProductCard(
                    product = product,
                    isWishlisted = wishlistIds.contains(product.id),
                    onToggleWishlist = { onToggleWishlist(product.id) },
                    onViewDetails = { onViewProduct(product.id) },
                    modifier = Modifier.weight(1f),
                  )
                }
                repeat(columns - rowProducts.size) { Spacer(modifier = Modifier.weight(1f)) }
              }
            }
          }
        }
      }
    }

    RiwaayatFooter(onNavigate = onNavigateFooter)
  }
}

@Composable
private fun BoxWrapperForSortMenu(
  selectedSort: SortOption,
  expanded: Boolean,
  onToggle: () -> Unit,
  onDismiss: () -> Unit,
  onSelect: (SortOption) -> Unit,
) {
  androidx.compose.foundation.layout.Box {
    OutlinedButton(
      onClick = onToggle,
      shape = RoundedCornerShape(12.dp),
      border = BorderStroke(1.dp, SoftBorder),
      modifier = Modifier.minimumInteractiveComponentSize().testTag("explore_sort_button"),
    ) {
      Icon(Icons.Outlined.SwapVert, contentDescription = null, tint = DeepMaroon)
      Text(
        text = "Sort: ${selectedSort.label}",
        color = CharcoalText,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(start = 6.dp),
      )
    }
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
      SortOption.entries.forEach { option ->
        DropdownMenuItem(
          text = { Text(option.label) },
          onClick = { onSelect(option) },
          modifier = Modifier.testTag("sort_option_${option.name.lowercase()}"),
        )
      }
    }
  }
}

@Composable
private fun chipColors() =
  FilterChipDefaults.filterChipColors(
    selectedContainerColor = DeepMaroonSoft,
    selectedLabelColor = DeepMaroon,
    containerColor = ElevatedCardSurface,
    labelColor = CharcoalText,
  )
