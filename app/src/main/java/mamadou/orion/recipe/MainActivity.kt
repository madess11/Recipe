package mamadou.orion.recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import mamadou.orion.recipe.ui.screen.*
import mamadou.orion.recipe.viewmodel.RecipeViewModel
import mamadou.orion.recipe.viewmodel.RecipeViewModelFactory
import mamadou.steve.recipe.ui.theme.RecipeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeTheme {
                val navController = rememberNavController()
                val viewModel: RecipeViewModel = viewModel(factory = RecipeViewModelFactory(applicationContext))

                // Chargement des données au lancement
                var isLoading by remember { mutableStateOf(true) }
                var errorMessage by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    try {
                        viewModel.loadRecipes(query = "chicken", page = 1)
                        isLoading = false
                    } catch (e: Exception) {
                        errorMessage = "Erreur de chargement des recettes : ${e.message}"
                        isLoading = false
                    }
                }

                if (isLoading) {
                    LoadingScreen()
                } else if (errorMessage != null) {
                    ErrorScreen(errorMessage!!)
                } else {
                    Scaffold { innerPadding ->
                        NavigationGraph(navController, viewModel, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: RecipeViewModel, modifier: Modifier) {
    NavHost(navController = navController, startDestination = "list", modifier = modifier) {
        composable("list") { ListScreen(navController, viewModel) }
        composable(
            "detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            DetailScreen(navController, recipeId, viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    RecipeTheme {
        LoadingScreen()
    }
}
