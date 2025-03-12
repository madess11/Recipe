package mamadou.orion.recipe.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import mamadou.orion.recipe.R
import mamadou.orion.recipe.viewmodel.RecipeViewModel

@Composable
fun ListScreen(navController: NavController, viewModel: RecipeViewModel) {
    val recipes by viewModel.recipes.collectAsState()

    // Utilisation de LazyColumn pour afficher les recettes de manière paresseuse
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(recipes) { recipe ->
            // Utilisation de la navigation pour afficher les détails de la recette
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        // Navigation vers l'écran de détail avec l'ID de la recette
                        navController.navigate("detail/${recipe.pk}")
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    AsyncImage(
                        model = recipe.featured_image,

                        contentDescription = stringResource(R.string.recipe_image_description),
                        modifier = Modifier
                            .width(120.dp) // Réduire la taille pour éviter de prendre toute la largeur
                            .height(120.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = recipe.title,
                        modifier = Modifier.weight(1f), // Permet au texte de prendre l'espace restant
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
