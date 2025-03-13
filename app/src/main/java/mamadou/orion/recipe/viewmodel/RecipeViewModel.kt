package mamadou.orion.recipe.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    fun fetchRecipes(query: String="beef%20carrot%20potato%20onion", page: Int=1) {
        viewModelScope.launch {
            _recipes.value = repository.getRecipes(query, page)

            Log.d("query =>",query)
            Log.d("Données reçues",_recipes.value.toString())
        }
    }
}

class RecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
