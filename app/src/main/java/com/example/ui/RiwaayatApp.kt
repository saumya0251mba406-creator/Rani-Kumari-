package com.example.ui

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.ProductCatalog
import com.example.ui.components.RiwaayatBottomNav
import com.example.ui.components.RiwaayatDrawerContent
import com.example.ui.components.RiwaayatNavigationRail
import com.example.ui.components.RiwaayatTopBar
import com.example.ui.components.SitewidePrototypeNotice
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.BagScreen
import com.example.ui.screens.ConfirmationScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EnquiryScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotFoundScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.StoriesScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.WarmIvory
import kotlinx.coroutines.launch

object AppRoute {
  const val HOME = "home"
  const val EXPLORE = "explore"
  const val PRODUCT_DETAIL = "product/{productId}"
  const val STORIES = "stories"
  const val WISHLIST = "wishlist"
  const val BAG = "bag"
  const val ENQUIRY = "enquiry"
  const val CONFIRMATION = "confirmation/{reference}"
  const val ABOUT = "about"
  const val DASHBOARD = "dashboard"
  const val NOT_FOUND = "not_found"

  fun productDetail(productId: String): String = "product/${Uri.encode(productId)}"

  fun confirmation(reference: String): String = "confirmation/${Uri.encode(reference)}"
}

