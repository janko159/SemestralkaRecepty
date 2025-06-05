// app/src/main/java/com/example/receptysemestralka/ui/home/AddRecipeScreen.kt
package com.example.receptysemestralka.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.receptysemestralka.data.RecipeData
import com.example.receptysemestralka.ui.home.views.HomeViewModel

@Composable
fun AddRecipeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    // 1) Ak sa dáta ešte načítavajú, zobrazíme načítavací spinner
    if (homeViewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 2) Stavové premenne pre nové polia
    var recipeName by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var currentIngredient by remember { mutableStateOf("") }
    val ingredientsList = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pridaj nový recept",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3) Polia na zadanie názvu a postupu
        OutlinedTextField(
            value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text("Názov receptu") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Postup prípravy") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),  // viac riadkov na text
            singleLine = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4) Zadanie a pridávanie surovín
        Text(
            text = "Pridaj suroviny",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = currentIngredient,
            onValueChange = { currentIngredient = it },
            label = { Text("Surovina") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val trimmed = currentIngredient.trim()
                if (trimmed.isNotEmpty()) {
                    ingredientsList.add(trimmed)
                    currentIngredient = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = "Pridať surovinu", color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5) Zoznam pridaných surovín (kliknutím sa odstráni)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (ingredientsList.isEmpty()) {
                Text(
                    text = "Žiadne suroviny",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                ingredientsList.forEachIndexed { index, item ->
                    Text(
                        text = "${index + 1}. $item",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { ingredientsList.removeAt(index) }
                            .padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 6) Tlačidlo “Uložiť” – pridá recept do viewModelu a vráti sa na Home
        Button(
            onClick = {
                // Overenie, že všetky polia sú vyplnené
                if (recipeName.isBlank()) {
                    // Môžete použiť napr. Snackbar alebo Toast, ale tu len anulujeme ukladanie
                    return@Button
                }
                if (instructions.isBlank()) {
                    return@Button
                }
                if (ingredientsList.isEmpty()) {
                    return@Button
                }
                // Vytvoríme nový objekt RecipeData
                val newRecipe = RecipeData(
                    name = recipeName.trim(),
                    instructions = instructions.trim(),
                    ingredients = ingredientsList.toList()
                )
                // Uložíme ho do ViewModelu (a do JSON-u v pozadí)
                homeViewModel.addRecipe(newRecipe)

                // Navigujeme späť na HomeScreen
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = "Uložiť recept",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7) Tlačidlo “Zrušiť” – vráti sa späť bez uloženia
        TextButton(
            onClick = { navController.popBackStack() }
        ) {
            Text(text = "Zrušiť", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
