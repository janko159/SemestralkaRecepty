// app/src/main/java/com/example/receptysemestralka/ui/home/views/HomeViewModel.kt
package com.example.receptysemestralka.ui.home.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.receptysemestralka.data.FileStorage
import com.example.receptysemestralka.data.RecipeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Celý zoznam receptov načítaný z JSON súboru
    var allRecipes by mutableStateOf<List<RecipeData>>(emptyList())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val loaded = FileStorage.loadRecipes(getApplication())
            if (loaded.isEmpty()) {
                // Pri prvom spustení (súbor neexistuje), seedneme vzorové recepty
                val sample = createSampleRecipes()
                FileStorage.saveRecipes(getApplication(), sample)
                allRecipes = sample
            } else {
                // Súbor existuje → načítame ho do stavu
                allRecipes = loaded
            }
        }
    }

    /** V prípade prázdneho JSON-u vygenerujeme 3 ukážkové recepty. */
    private fun createSampleRecipes(): List<RecipeData> {
        return listOf(
            RecipeData(
                name = "Špagety s kyslou smotanou",
                instructions = "Uvar špagety. Medzitým zmiešaj kyslú smotanu s cesnakom a korením. Špagety sceď a premiešaj so smotanovou omáčkou.",
                ingredients = listOf("špagety", "kyslá smotana", "cesnak", "soľ", "čierne korenie")
            ),
            RecipeData(
                name = "Ovocný šalát",
                instructions = "Nakrájaj jablko, hrušku a banán. Zmiešaj ovocie, pokvapkáš citrónom. Podávaj čerstvé.",
                ingredients = listOf("jablko", "hruška", "banán", "citrónová šťava")
            ),
            RecipeData(
                name = "Pečené zemiaky",
                instructions = "Zemiaky umyj, nakrájaj na mesiačiky, pokrop olivovým olejom, osoľ. Peč pri 200 °C 30 minút.",
                ingredients = listOf("zemiaky", "olivový olej", "soľ")
            )
        )
    }

    /**
     * Filtrovanie receptov podľa komparácie zoznamu ingrediencií (v pamäti).
     * Vráti tie recepty, ktoré obsahujú všetky položky zo userIngredients.
     */
    fun findRecipesByIngredients(userIngredients: List<String>): List<RecipeData> {
        return allRecipes.filter { recipe ->
            // Porovnáme case‐insensitive: každá user‐ingrediencia sa nachádza
            // v ingredients zoznamu receptu
            userIngredients.all { ui ->
                val lowerUi = ui.trim().lowercase()
                recipe.ingredients.any { it.lowercase() == lowerUi }
            }
        }
    }

    /** Pridá nový recept do zoznamu a hneď uloží celý JSON späť do súboru. */
    fun addRecipe(newRecipe: RecipeData) {
        val updated = allRecipes + newRecipe
        allRecipes = updated
        viewModelScope.launch(Dispatchers.IO) {
            FileStorage.saveRecipes(getApplication(), updated)
        }
    }
}
