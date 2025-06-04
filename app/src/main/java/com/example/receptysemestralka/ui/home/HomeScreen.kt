// app/src/main/java/com/example/receptysemestralka/ui/home/HomeScreen.kt
package com.example.receptysemestralka.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.receptysemestralka.ui.home.views.HomeViewModel
import com.example.receptysemestralka.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    var ingredient by remember { mutableStateOf("") }
    // Zoznam surovín, kde budeme mazanie rovnakým spôsobom ako v AddRecipeScreen
    val ingredientsList = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hlavný nadpis
        Text(
            text = "RECEPTY PODĽA MOŽNOSTÍ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Podnadpis
        Text(
            text = "Zadajte suroviny",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Textové pole na zadanie suroviny
        OutlinedTextField(
            value = ingredient,
            onValueChange = { ingredient = it },
            placeholder = { Text("Surovina") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tlačidlo "Pridať"
        Button(
            onClick = {
                if (ingredient.isNotBlank()) {
                    ingredientsList.add(ingredient.trim().lowercase())
                    ingredient = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary
            )
        ) {
            Text(
                text = "Pridať",
                color = colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Obal pre zoznam pridaných surovín s možnosťou kliknutím odstrániť
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(
                    width = 1.dp,
                    color = colorScheme.onBackground.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (ingredientsList.isEmpty()) {
                Text(
                    text = "Žiadne suroviny",
                    color = colorScheme.onBackground.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                ingredientsList.forEachIndexed { index, item ->
                    Text(
                        text = "${index + 1}. $item",
                        color = colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Po kliknutí sa zmaže príslušná surovina
                                ingredientsList.removeAt(index)
                            }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tlačidlo "Vyhľadaj recept" – navigácia na obrazovku s výsledkami
        Button(
            onClick = {
                homeViewModel.updateSelectedIngredients(ingredientsList.toList())
                navController.navigate(Screen.Recipes.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary
            )
        ) {
            Text(
                text = "Vyhľadaj recept",
                color = colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tlačidlo "Pridaj recept" – navigácia na AddRecipeScreen
        Button(
            onClick = {
                navController.navigate(Screen.AddRecipe.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.secondary
            )
        ) {
            Text(
                text = "Pridaj recept",
                color = colorScheme.onSecondary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
