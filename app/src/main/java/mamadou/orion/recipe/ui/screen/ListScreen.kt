package mamadou.orion.recipe.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import mamadou.orion.recipe.viewmodel.RecipeViewModel

@Composable
fun ListScreen(navController: NavController, viewModel: RecipeViewModel = viewModel()) {
    val recipes by viewModel.recipes.collectAsState()
    var page by remember { mutableIntStateOf(1) }
    val pageSize = 30
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("beef") }
    val coroutineScope = rememberCoroutineScope()

    // Définition des catégories depuis la query
    val baseQuery = "chicken+soup+beef+carrot+potato+onion"
    val baseQuery2 = "beef%20carrot%20potato%20onion"
    val categories =  baseQuery.removePrefix("query=").split("+")

    // Charger les recettes au démarrage et lors du changement de catégorie
    LaunchedEffect(selectedCategory) {
        coroutineScope.launch {
            viewModel.fetchRecipes(selectedCategory)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Champ de recherche
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Rechercher une recette") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Barre de sélection des catégories
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Liste des recettes
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState()
        ) {
            items(recipes.take(page * pageSize)) { recipe ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("detail/${recipe.pk}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        AsyncImage(
                            model = recipe.featured_image,
                            contentDescription = "Recipe Image",
                            modifier = Modifier
                                .width(120.dp)
                                .height(120.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = recipe.title,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            // Bouton "Plus" pour la pagination
            item {
                if (recipes.size > page * pageSize) {
                    Button(
                        onClick = { page++ },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Text("Plus")
                    }
                }
            }
        }
    }
}
