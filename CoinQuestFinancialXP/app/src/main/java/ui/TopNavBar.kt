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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

data class TopNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)


@Composable
fun TopNavBar(navController: NavController) {
    val items = listOf(
        TopNavItem(Screen.Profile.route, Screen.Profile.title, Icons.Filled.Person),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier=Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer).padding(top=16.dp)) {
        Row(modifier=Modifier.fillMaxWidth().padding(vertical=12.dp, horizontal=16.dp),
            horizontalArrangement=Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment=Alignment.CenterVertically) {
            Text("CoinQuest", modifier=Modifier.weight(1.0f), color=MaterialTheme.colorScheme.onPrimaryContainer, fontSize=24.sp)
            items.forEach { item ->
                Box(
                    modifier = Modifier
                        .clickable {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center, modifier=Modifier.padding(3.dp)) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                                .padding(start = 0.dp)
                        )
                        Text(item.title)
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
