package mamadou.orion.recipe.ui.screen


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.recipesapp.Screen
import mamadou.orion.recipe.viewmodel.RecipeViewModel

@Composable
fun LoadingScreen(navController: NavController, viewModel: RecipeViewModel) {
    val recipes by viewModel.recipes.collectAsState()

    // Vérifie si les recettes sont chargées avant de naviguer
    LaunchedEffect(recipes) {
        if (recipes.isNotEmpty()) {
            navController.navigate(Screen.List.route) {
                popUpTo(Screen.Loading.route) { inclusive = true } // Supprime l'écran de chargement de la pile
            }
        }
    }

    // Affiche l'indicateur de chargement tant que les données ne sont pas prêtes
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
