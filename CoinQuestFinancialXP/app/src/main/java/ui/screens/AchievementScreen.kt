package ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors

data class Achievement(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementScreen(navController: NavHostController) {
    BackHandler {
        navController.popBackStack()
    }

    val customColors = LocalCustomColors.current

    val achievements = listOf(
        Achievement(
            id = 1,
            title = "First Steps",
            description = "First Login",
            icon = Icons.Default.Star,
            isUnlocked = true,
            color = Color(0xFFFFB900)
        ),
        Achievement(
            id = 2,
            title = "Look at you,Being Responsible",
            description = "Completed month without going over the budget",
            icon = Icons.Default.Star,
            isUnlocked = true,
            color = Color(0xFF00B3FF)
        ),
        Achievement(
            id = 3,
            title = "First Timer",
            description = "Added first expense",
            icon = Icons.Default.Star,
            isUnlocked = false,
            color = Color(0xFFFF5630)
        ),
        Achievement(
            id = 4,
            title = "Streak Champion",
            description = "Maintain a 30-day streak",
            icon = Icons.Default.Star,
            isUnlocked = false,
            color = Color(0xFF36B37E)
        ),
        Achievement(
            id = 5,
            title = "Big Spender",
            description = "Added 5 expense",
            icon = Icons.Default.Star,
            isUnlocked = false,
            color = Color(0xFF6554C0)
        ),
        Achievement(
            id = 6,
            title = "Log Out achievement",
            description = "Logout For the First Time",
            icon = Icons.Default.Lock,
            isUnlocked = false,
            color = Color(0xFFFF8F73)
        )
    )

    Scaffold(
        containerColor=customColors.page,
        topBar = {
            TopAppBar(
                windowInsets=WindowInsets(0.dp),
                modifier=Modifier.padding(0.dp),
                title = {
                    Text(
                        text = "Achievements",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    color = customColors.page
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Collect them all!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally),
                    fontWeight=FontWeight.SemiBold
                )

                /*FlowRow(
                    //columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow=2,

                ) {


                }*/
                achievements.chunked(2).forEach { pair ->
                    Row(
                        modifier=Modifier.fillMaxWidth().padding(vertical=8.dp, horizontal=16.dp),
                        horizontalArrangement=Arrangement.spacedBy(16.dp)
                    ) {
                        pair.forEach { ach ->
                            AchievementCard(achievement=ach, mod=Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement, mod : Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = (if (!expanded) mod.aspectRatio(0.8f)
            .wrapContentHeight() else mod.wrapContentHeight()).clickable{
                expanded = !expanded
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (achievement.isUnlocked) achievement.color else Color.Gray
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (achievement.isUnlocked)
                            achievement.color
                        else
                            Color.Gray.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (achievement.isUnlocked) {
                    Icon(
                        imageVector = achievement.icon,
                        contentDescription = achievement.title,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = achievement.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (achievement.isUnlocked)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = achievement.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = if (achievement.isUnlocked)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}