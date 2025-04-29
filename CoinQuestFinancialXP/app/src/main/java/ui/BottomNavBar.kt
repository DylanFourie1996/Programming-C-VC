package com.example.coinquestfinancialxp.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coinquestfinancialxp.navigation.Screen
import com.example.coinquestfinancialxp.ui.screens.LoginScreen
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)


@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(Screen.Home.route, Screen.Home.title, Icons.Filled.Home),
        BottomNavItem(Screen.Profile.route, Screen.Profile.title, Icons.Filled.Person),
        BottomNavItem(Screen.Settings.route, Screen.Settings.title, Icons.Filled.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val customColors = LocalCustomColors.current
    val unselectedPadding = 0.dp
    val selectedPadding = 16.dp

    Surface(modifier=Modifier.fillMaxWidth().padding(horizontal=24.dp, vertical=32.dp),
        shape=RoundedCornerShape(100.dp),
        color=Color.White,
        shadowElevation=1.dp) {
        Row(modifier=Modifier.fillMaxWidth().padding(vertical=12.dp),
            horizontalArrangement=Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment=Alignment.CenterVertically) {
            items.forEach { item ->
                val selected = item.route == currentRoute


                Box(
                    modifier = Modifier.clip(RoundedCornerShape(100.dp))
                        .clickable {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                        .background(if (selected) Color(1.0f, 1.0f, 1.0f,1f) else Color.Transparent)
                        .padding(end=if (selected) selectedPadding else unselectedPadding),

                    ) {
                    Row(verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.Start) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).padding(start=if (selected) 8.dp else 0.dp)
                        )
                        if (selected) {
                            Spacer(modifier=Modifier.width(3.dp))
                            Text(text=item.title)
                        }
                    }
                }
                /*NavigationBarItem(
                modifier= Modifier.clip(RoundedCornerShape(15.dp)),
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )*/
            }
        }
    }
}
