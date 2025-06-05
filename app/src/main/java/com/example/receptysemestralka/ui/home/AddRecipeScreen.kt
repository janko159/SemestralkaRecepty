// app/src/main/java/com/example/receptysemestralka/ui/home/AddRecipeScreen.kt
package com.example.receptysemestralka.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.receptysemestralka.data.RecipeData
import com.example.receptysemestralka.ui.home.views.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    // Použijeme Scaffold, aby sme mohli mať TopAppBar s ikonou späť
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pridaj nový recept",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Späť"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)      // rešpektujeme padding zo Scaffoldu
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

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
            }
        })
}