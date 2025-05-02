package com.example.coinquestfinancialxp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.coinquestfinancialxp.navigation.Screen
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
        BottomNavItem(Screen.CategorySpendScreen.route, Screen.CategorySpendScreen.title, Icons.Filled.Menu),
        BottomNavItem(Screen.StatsScreen.route, Screen.StatsScreen.title, Icons.Filled.Share),
        BottomNavItem(Screen.Settings.route, Screen.Settings.title, Icons.Filled.Settings),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val customColors = LocalCustomColors.current
    val unselectedPadding = 0.dp
    val selectedPadding = 16.dp

    Surface(modifier=Modifier.fillMaxWidth().padding(start=24.dp, end=24.dp, top=0.dp, bottom=24.dp),
        shape=RoundedCornerShape(100.dp),
        color=Color.White,
        shadowElevation=1.dp) {
        Row(modifier=Modifier.fillMaxWidth().padding(vertical=12.dp),
            horizontalArrangement=Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment=Alignment.CenterVertically) {
            items.forEach { item ->
                val selected = item.route == currentRoute

                Column(horizontalAlignment=Alignment.CenterHorizontally) {
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
                            .background(
                                if (selected) Color(
                                    1.0f,
                                    1.0f,
                                    1.0f,
                                    1f
                                ) else Color.Transparent
                            )
                            .padding(end = unselectedPadding)//if (selected) selectedPadding else unselectedPadding),

                        ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                                    .padding(start = 0.dp)//if (selected) 8.dp else 0.dp)
                            )

                        }
                    }
                    if (selected) {
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = item.title, fontSize=12.sp)
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
