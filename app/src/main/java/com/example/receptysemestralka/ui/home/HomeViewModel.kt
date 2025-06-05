package com.example.receptysemestralka.ui.home.views

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.receptysemestralka.data.FileStorage
import com.example.receptysemestralka.data.RecipeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.text.Normalizer

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var allRecipes by mutableStateOf<List<RecipeData>>(emptyList())
        private set

    var selectedIngredients by mutableStateOf<List<String>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val loaded = FileStorage.loadRecipes(getApplication())
            if (loaded.isEmpty()) {
                val sample = createSampleRecipes()
                FileStorage.saveRecipes(getApplication(), sample)
                allRecipes = sample
            } else {
                allRecipes = loaded
            }

            // TOTO PRIDAJTE – vyhodnotenie, čo sa skutočne načítalo
            Log.d("HomeViewModel", ">> allRecipes.size=${allRecipes.size}")
            allRecipes.forEach { r ->
                Log.d("HomeViewModel", "  • ${r.name} → ingredients=${r.ingredients}")
            }

            isLoading = false
        }
    }

    private fun createSampleRecipes(): List<RecipeData> {
        return listOf(
            RecipeData(
                name = "Špagety s kyslou smotanou",
                instructions = "Uvar špagety, zmiešaj kyslú smotanu s cesnakom a korením...",
                ingredients = listOf("špagety", "kyslá smotana", "cesnak", "soľ", "čierne korenie")
            ),
            RecipeData(
                name = "Ovocný šalát",
                instructions = "Nakrájaj jablko, hrušku, banán. Zmiešaj ovocie, pokvapkáš citrónom...",
                ingredients = listOf("jablko", "hruška", "banán", "citrónová šťava")
            ),
            RecipeData(
                name = "Pečené zemiaky",
                instructions = "Zemiaky umyj, nakrájaj na mesiačiky, pokrop olivovým olejom, osoľ. Peč pri 200 °C 30 minút.",
                ingredients = listOf("zemiaky", "olivový olej", "soľ")
            )
        )
    }

    fun updateSelectedIngredients(newSelected: List<String>) {
        // Trim + lowercase, aby sme mali konzistentné porovnanie
        selectedIngredients = newSelected.map { it.trim().lowercase() }
    }

    private fun stripDiacritics(input: String): String {
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

    /**
     * Upravené filtrovanie: aspoň jedna zo zadaných surovín sa musí nachádzať v recepte.
     * (Diakritiku ignorujeme.)
     */
    fun findRecipesByIngredients(): List<RecipeData> {
        if (selectedIngredients.isEmpty()) return emptyList()

        return allRecipes.filter { recipe ->
            selectedIngredients.any { siRaw ->
                val si = stripDiacritics(siRaw)
                recipe.ingredients.any { ingRaw ->
                    val ing = stripDiacritics(ingRaw.lowercase())
                    ing.contains(si)
                }
            }
        }
    }

    fun addRecipe(newRecipe: RecipeData) {
        val updated = allRecipes + newRecipe
        allRecipes = updated
        viewModelScope.launch(Dispatchers.IO) {
            FileStorage.saveRecipes(getApplication(), updated)
        }
    }
}
