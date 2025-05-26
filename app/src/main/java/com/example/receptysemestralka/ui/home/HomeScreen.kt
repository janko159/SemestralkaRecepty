package com.example.receptysemestralka.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    // Stav pre textové pole "Surovina"
    var ingredient by remember { mutableStateOf("") }
    // Stav pre zoznam surovín
    val ingredientsList = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
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

        Spacer(modifier = Modifier.height(24.dp))

        // Podnadpis
        Text(
            text = "Zadajte suroviny",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                    ingredientsList.add(ingredient.trim())
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

        Spacer(modifier = Modifier.height(16.dp))

        // Obal pre zoznam pridaných surovín
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

        // Môžeme tu pokračovať s ďalšími prvkami... (Filtrovanie, Vyhľadaj)
    }
}