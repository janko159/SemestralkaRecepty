// app/src/main/java/com/example/receptysemestralka/ui/home/HomeScreen.kt
package com.example.receptysemestralka.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.receptysemestralka.data.RecipeData
import com.example.receptysemestralka.ui.home.views.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
    // Stav pre vstupné pole "Surovina"
    var ingredient by remember { mutableStateOf("") }
    // Stav pre zoznam zadaných surovín
    val ingredientsList = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hlavný nadpis
        Text(
            text = "RECEPTY PODĽA MOŽNOSTÍ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Podnadpis
        Text(
            text = "Zadajte suroviny",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
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

        // Tlačidlo "Pridať surovinu"
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
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Pridať",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Zobrazenie zoznamu pridaných surovín
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
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
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tlačidlo "Vyhľadaj recept"
        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Vyhľadaj recept",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nové tlačidlo "Pridaj recept"
        Button(
            onClick = {
                // TODO: napríklad: navController.navigate(Screen.AddRecipe.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = "Pridaj recept",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
