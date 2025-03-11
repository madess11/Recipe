package mamadou.orion.recipe.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class ApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun fetchRecipes(query: String, page: Int): List<Recipe> {
        val response: HttpResponse = client.get("https://food2fork.ca/api/recipe/search/") {
            url {
                parameters.append("query", query)
                parameters.append("page", page.toString())
            }
            headers.append("Authorization", "Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
        }
        return response.body<RecipeResponse>().results
    }
}

@Serializable
data class RecipeResponse(val results: List<Recipe>)
