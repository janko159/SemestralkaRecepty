// app/src/main/java/com/example/receptysemestralka/ui/home/RecipesScreen.kt
package com.example.receptysemestralka.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receptysemestralka.data.RecipeData
import com.example.receptysemestralka.ui.home.views.HomeViewModel

@Composable
fun RecipesScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    // 1) Čakáme na dokončenie načítania dát
    if (homeViewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 2) Získame filtrované recepty
    val filteredRecipes: List<RecipeData> = homeViewModel.findRecipesByIngredients()

    // 3) Budeme si pamätať, ktorý recept je práve rozbalený (podľa jeho názvu)
    var expandedRecipeName by remember { mutableStateOf<String?>(null) }

    if (filteredRecipes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Žiadne recepty nenájdené",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredRecipes) { recipe ->
                RecipeCardExpandable(
                    recipe = recipe,
                    expanded = (recipe.name == expandedRecipeName),
                    onCardClick = {
                        expandedRecipeName = if (expandedRecipeName == recipe.name) {
                            // Ak je už rozbalený, kliknutím ho zbalíme
                            null
                        } else {
                            // Inak rozbalíme práve tento
                            recipe.name
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun RecipeCardExpandable(
    recipe: RecipeData,
    expanded: Boolean,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            // Pridáme animateContentSize(), aby sa výška karty plynule menila pri rozbaľovaní
            .animateContentSize()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Názov receptu
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Predbežný preview - prvé 3 ingrediencie
            val previewIngredients = recipe.ingredients
                .take(3)
                .joinToString(", ")
            Text(
                text = "Suroviny: $previewIngredients" +
                        if (recipe.ingredients.size > 3) "…" else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            // Ak je karta rozbalená, zobrazíme detail (celé ingrediencie + postup)
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))

                // Nadpis „Všetky suroviny:”
                Text(
                    text = "Všetky suroviny:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Zoznam všetkých ingrediencií
                recipe.ingredients.forEach { ing ->
                    Text(
                        text = "- $ing",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nadpis „Postup:”
                Text(
                    text = "Postup:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Samotný text postupu
                Text(
                    text = recipe.instructions,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