@Composable
fun RiwaayatApp(viewModel: RiwaayatViewModel = viewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  fun navigateSingleTop(route: String) {
    navController.navigate(route) {
      popUpTo(navController.graph.findStartDestination().id) { saveState = true }
      launchSingleTop = true
      restoreState = true
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      RiwaayatDrawerContent(
        currentRoute = currentRoute,
        wishlistCount = uiState.wishlistCount,
        bagCount = uiState.bagTotalQuantity,
        onNavigate = { route ->
          scope.launch { drawerState.close() }
          navigateSingleTop(route)
        },
      )
    },
  ) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(WarmIvory)) {
      val useNavRail = maxWidth >= 600.dp

      Scaffold(
        containerColor = WarmIvory,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
          RiwaayatTopBar(
            wishlistCount = uiState.wishlistCount,
            bagCount = uiState.bagTotalQuantity,
            onOpenDrawer = { scope.launch { drawerState.open() } },
            onNavigateHome = { navigateSingleTop(AppRoute.HOME) },
            onNavigateExplore = { navigateSingleTop(AppRoute.EXPLORE) },
            onNavigateWishlist = { navigateSingleTop(AppRoute.WISHLIST) },
            onNavigateBag = { navigateSingleTop(AppRoute.BAG) },
          )
        },
        bottomBar = {
          if (!useNavRail) {
            RiwaayatBottomNav(
              currentRoute = currentRoute,
              onNavigate = { navigateSingleTop(it) },
            )
          }
        },
      ) { innerPadding ->
        Row(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
          if (useNavRail) {
            RiwaayatNavigationRail(
              currentRoute = currentRoute,
              wishlistCount = uiState.wishlistCount,
              bagCount = uiState.bagTotalQuantity,
              onNavigate = { navigateSingleTop(it) },
            )
          }

          Column(modifier = Modifier.weight(1f).fillMaxSize()) {
            SitewidePrototypeNotice()

            Box(modifier = Modifier.weight(1f)) {
              NavHost(
                navController = navController,
                startDestination = AppRoute.HOME,
                enterTransition = { fadeIn(animationSpec = tween(180)) },
                exitTransition = { fadeOut(animationSpec = tween(180)) },
              ) {
                composable(AppRoute.HOME) {
                  HomeScreen(
                    featuredProducts = uiState.allProducts.filter { it.isFeatured },
                    wishlistIds = uiState.wishlistIds,
                    onExploreCollection = { navigateSingleTop(AppRoute.EXPLORE) },
                    onDiscoverStories = { navigateSingleTop(AppRoute.STORIES) },
                    onSelectRegion = { region ->
                      viewModel.openRegionInExplore(region)
                      navigateSingleTop(AppRoute.EXPLORE)
                    },
                    onToggleWishlist = viewModel::toggleWishlist,
                    onViewProduct = { productId ->
                      navController.navigate(AppRoute.productDetail(productId))
                    },
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(AppRoute.EXPLORE) {
                  ExploreScreen(
                    filteredProducts = uiState.filteredProducts,
                    totalCatalogCount = uiState.allProducts.size,
                    filterState = uiState.filterState,
                    wishlistIds = uiState.wishlistIds,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onSelectRegion = viewModel::selectRegionFilter,
                    onSelectCategory = viewModel::selectCategoryFilter,
                    onSelectPriceFilter = viewModel::selectPriceFilter,
                    onSelectSortOption = viewModel::selectSortOption,
                    onClearFilters = viewModel::clearAllFilters,
                    onToggleWishlist = viewModel::toggleWishlist,
                    onViewProduct = { productId ->
                      navController.navigate(AppRoute.productDetail(productId))
                    },
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(
                  route = AppRoute.PRODUCT_DETAIL,
                  arguments = listOf(navArgument("productId") { type = NavType.StringType }),
                ) { entry ->
                  val productId = entry.arguments?.getString("productId") ?: ""
                  val product = ProductCatalog.findProductById(productId)
                  ProductDetailScreen(
                    product = product,
                    isWishlisted = uiState.wishlistIds.contains(productId),
                    wishlistIds = uiState.wishlistIds,
                    onBack = {
                      if (!navController.popBackStack()) {
                        navigateSingleTop(AppRoute.EXPLORE)
                      }
                    },
                    onExploreCollection = { navigateSingleTop(AppRoute.EXPLORE) },
                    onToggleWishlist = viewModel::toggleWishlist,
                    onAddToBag = viewModel::addToBag,
                    onViewBag = { navigateSingleTop(AppRoute.BAG) },
                    onViewRelatedProduct = { relatedId ->
                      navController.navigate(AppRoute.productDetail(relatedId))
                    },
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(AppRoute.STORIES) {
                  StoriesScreen(
                    onShopRegion = { region ->
                      viewModel.openRegionInExplore(region)
                      navigateSingleTop(AppRoute.EXPLORE)
                    },
                    onViewProduct = { productId ->
                      navController.navigate(AppRoute.productDetail(productId))
                    },
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(AppRoute.WISHLIST) {
                  WishlistScreen(
                    wishlistProducts = uiState.wishlistProducts,
                    onToggleWishlist = viewModel::toggleWishlist,
                    onViewProduct = { productId ->
                      navController.navigate(AppRoute.productDetail(productId))
                    },
                    onExploreCollection = { navigateSingleTop(AppRoute.EXPLORE) },
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(AppRoute.BAG) {
                  BagScreen(
                    bagItems = uiState.bagItems,
                    merchandiseSubtotalInr = uiState.bagMerchandiseSubtotalInr,
                    onUpdateQuantity = viewModel::updateBagQuantity,
                    onRemoveItem = viewModel::removeBagItem,
                    onViewProduct = { productId ->
                      navController.navigate(AppRoute.productDetail(productId))
                    },
                    onContinueShopping = { navigateSingleTop(AppRoute.EXPLORE) },
                    onProceedToEnquiry = { navController.navigate(AppRoute.ENQUIRY) },
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(AppRoute.ENQUIRY) {
                  EnquiryScreen(
                    bagItems = uiState.bagItems,
                    merchandiseSubtotalInr = uiState.bagMerchandiseSubtotalInr,
                    formState = uiState.enquiryForm,
                    storeWhatsAppDigits = uiState.storeWhatsAppDigits,
                    isSubmitting = uiState.isSubmittingEnquiry,
                    onUpdateForm = viewModel::updateEnquiryForm,
                    onSaveDemoEnquiry = viewModel::saveDemoEnquiry,
                    onBuildWhatsAppMessageIfValid = viewModel::buildWhatsAppMessageIfValid,
                    onEnquirySaved = { reference ->
                      navController.navigate(AppRoute.confirmation(reference)) {
                        popUpTo(AppRoute.BAG) { inclusive = false }
                      }
                    },
                    onExploreCollection = { navigateSingleTop(AppRoute.EXPLORE) },
                    onOpenDashboard = { navigateSingleTop(AppRoute.DASHBOARD) },
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(
                  route = AppRoute.CONFIRMATION,
                  arguments = listOf(navArgument("reference") { type = NavType.StringType }),
                ) { entry ->
                  val reference = entry.arguments?.getString("reference") ?: ""
                  val record = viewModel.findEnquiryByReference(reference)
                  ConfirmationScreen(
                    record = record,
                    onContinueExploring = { navigateSingleTop(AppRoute.EXPLORE) },
                    onViewDashboard = { navigateSingleTop(AppRoute.DASHBOARD) },
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(AppRoute.ABOUT) {
                  AboutScreen(onNavigateFooter = { navigateSingleTop(it) })
                }

                composable(AppRoute.DASHBOARD) {
                  DashboardScreen(
                    savedEnquiries = uiState.savedEnquiries,
                    totalUnitsRequested = uiState.dashboardTotalUnitsRequested,
                    estimatedEnquiryValueInr = uiState.dashboardEstimatedEnquiryValueInr,
                    storeWhatsAppDigits = uiState.storeWhatsAppDigits,
                    onSaveStoreWhatsApp = viewModel::updateStoreWhatsAppNumber,
                    onLoadSampleEnquiries = viewModel::loadSampleEnquiries,
                    onResetDemoData = viewModel::resetAllDemoData,
                    onNavigateFooter = { navigateSingleTop(it) },
                  )
                }

                composable(AppRoute.NOT_FOUND) {
                  NotFoundScreen(
                    onPrimaryAction = { navigateSingleTop(AppRoute.EXPLORE) },
                    onSecondaryAction = { navigateSingleTop(AppRoute.HOME) },
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
