// app/src/main/java/com/example/receptysemestralka/ui/home/AddRecipeScreen.kt
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.receptysemestralka.data.RecipeData
import com.example.receptysemestralka.ui.home.views.HomeViewModel

/**
 * AddRecipeScreen: obrazovka na pridanie nového receptu.
 * Po kliknutí na surovinu v zozname sa daná surovina odstráni.
 */
@Composable
fun AddRecipeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    // Stav pre názov receptu + chyba
    var recipeName by remember { mutableStateOf(TextFieldValue("")) }
    var nameError by remember { mutableStateOf(false) }

    // Stav pre aktuálnu surovinu
    var currentIngredient by remember { mutableStateOf(TextFieldValue("")) }
    // Stav pre zoznam surovín (využijeme mutableStateListOf)
    val ingredientsList = remember { mutableStateListOf<String>() }
    var ingredientError by remember { mutableStateOf(false) }

    // Stav pre postup receptu + chyba
    var instructions by remember { mutableStateOf(TextFieldValue("")) }
    var instructionsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pridajte nový recept",
            fontSize = 26.sp,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 1) TextField pre názov receptu
        OutlinedTextField(
            value = recipeName,
            onValueChange = {
                recipeName = it
                if (it.text.trim().isNotEmpty()) nameError = false
            },
            label = { Text("Názov receptu") },
            placeholder = { Text("Zadajte názov") },
            singleLine = true,
            isError = nameError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),

        )
        if (nameError) {
            Text(
                text = "Názov nemôže byť prázdny",
                color = colorScheme.error,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2) Riadok s TextField + Button pre pridanie suroviny
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentIngredient,
                onValueChange = { currentIngredient = it },
                placeholder = { Text("Nová surovina") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),

            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    val ingText = currentIngredient.text.trim()
                    if (ingText.isNotEmpty()) {
                        ingredientsList.add(ingText)
                        currentIngredient = TextFieldValue("")
                        ingredientError = false
                    }
                },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary
                )
            ) {
                Text(text = "Pridať surovinu", color = colorScheme.onPrimary)
            }
        }
        if (ingredientError) {
            Text(
                text = "Pridajte aspoň jednu surovinu",
                color = colorScheme.error,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3) Zoznam pridaných surovín – kliknutie odstráni danú položku
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp, max = 200.dp)
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
                    color = colorScheme.onBackground.copy(alpha = 0.5f)
                )
            } else {
                ingredientsList.forEachIndexed { index, item ->
                    Text(
                        text = "${index + 1}. $item",
                        color = colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Odstránime kliknutú surovinu
                                ingredientsList.removeAt(index)
                            }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4) Multiline TextField pre postup
        OutlinedTextField(
            value = instructions,
            onValueChange = {
                instructions = it
                if (it.text.trim().isNotEmpty()) instructionsError = false
            },
            label = { Text("Postup") },
            placeholder = { Text("Popíšte postup prípravy") },
            isError = instructionsError,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(8.dp),

        )
        if (instructionsError) {
            Text(
                text = "Postup nemôže byť prázdny",
                color = colorScheme.error,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 5) Tlačidlo Uložiť recept
        Button(
            onClick = {
                val nameText = recipeName.text.trim()
                val instText = instructions.text.trim()

                nameError = nameText.isEmpty()
                ingredientError = ingredientsList.isEmpty()
                instructionsError = instText.isEmpty()

                if (!nameError && !ingredientError && !instructionsError) {
                    val newRecipe = RecipeData(
                        name = nameText,
                        instructions = instText,
                        ingredients = ingredientsList.toList()
                    )
                    homeViewModel.addRecipe(newRecipe)
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.secondary
            )
        ) {
            Text(text = "Uložiť recept", color = colorScheme.onSecondary)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
