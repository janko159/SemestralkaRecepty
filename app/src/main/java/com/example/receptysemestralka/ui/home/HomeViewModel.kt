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

/*
* HomeViewModel sluzi na riadenie dat v aplikacii
* */

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    /*
    * premenne na recepty, suroviny a stav či sú už dáta načítané
    * sú mutableStateOf aby vedeli reagovať na zmenu dát
    * */
    var allRecipes by mutableStateOf<List<RecipeData>>(emptyList())
        private set

    var selectedIngredients by mutableStateOf<List<String>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set
    /*
    * Inicializačný blok zavolá sa po konštruktore triedy
    * viewModelScope je preddefinovany CoroutineScope ktory je viazany na
    * zivotny cyklus ViewModelu
    * launch mi vytvori novu coroutine ktora bude sledovat vstupy/vystupy
    * */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val loaded = FileStorage.loadRecipes(getApplication()) // getApplication referencia na Context -> citanie z interneho uloziska JSON subor
            if (loaded.isEmpty()) {
                val sample = createSampleRecipes()
                FileStorage.saveRecipes(getApplication(), sample)
                allRecipes = sample
            } else {
                allRecipes = loaded
            }
            isLoading = false
        }
    }

    private fun createSampleRecipes(): List<RecipeData> {
        return listOf(
            RecipeData(
                name = "Špagety s kyslou smotanou",
                instructions = "Uvar špagety…",
                ingredients = listOf("špagety", "kyslá smotana", "cesnak", "soľ", "čierne korenie")
            ),
            RecipeData(
                name = "Ovocný šalát",
                instructions = "Nakrájaj jablko…",
                ingredients = listOf("jablko", "hruška", "banán", "citrónová šťava")
            ),
            RecipeData(
                name = "Pečené zemiaky",
                instructions = "Zemiaky umyj…",
                ingredients = listOf("zemiaky", "olivový olej", "soľ")
            ),

        )
    }

    /*
    * aktualizácia listu surovín
    * prejde každý prvok zbaví ho bielych znakov a da ho na male pismena
    * potom sa prefiltruju prazdne zaznamy - vyberam neprazdne retazce
    * */
    fun updateSelectedIngredients(newSelected: List<String>) {
        selectedIngredients = newSelected.map { it.trim().lowercase() }.filter { it.isNotBlank() }
    }

    /*
    * prehľadá všetky recepty a vráti len tie,
    * ktoré obsahujú všetky práve vybraté suroviny.
    * */
    fun findRecipesByIngredients(): List<RecipeData> {
        if (selectedIngredients.isEmpty()) return emptyList()

        return allRecipes.filter { recipe ->
            val recipeIngsLower = recipe.ingredients.map { it.lowercase() }
            selectedIngredients.all { si ->
                recipeIngsLower.contains(si)
            }
        }
    }
    /*
    * Uloží nový zoznam receptov do trvalého úložiska,
    * JSON súbor asynchrónne na pozadí
    * */
    fun addRecipe(newRecipe: RecipeData) {
        val updated = allRecipes + newRecipe
        allRecipes = updated
        viewModelScope.launch(Dispatchers.IO) {
            FileStorage.saveRecipes(getApplication(), updated)
        }
    }
}
