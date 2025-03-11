package mamadou.orion.recipe.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import mamadou.orion.recipe.data.Recipe
import mamadou.orion.recipe.data.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(context: Context) : ViewModel() {
    private val repository = RecipeRepository(context)

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    fun fetchRecipes(query: String, page: Int) {
        viewModelScope.launch {
            _recipes.value = repository.getRecipes(query, page)
        }
    }
}
