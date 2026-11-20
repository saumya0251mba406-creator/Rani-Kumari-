package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.ProductCatalog
import com.example.ui.AppRoute
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.DeepMaroonSoft
import com.example.ui.theme.MutedCharcoalText
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldSoft
import com.example.ui.theme.MutedSurface
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwaayatTopBar(
  wishlistCount: Int,
  bagCount: Int,
  onOpenDrawer: () -> Unit,
  onNavigateHome: () -> Unit,
  onNavigateExplore: () -> Unit,
  onNavigateWishlist: () -> Unit,
  onNavigateBag: () -> Unit,
) {
  CenterAlignedTopAppBar(
    colors =
      TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = WarmIvory,
        titleContentColor = DeepMaroon,
        navigationIconContentColor = CharcoalText,
        actionIconContentColor = CharcoalText,
      ),
    navigationIcon = {
      IconButton(
        onClick = onOpenDrawer,
        modifier = Modifier.testTag("nav_drawer_button"),
      ) {
        Icon(
          imageVector = Icons.Outlined.Menu,
          contentDescription = "Open navigation menu",
        )
      }
    },
    title = {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
          Modifier.clickable(onClick = onNavigateHome).padding(horizontal = 8.dp, vertical = 2.dp),
      ) {
        Text(
          text = "Riwaayat",
          style = MaterialTheme.typography.headlineMedium,
          color = DeepMaroon,
          maxLines = 1,
        )
      }
    },
    actions = {
      IconButton(
        onClick = onNavigateExplore,
        modifier = Modifier.testTag("top_search_button"),
      ) {
        Icon(
          imageVector = Icons.Outlined.Search,
          contentDescription = "Search and explore collection",
        )
      }
      IconButton(
        onClick = onNavigateWishlist,
        modifier = Modifier.testTag("top_wishlist_button"),
      ) {
        BadgedBox(
          badge = {
            if (wishlistCount > 0) {
              Badge(containerColor = DeepMaroon, contentColor = WarmIvory) {
                Text(text = wishlistCount.toString())
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Wishlist ($wishlistCount saved)",
          )
        }
      }
      IconButton(
        onClick = onNavigateBag,
        modifier = Modifier.testTag("top_bag_button"),
      ) {
        BadgedBox(
          badge = {
            if (bagCount > 0) {
              Badge(containerColor = MutedGold, contentColor = CharcoalText) {
                Text(text = bagCount.toString())
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Outlined.ShoppingBag,
            contentDescription = "Shopping bag ($bagCount items)",
          )
        }
      }
    },
  )
}

@Composable
fun SitewidePrototypeNotice(modifier: Modifier = Modifier) {
  Surface(
    color = MutedGoldSoft,
    border = BorderStroke(1.dp, SoftBorder),
    modifier = modifier.fillMaxWidth(),
  ) {
    Text(
      text = ProductCatalog.SITEWIDE_PROTOTYPE_NOTICE,
      style = MaterialTheme.typography.labelMedium,
      color = CharcoalText,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
  }
}

@Composable
fun RiwaayatBottomNav(
  currentRoute: String?,
  onNavigate: (String) -> Unit,
) {
  NavigationBar(
    containerColor = WarmIvory,
    tonalElevation = 0.dp,
    windowInsets = WindowInsets.navigationBars,
  ) {
    val items =
      listOf(
        Triple(AppRoute.HOME, "Home", Icons.Outlined.Home),
        Triple(AppRoute.EXPLORE, "Explore", Icons.Outlined.Explore),
        Triple(AppRoute.STORIES, "Stories", Icons.Outlined.AutoStories),
        Triple(AppRoute.ABOUT, "About", Icons.Outlined.Info),
      )
    items.forEach { (route, label, icon) ->
      val selected = currentRoute == route
      NavigationBarItem(
        selected = selected,
        onClick = { onNavigate(route) },
        icon = { Icon(imageVector = icon, contentDescription = label) },
        label = {
          Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        },
        colors =
          NavigationBarItemDefaults.colors(
            selectedIconColor = DeepMaroon,
            selectedTextColor = DeepMaroon,
            indicatorColor = DeepMaroonSoft,
            unselectedIconColor = MutedCharcoalText,
            unselectedTextColor = MutedCharcoalText,
          ),
        modifier = Modifier.testTag("bottom_nav_$route"),
      )
    }
  }
}

@Composable
fun RiwaayatNavigationRail(
  currentRoute: String?,
  wishlistCount: Int,
  bagCount: Int,
  onNavigate: (String) -> Unit,
) {
  NavigationRail(
    containerColor = WarmIvory,
    modifier = Modifier.fillMaxHeight().padding(top = 8.dp),
  ) {
    val items =
      listOf(
        Triple(AppRoute.HOME, "Home", Icons.Outlined.Home),
        Triple(AppRoute.EXPLORE, "Explore", Icons.Outlined.Explore),
        Triple(AppRoute.STORIES, "Stories", Icons.Outlined.AutoStories),
        Triple(AppRoute.WISHLIST, "Wishlist", Icons.Outlined.FavoriteBorder),
        Triple(AppRoute.BAG, "Bag", Icons.Outlined.ShoppingBag),
        Triple(AppRoute.ABOUT, "About", Icons.Outlined.Info),
      )
    items.forEach { (route, label, icon) ->
      val selected = currentRoute == route
      NavigationRailItem(
        selected = selected,
        onClick = { onNavigate(route) },
        icon = {
          when (route) {
            AppRoute.WISHLIST ->
              BadgedBox(
                badge = {
                  if (wishlistCount > 0) {
                    Badge(containerColor = DeepMaroon, contentColor = WarmIvory) {
                      Text(wishlistCount.toString())
                    }
                  }
                }
              ) {
                Icon(icon, contentDescription = label)
              }
            AppRoute.BAG ->
              BadgedBox(
                badge = {
                  if (bagCount > 0) {
                    Badge(containerColor = MutedGold, contentColor = CharcoalText) {
                      Text(bagCount.toString())
                    }
                  }
                }
              ) {
                Icon(icon, contentDescription = label)
              }
            else -> Icon(icon, contentDescription = label)
          }
        },
        label = { Text(label, maxLines = 1) },
        colors =
          NavigationRailItemDefaults.colors(
            selectedIconColor = DeepMaroon,
            selectedTextColor = DeepMaroon,
            indicatorColor = DeepMaroonSoft,
            unselectedIconColor = MutedCharcoalText,
            unselectedTextColor = MutedCharcoalText,
          ),
      )
    }
  }
}

@Composable
fun RiwaayatDrawerContent(
  currentRoute: String?,
  wishlistCount: Int,
  bagCount: Int,
  onNavigate: (String) -> Unit,
) {
  ModalDrawerSheet(
    drawerContainerColor = WarmIvory,
    modifier = Modifier.widthIn(max = 310.dp),
  ) {
    Column(
      modifier = Modifier.verticalScroll(rememberScrollState()).padding(vertical = 16.dp)
    ) {
      Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text(
          text = "Riwaayat",
          style = MaterialTheme.typography.headlineLarge,
          color = DeepMaroon,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Indian Heritage. Your Everyday Style.",
          style = MaterialTheme.typography.bodySmall,
          color = MutedCharcoalText,
        )
      }
      HorizontalDivider(color = SoftBorder, modifier = Modifier.padding(vertical = 8.dp))

      val primaryDestinations =
        listOf(
          Triple(AppRoute.HOME, "Home", Icons.Outlined.Home),
          Triple(AppRoute.EXPLORE, "Explore Collection", Icons.Outlined.Explore),
          Triple(AppRoute.STORIES, "Regional Stories", Icons.Outlined.AutoStories),
          Triple(AppRoute.WISHLIST, "Wishlist ($wishlistCount)", Icons.Outlined.FavoriteBorder),
          Triple(AppRoute.BAG, "Shopping Bag ($bagCount)", Icons.Outlined.ShoppingBag),
          Triple(AppRoute.ABOUT, "About Riwaayat", Icons.Outlined.Info),
        )

      primaryDestinations.forEach { (route, label, icon) ->
        NavigationDrawerItem(
          label = { Text(label) },
          selected = currentRoute == route,
          onClick = { onNavigate(route) },
          icon = { Icon(icon, contentDescription = null) },
          colors =
            NavigationDrawerItemDefaults.colors(
              selectedContainerColor = DeepMaroonSoft,
              selectedIconColor = DeepMaroon,
              selectedTextColor = DeepMaroon,
              unselectedContainerColor = WarmIvory,
              unselectedIconColor = MutedCharcoalText,
              unselectedTextColor = CharcoalText,
            ),
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
        )
      }

      HorizontalDivider(color = SoftBorder, modifier = Modifier.padding(vertical = 12.dp))

      Text(
        text = "ASSIGNMENT TOOLS",
        style = MaterialTheme.typography.labelSmall,
        color = MutedCharcoalText,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
      )

      NavigationDrawerItem(
        label = { Text("Demo Dashboard") },
        selected = currentRoute == AppRoute.DASHBOARD,
        onClick = { onNavigate(AppRoute.DASHBOARD) },
        icon = { Icon(Icons.Outlined.Dashboard, contentDescription = null) },
        colors =
          NavigationDrawerItemDefaults.colors(
            selectedContainerColor = DeepMaroonSoft,
            selectedIconColor = DeepMaroon,
            selectedTextColor = DeepMaroon,
            unselectedContainerColor = WarmIvory,
            unselectedIconColor = MutedCharcoalText,
            unselectedTextColor = CharcoalText,
          ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp).testTag("drawer_dashboard"),
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RiwaayatFooter(
  onNavigate: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    color = MutedSurface,
    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    border = BorderStroke(1.dp, SoftBorder),
    modifier = modifier.fillMaxWidth().padding(top = 24.dp),
  ) {
    Column(
      modifier =
        Modifier.fillMaxWidth().widthIn(max = 1040.dp).padding(horizontal = 20.dp, vertical = 24.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
        text = "Riwaayat",
        style = MaterialTheme.typography.headlineSmall,
        color = DeepMaroon,
        modifier = Modifier.semantics { heading() },
      )
      Text(
        text =
          "Indian Heritage. Your Everyday Style. A curated regional-fashion discovery and WhatsApp order-enquiry prototype.",
        style = MaterialTheme.typography.bodyMedium,
        color = CharcoalText,
      )

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        FooterLink("Home") { onNavigate(AppRoute.HOME) }
        FooterLink("Explore") { onNavigate(AppRoute.EXPLORE) }
        FooterLink("Regional Stories") { onNavigate(AppRoute.STORIES) }
        FooterLink("About") { onNavigate(AppRoute.ABOUT) }
        FooterLink("Wishlist") { onNavigate(AppRoute.WISHLIST) }
        FooterLink("Bag") { onNavigate(AppRoute.BAG) }
        FooterLink("Demo Dashboard", testTag = "footer_demo_dashboard") {
          onNavigate(AppRoute.DASHBOARD)
        }
      }

      HorizontalDivider(color = SoftBorder)

      Text(
        text =
          "Prototype notice: Sample products, illustrative specifications, and demonstration pricing only. No live payments or verified supplier claims.",
        style = MaterialTheme.typography.bodySmall,
        color = MutedCharcoalText,
      )
    }
  }
}

@Composable
private fun FooterLink(
  label: String,
  testTag: String? = null,
  onClick: () -> Unit,
) {
  TextButton(
    onClick = onClick,
    modifier = if (testTag != null) Modifier.testTag(testTag) else Modifier,
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelLarge,
      color = DeepMaroon,
      fontWeight = FontWeight.SemiBold,
    )
  }
}
