package com.example.receptysemestralka.data

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val instructions: String,
    val ingredientsJson: String  // tu budeme ukladať JSON zoznam surovín
)

// ------------- TypeConverter -------------
class Converters {
    private val gson = Gson()
    private val type = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun listToJson(list: List<String>): String = gson.toJson(list, type)

    @TypeConverter
    fun jsonToList(json: String): List<String> =
        gson.fromJson(json, type) ?: emptyList()
}
