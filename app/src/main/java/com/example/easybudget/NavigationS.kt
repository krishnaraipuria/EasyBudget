package com.example.easybudget

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.selects.selectUnbiased

@Composable
fun NavHostS() {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        NavBottomBar(
            navController = navController,
            items = listOf(
                NaviItem(route = "homes", icon = R.drawable.group_18),
                NaviItem(route = "stats", icon = R.drawable.group_18)
            )
        )
    }) {


        NavHost(navController = navController, startDestination = "homes", modifier = Modifier.padding(it)) {
            composable(route = "homes") {
                HomeS(navController)
            }
            composable(route = "add") {
                AddTran(navController)
            }
            composable(route = "stats") {
                StatsS(navController)
            }
        }
    }
}

data class NaviItem(
    val route: String,
    val icon: Int,
)

@Composable
fun NavBottomBar(navController: NavController, items: List<NaviItem>) {
    val navBackStackEntry=navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar() {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    androidx.compose.material3.Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Color.Black,
                    selectedIconColor = Color.Black,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )
        }
        }
    }