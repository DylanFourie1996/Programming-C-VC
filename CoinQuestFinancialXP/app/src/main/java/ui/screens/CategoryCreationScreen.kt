package ui.screens

import Model.CategoryModel
import Utils.SessionManager
import ViewModels.CategoryViewModel
import ViewModels.Factories.CategoryViewModelFactory
import ViewModels.Factories.LoginRegisterViewModelFactory
import ViewModels.LoginRegisterViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors
import kotlinx.coroutines.delay
import ui.CustomComposables.StandardButton
import ui.CustomComposables.StandardTextBox

@Composable
fun CategoryCreation(navController : NavController) {
    BackHandler {}
    val categoryViewModel : CategoryViewModel = viewModel(factory=CategoryViewModelFactory(
        LocalContext.current
    ))
    val categories by categoryViewModel.allCategories.collectAsState()

    val showAddCategory = remember {mutableStateOf(false)}
    var categoryText by remember {mutableStateOf("")}

    val sessionManager = SessionManager.getInstance(LocalContext.current)

    val customColors = LocalCustomColors.current
    Scaffold(topBar =  {
        Column(modifier=Modifier.fillMaxWidth().padding(top=32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Categories")
            Divider(modifier=Modifier.padding(top=32.dp, bottom=0.dp), color=customColors.DividerColor1)
        }

    }
    ) { paddingParameter ->
        LazyColumn(modifier= Modifier.padding(paddingParameter), contentPadding= PaddingValues(bottom=32.dp)) {

            items(categories) { category : CategoryModel ->
                Spacer(modifier=Modifier.height(12.dp))
                Tag(categoryViewModel, category)
                Spacer(modifier=Modifier.height(12.dp))
                Divider(color=customColors.DividerColor2)
            }
            item {
                StandardButton(text = "+", modifier=Modifier.padding(horizontal=150.dp, vertical=16.dp).height(42.dp), onClick = {
                    showAddCategory.value = true
                })

            }
        }
    }

    if (showAddCategory.value)
    {
        AlertDialog(
            modifier=Modifier.padding(vertical=16.dp),
            shape = RoundedCornerShape(15.dp),
            onDismissRequest = {
                showAddCategory.value = false
            },
            title={Text("New Category")},
            text={
                Column(modifier=Modifier.fillMaxWidth().padding(horizontal=16.dp)) {
                    Text("Create a new category")
                    Spacer(modifier = Modifier.height(32.dp))
                    StandardTextBox(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Category Name",
                        value = categoryText,
                        onValueChange = {
                            categoryText=it
                        })
                }
                 },
            buttons = {
                Column(modifier=Modifier.fillMaxWidth().padding(bottom=32.dp, top=16.dp, start=16.dp, end=16.dp)) {
                    StandardButton(modifier=Modifier.padding(horizontal=24.dp), text = "Confirm", onClick = {
                        if (sessionManager.isLoggedIn()) {
                            categoryViewModel.insertNewCategory(CategoryModel(userId=sessionManager.getUserId(), title = categoryText, premade=false))
                            showAddCategory.value = false
                        }
                    })
                    Spacer(modifier=Modifier.height(16.dp))
                    StandardButton(modifier=Modifier.padding(horizontal=24.dp), text = "Cancel", onClick = { showAddCategory.value = false })
                }
            }
        )
    }
}

@Composable
fun Tag(categoryViewModel : CategoryViewModel, category : CategoryModel)
{
    val customColors = LocalCustomColors.current
    val isPremade = category.premade
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(visible=visible, exit = slideOutHorizontally(targetOffsetX={-it})+ fadeOut(animationSpec= tween(durationMillis=150)))
    {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.weight(1.0f).padding(vertical = 12.dp),
                elevation = 8.dp,
                shape = if (!isPremade) RoundedCornerShape(
                    topStart = 100.dp,
                    bottomStart = 100.dp
                ) else RoundedCornerShape(100.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(category.title)
                }
            }
            if (!isPremade) {

                Button(
                    modifier = Modifier.width(48.dp).height(32.dp),
                    shape = RoundedCornerShape(bottomEnd = 100.dp, topEnd = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = customColors.TextBoxBG,
                    ),
                    onClick = {
                        // Delete category
                        //categoryViewModel.deleteCategory(category)
                        visible = false
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }


            }
        }
    }

    if (!visible) {
        LaunchedEffect(Unit) {
            delay(150)
            categoryViewModel.deleteCategory(category)
        }
    }
}