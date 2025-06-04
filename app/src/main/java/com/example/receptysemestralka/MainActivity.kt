// Umiestnenie súboru:
// app/src/main/java/com/example/receptysemestralka/MainActivity.kt
//   (alebo v src/main/kotlin/... ak váš projekt používa Kotlin-priečinky)

package com.example.receptysemestralka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.receptysemestralka.ui.home.AddRecipeScreen
import com.example.receptysemestralka.ui.home.HomeScreen
import com.example.receptysemestralka.ui.home.RecipesScreen
import com.example.receptysemestralka.ui.theme.ReceptySemestralkaTheme

/**
 * 1) Definícia jednotlivých "rout" pre navigáciu.
 *    - Home         => domovská obrazovka
 *    - AddRecipe    => obrazovka na pridanie receptu (zatiaľ len nadpis)
 *    - (Results, Detail) – ponecháme tu, hoci ich teraz nevyužívame.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddRecipe : Screen("add_recipe")
    object Recipes : Screen("recipes")            // <-- NOVÉ
    object Results : Screen("results")
    object Detail : Screen("detail/{recipeName}") {
        fun createRoute(recipeName: String) = "detail/$recipeName"
    }
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
                    AppNavigation()
                }
            }
        }
    }
}

/**
 * 2) Hlavná {@Composable} funkcia, ktorá vytvára NavHost
 *    a zisťuje, do ktorej obrazovky (composable) pristupovať.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 1) Domovská obrazovka
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // 2) Obrazovka "AddRecipe" (prázdna zatiaľ)
        composable(Screen.AddRecipe.route) {
            AddRecipeScreen(navController = navController)
        }

        // 3) NOVÁ obrazovka "Recipes"
        composable(Screen.Recipes.route) {
            RecipesScreen()   // tu zobrazíme nadpis "Recepty"
        }

        // 4) Ostatné (Results, Detail) necháme definované,
        //    aj keď ich ešte nebudeme volať.
        composable(Screen.Results.route) { /* ResultsScreen(navController) */ }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("recipeName") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeName = backStackEntry.arguments?.getString("recipeName") ?: ""
            // DetailScreen(recipeName)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppNavigationPreview() {
    ReceptySemestralkaTheme {
        AppNavigation()
    }
}
