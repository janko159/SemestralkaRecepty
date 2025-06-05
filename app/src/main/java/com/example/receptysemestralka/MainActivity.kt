// app/src/main/java/com/example/receptysemestralka/MainActivity.kt
package com.example.receptysemestralka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.receptysemestralka.ui.home.AddRecipeScreen
import com.example.receptysemestralka.ui.home.HomeScreen
import com.example.receptysemestralka.ui.home.RecipesScreen
import com.example.receptysemestralka.ui.home.views.HomeViewModel
import com.example.receptysemestralka.ui.theme.ReceptySemestralkaTheme

/**
 * Obalová trieda pre všetky trasy v aplikácii
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddRecipe : Screen("add_recipe")
    object Recipes : Screen("recipes")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReceptySemestralkaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation() //navigácia
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    // 1) Vytvoríme si NavController
    val navController = rememberNavController()

    // 2) Vytvoríme JEDINÚ inštanciu HomeViewModel, ktorú budeme zdieľať
    val homeViewModel: HomeViewModel = viewModel()

    // 3) Definujeme NavHost, kde do každej obrazovky odovzdáme rovnaké homeViewModel
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // HomeScreen potrebuje navController aj zdieľaný homeViewModel
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        // AddRecipeScreen potrebuje navController aj homeViewModel, aby mohol ukladať nové recepty
        composable(Screen.AddRecipe.route) {
            AddRecipeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        // RecipesScreen potrebuje len homeViewModel (nemá navController)
        composable(Screen.Recipes.route) {
            RecipesScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }
    }
}
