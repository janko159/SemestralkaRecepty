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

/*
* objekty pre navigaciu
* su v sealed class pretoze to je uzavreta trieda ktorej podtriedy mozu byt
* len tie, ktore su definovane v tom istom subore
* kompilator vie že sú len tieto screeny v aplikacii
* */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddRecipe : Screen("add_recipe")
    object Recipes : Screen("recipes")
}

/*
* Component Activity zakladna aktivita ktora podporuje compose
* */
class MainActivity : ComponentActivity() {
    /*
    * tu sa zavedie aktivita nastavia sa zaklady
    * Cele UI ide cez compose
    * */
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
/*
* nastavenie navigacie pri prvom volani default HomeScreen
* */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        composable(Screen.AddRecipe.route) {
            AddRecipeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        composable(Screen.Recipes.route) {
            RecipesScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }
    }
}
