package mamadou.orion.recipe.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.recipesapp.R
import com.example.recipesapp.Screen
import mamadou.orion.recipe.viewmodel.RecipeViewModel

@Composable
fun ListScreen(navController: NavController, viewModel: RecipeViewModel) {
    val recipes by viewModel.recipes.collectAsState()

    LazyColumn {
        items(recipes) { recipe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { navController.navigate(Screen.Detail.createRoute(recipe.pk)) },
                elevation = 4.dp
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Image(
                        painter = rememberImagePainter(recipe.featured_image),
                        contentDescription = stringResource(R.string.recipe_image_description),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = recipe.title, style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}
