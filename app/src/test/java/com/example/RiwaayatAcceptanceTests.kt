package com.example

import android.app.Application
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import com.example.data.Formatters
import com.example.data.ProductCatalog
import com.example.data.RiwaayatStorage
import com.example.model.EnquiryFormState
import com.example.model.PriceFilter
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.Region
import com.example.model.SortOption
import com.example.ui.BagActionResult
import com.example.ui.RiwaayatApp
import com.example.ui.RiwaayatViewModel
import com.example.ui.theme.RiwaayatTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class RiwaayatAcceptanceTests {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var app: Application

  @Before
  fun setUp() {
    app = ApplicationProvider.getApplicationContext()
    app.getSharedPreferences(RiwaayatStorage.PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit()
  }

  @Test
  fun `catalog covers 8 products across 5 regions with valid INR formatting`() {
    assertEquals(8, ProductCatalog.products.size)
    assertEquals(Region.entries.toSet(), ProductCatalog.products.map { it.region }.toSet())
    assertNull(ProductCatalog.findProductById("non-existent-id"))
    assertEquals("₹2,200", Formatters.formatInr(2200))
    assertEquals("₹3,000", Formatters.formatInr(3000))
  }

  @Test
  fun `validation normalizes Indian mobile and enforces pincode and acknowledgement rules`() {
    assertEquals("+91 9876543210", Formatters.normalizeIndianMobile("9876543210"))
    assertEquals("+91 9876543210", Formatters.normalizeIndianMobile("+91 98765-43210"))
    assertEquals("+91 9876543210", Formatters.normalizeIndianMobile("09876543210"))
    assertNull(Formatters.normalizeIndianMobile("1234567890"))
    assertNull(Formatters.normalizeIndianMobile("98765"))

    assertTrue(Formatters.isValidPincode("800001"))
    assertFalse(Formatters.isValidPincode("012345"))
    assertFalse(Formatters.isValidPincode("80001"))

    assertTrue(Formatters.isValidOptionalEmail(""))
    assertTrue(Formatters.isValidOptionalEmail("buyer@example.com"))
    assertFalse(Formatters.isValidOptionalEmail("not-an-email"))

    val invalidForm =
      EnquiryFormState(
        fullName = "   ",
        mobileNumber = "123",
        city = " ",
        state = "",
        pincode = "012345",
        email = "bad",
        acknowledged = false,
      )
    val errors = Formatters.validateEnquiryForm(invalidForm)
    assertTrue(errors.hasErrors)
    assertNotNull(errors.fullName)
    assertNotNull(errors.mobileNumber)
    assertNotNull(errors.city)
    assertNotNull(errors.state)
    assertNotNull(errors.pincode)
    assertNotNull(errors.email)
    assertNotNull(errors.acknowledged)
  }

  @Test
  fun `whatsapp uri uses digits-only recipient and url-encoded text`() {
    assertEquals("919876543210", Formatters.normalizeStoreWhatsAppDigits("+91 98765 43210"))
    assertNull(Formatters.normalizeStoreWhatsAppDigits("123"))

    val uri = Formatters.buildWhatsAppUri("919876543210", "Hello Riwaayat & Bihar")
    assertTrue(uri.startsWith("https://wa.me/919876543210?text="))
    assertTrue(uri.contains("Hello%20Riwaayat%20%26%20Bihar"))
  }

  @Test
  fun `combined filters and numeric sorting work accurately`() {
    val vm = RiwaayatViewModel(app)
    vm.selectRegionFilter(Region.BIHAR)
    vm.selectCategoryFilter(ProductCategory.KURTAS)
    assertEquals(1, vm.uiState.value.filteredProducts.size)
    assertEquals("mithila-cotton-kurta", vm.uiState.value.filteredProducts.first().id)

    vm.clearAllFilters()
    vm.selectPriceFilter(PriceFilter.UNDER_2000)
    assertEquals(2, vm.uiState.value.filteredProducts.size)

    vm.clearAllFilters()
    vm.selectSortOption(SortOption.PRICE_LOW_TO_HIGH)
    val pricesAsc = vm.uiState.value.filteredProducts.map { it.priceInr }
    assertEquals(pricesAsc.sorted(), pricesAsc)

    vm.selectSortOption(SortOption.PRICE_HIGH_TO_LOW)
    val pricesDesc = vm.uiState.value.filteredProducts.map { it.priceInr }
    assertEquals(pricesDesc.sortedDescending(), pricesDesc)
  }

  @Test
  fun `bag merges identical variants, separates different variants, and respects stock limits`() {
    val vm = RiwaayatViewModel(app)
    val kurta = ProductCatalog.findProductById("mithila-cotton-kurta")!!
    val dupatta = ProductCatalog.findProductById("bhagalpur-woven-dupatta")!!

    // Missing size rejected for kurta
    assertTrue(vm.addToBag(kurta, null, "Warm Ivory", 1) is BagActionResult.Error)
    // Unavailable variant rejected (XS + Indigo Slate has 0 demo stock)
    assertTrue(vm.addToBag(kurta, "XS", "Indigo Slate", 1) is BagActionResult.Error)
    // Zero quantity rejected
    assertTrue(vm.addToBag(kurta, "M", "Warm Ivory", 0) is BagActionResult.Error)

    // Add valid variant
    assertTrue(vm.addToBag(kurta, "M", "Warm Ivory", 2) is BagActionResult.Added)
    // Add same variant -> merges quantity
    assertTrue(vm.addToBag(kurta, "M", "Warm Ivory", 1) is BagActionResult.Added)
    // Add different variant -> separate line
    assertTrue(vm.addToBag(kurta, "L", "Deep Maroon", 1) is BagActionResult.Added)
    // Add dupatta -> uses One Size
    assertTrue(vm.addToBag(dupatta, null, "Sandstone Gold", 1) is BagActionResult.Added)

    val bag = vm.uiState.value.bagItems
    assertEquals(3, bag.size)
    assertEquals(5, vm.uiState.value.bagTotalQuantity)
    assertEquals(2200 * 4 + 1800, vm.uiState.value.bagMerchandiseSubtotalInr)
    assertEquals(Product.ONE_SIZE, bag.last().size)

    // Exceeding stock limit (M Warm Ivory stock is 5; currently 3 in bag -> adding 3 fails)
    assertTrue(vm.addToBag(kurta, "M", "Warm Ivory", 3) is BagActionResult.Error)
  }

  @Test
  fun `corrupted localStorage recovers safely without crashing`() {
    app
      .getSharedPreferences(RiwaayatStorage.PREFS_NAME, Context.MODE_PRIVATE)
      .edit()
      .putString(RiwaayatStorage.KEY_WISHLIST, "{not valid json")
      .putString(RiwaayatStorage.KEY_BAG, "corrupted")
      .putString(RiwaayatStorage.KEY_ENQUIRIES, "[123, null]")
      .putString(RiwaayatStorage.KEY_STORE_WHATSAPP, "invalid")
      .commit()

    val vm = RiwaayatViewModel(app)
    assertTrue(vm.uiState.value.wishlistIds.isEmpty())
    assertTrue(vm.uiState.value.bagItems.isEmpty())
    assertTrue(vm.uiState.value.savedEnquiries.isEmpty())
    assertEquals("", vm.uiState.value.storeWhatsAppDigits)
  }

  @Test
  fun `saving demo enquiry creates unique non-PII record and clears bag while leaving WhatsApp unset`() {
    val vm = RiwaayatViewModel(app)
    val kurta = ProductCatalog.findProductById("mithila-cotton-kurta")!!
    vm.addToBag(kurta, "M", "Warm Ivory", 2)
    vm.toggleWishlist(kurta.id)

    vm.updateEnquiryForm {
      it.copy(
        fullName = "Rani Kumari",
        mobileNumber = "9876543210",
        city = "Patna",
        state = "Bihar",
        pincode = "800001",
        email = "test@example.com",
        deliveryNote = "Call in evening",
        acknowledged = true,
      )
    }

    val ref1 = vm.saveDemoEnquiry()
    assertNotNull(ref1)
    assertTrue(vm.uiState.value.bagItems.isEmpty())
    assertEquals("", vm.uiState.value.enquiryForm.fullName)

    // Persisted raw JSON must NOT contain phone number, city, pincode, or email
    val rawEnquiriesJson =
      app
        .getSharedPreferences(RiwaayatStorage.PREFS_NAME, Context.MODE_PRIVATE)
        .getString(RiwaayatStorage.KEY_ENQUIRIES, "") ?: ""
    assertFalse(rawEnquiriesJson.contains("9876543210"))
    assertFalse(rawEnquiriesJson.contains("800001"))
    assertFalse(rawEnquiriesJson.contains("Patna"))
    assertFalse(rawEnquiriesJson.contains("test@example.com"))

    // Persistence across fresh ViewModel instance
    val vm2 = RiwaayatViewModel(app)
    assertEquals(1, vm2.uiState.value.savedEnquiries.size)
    assertEquals(setOf(kurta.id), vm2.uiState.value.wishlistIds)
    assertEquals(2, vm2.uiState.value.dashboardTotalUnitsRequested)
    assertEquals(4400, vm2.uiState.value.dashboardEstimatedEnquiryValueInr)

    // Second enquiry produces unique reference
    vm2.addToBag(kurta, "M", "Warm Ivory", 1)
    vm2.updateEnquiryForm {
      it.copy(
        fullName = "Priya",
        mobileNumber = "9876543211",
        city = "Gaya",
        state = "Bihar",
        pincode = "823001",
        acknowledged = true,
      )
    }
    val ref2 = vm2.saveDemoEnquiry()
    assertNotNull(ref2)
    assertNotEquals(ref1, ref2)

    // Sample enquiries are clearly labeled
    vm2.loadSampleEnquiries()
    assertTrue(vm2.uiState.value.savedEnquiries.any { it.isSample })
    assertTrue(vm2.uiState.value.savedEnquiries.any { !it.isSample })

    // Reset clears everything
    vm2.resetAllDemoData()
    assertTrue(vm2.uiState.value.savedEnquiries.isEmpty())
    assertTrue(vm2.uiState.value.wishlistIds.isEmpty())
    assertTrue(vm2.uiState.value.bagItems.isEmpty())
  }

  @Test
  @Config(sdk = [36], qualifiers = "w360dp-h780dp")
  fun `compose CUJ navigates from Home to Bihar Explore to Product Detail to Bag to Enquiry to Confirmation and Dashboard`() {
    composeTestRule.setContent { RiwaayatTheme { RiwaayatApp() } }
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithText("Wear a Story. Celebrate a Region.").assertIsDisplayed()
    composeTestRule.onNodeWithTag("home_region_bihar").performScrollTo().performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("explore_result_count").assertIsDisplayed()
    composeTestRule.onNodeWithText("Showing 3 of 8 pieces").assertIsDisplayed()

    composeTestRule
      .onNodeWithTag("view_details_mithila-cotton-kurta")
      .performScrollTo()
      .performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("add_to_bag_button").performScrollTo().performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("add_to_bag_feedback").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("feedback_view_bag").performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("proceed_to_enquiry").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("proceed_to_enquiry").performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithText("Send an Order Enquiry").assertIsDisplayed()
    composeTestRule.onNodeWithTag("whatsapp_unconfigured_notice").performScrollTo().assertIsDisplayed()
  }
}
