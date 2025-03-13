package mamadou.orion.recipe.data

import android.content.Context

class RecipeRepository(context: Context) {
    private val db = RecipeDatabase.getDatabase(context)
    private val apiService = ApiService()

    suspend fun getRecipes(query: String, page: Int): List<Recipe> {
        return try {
            val recipes = apiService.fetchRecipes(query, page)
            db.recipeDao().insertRecipes(recipes)
            recipes
        } catch (e: Exception) {
            db.recipeDao().getAllRecipes() // Retourne le cache local en cas d'erreur réseau
        }
    }
}
