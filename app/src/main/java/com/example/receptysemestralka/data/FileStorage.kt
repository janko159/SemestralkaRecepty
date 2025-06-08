package com.example.receptysemestralka.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object FileStorage {
    private const val FILE_NAME = "recipes.json"
    private val gson = Gson()
    private val listType = object : TypeToken<List<RecipeData>>() {}.type
    /*
    * Algoritmus inšpirovaný internetom
    * */
    /** Načíta zo súboru, ak neexistuje, vráti prázdny zoznam. */
    fun loadRecipes(context: Context): List<RecipeData> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return emptyList()
        val json = file.readText()
        return gson.fromJson<List<RecipeData>>(json, listType) ?: emptyList()
    }

    /*
    * Algoritmus inšpirovaný internetom
    * */
    /** Zapisuje celý zoznam do súboru (prepisuje). */
    fun saveRecipes(context: Context, recipes: List<RecipeData>) {
        val file = File(context.filesDir, FILE_NAME)
        val json = gson.toJson(recipes)
        file.writeText(json)
    }
}
