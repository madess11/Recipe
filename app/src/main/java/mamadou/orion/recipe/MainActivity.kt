package mamadou.orion.recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mamadou.orion.recipe.ui.screen.DetailScreen
import mamadou.orion.recipe.ui.screen.ListScreen
import mamadou.orion.recipe.ui.theme.RecipeTheme
import mamadou.orion.recipe.viewmodel.RecipeViewModel
import mamadou.orion.recipe.viewmodel.RecipeViewModelFactory

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
                        viewModel.fetchRecipes()
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
