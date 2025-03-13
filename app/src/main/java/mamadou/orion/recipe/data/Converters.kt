package mamadou.orion.recipe.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return Json.decodeFromString(data)
    }
}
