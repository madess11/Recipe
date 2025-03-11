package mamadou.orion.recipe.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.recipesapp.R
import mamadou.orion.recipe.viewmodel.RecipeViewModel

@Composable
fun DetailScreen(navController: NavController, recipeId: Int, viewModel: RecipeViewModel) {
    val recipe = viewModel.recipes.collectAsState().value.find { it.pk == recipeId }

    recipe?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(it.title) },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Image(
                        painter = rememberImagePainter(it.featured_image),
                        contentDescription = it.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${stringResource(R.string.published_by)} ${it.publisher}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${stringResource(R.string.rating)} ${it.rating} ⭐",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.ingredients),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(it.ingredients) { ingredient ->
                    Text(
                        text = "• $ingredient",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.back))
                    }
                }
            }
        }
    } ?: run {
        Text(
            text = stringResource(R.string.recipe_not_found),
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    }
}
