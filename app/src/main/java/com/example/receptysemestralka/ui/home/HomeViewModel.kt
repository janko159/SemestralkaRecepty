// app/src/main/java/com/example/receptysemestralka/ui/home/HomeViewModel.kt
package com.example.receptysemestralka.ui.home

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

    // 1) Kompletný zoznam receptov načítaný zo súboru
    var allRecipes by mutableStateOf<List<RecipeData>>(emptyList())
        private set

    init {
        // 2) Pri štarte ViewModelu načítame JSON do allRecipes
        viewModelScope.launch(Dispatchers.IO) {
            val loaded = FileStorage.loadRecipes(getApplication())
            // prepíšeme stav na hlavnom vlákne
            allRecipes = loaded
        }
    }

    /**
     * 3) Filtrovanie podľa vstupného zoznamu surovín.
     *    Vráti len tie RecipeData, kde všetky položky
     *    userIngredients sa nachádzajú v recipe.ingredients.
     */
    fun findRecipesByIngredients(userIngredients: List<String>): List<RecipeData> {
        return allRecipes.filter { recipe ->
            userIngredients.all { it in recipe.ingredients }
        }
    }

    /**
     * 4) Ak budeš chcieť priamo pridávať nové recepty cez UI:
     *    - pridáme do allRecipes a
     *    - uložíme späť do súboru.
     */
    fun addRecipe(newRecipe: RecipeData) {
        val updated = allRecipes + newRecipe
        allRecipes = updated
        viewModelScope.launch(Dispatchers.IO) {
            FileStorage.saveRecipes(getApplication(), updated)
        }
    }
}
