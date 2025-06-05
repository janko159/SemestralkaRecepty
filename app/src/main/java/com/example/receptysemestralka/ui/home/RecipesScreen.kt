// app/src/main/java/com/example/receptysemestralka/ui/home/RecipesScreen.kt
package com.example.receptysemestralka.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receptysemestralka.data.RecipeData
import com.example.receptysemestralka.ui.home.views.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    // 1) Ak sa dáta ešte načítavajú, zobrazíme načítací spinner
    if (homeViewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 2) Získame filtrované recepty podľa vybraných ingrediencií
    val filteredRecipes: List<RecipeData> = homeViewModel.findRecipesByIngredients()

    // 3) Premenná, ktorá bude držať názov práve rozbaleného receptu (null = žiadny)
    var expandedRecipeName by remember { mutableStateOf<String?>(null) }

    // Ukádame si topBar a zvyšok obsahu do Scaffold, aby sme mohli mať šípku “späť”
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nájdené recepty",
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
        }
    ) { innerPadding ->
        // 4) Obsah RecipesScreen – buď hlásenie o nenájdených receptoch, alebo zoznam
        if (filteredRecipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
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
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCardExpandable(
                        recipe = recipe,
                        expanded = (recipe.name == expandedRecipeName),
                        onCardClick = {
                            expandedRecipeName = if (expandedRecipeName == recipe.name) {
                                // Ak už bol rozbalený, kliknutím ho zbalíme
                                null
                            } else {
                                // Inak rozbalíme práve tento recept
                                recipe.name
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Jedna karta receptu, ktorá vie zobraziť buď iba názov + stručný preview ingrediencií,
 * alebo (ak je expanded==true) rozbaliť sa a ukázať všetky ingrediencie + postup.
 */
@Composable
private fun RecipeCardExpandable(
    recipe: RecipeData,
    expanded: Boolean,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()    // plynulý prechod výšky karty pri rozbaľovaní
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

            // Preview – iba prvé 3 ingrediencie
            val previewIngredients = recipe.ingredients
                .take(3)
                .joinToString(", ")
            Text(
                text = "Suroviny: $previewIngredients" +
                        if (recipe.ingredients.size > 3) "…" else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            // Ak je karta rozbalená, pridáme sekciu so všetkými ingredienciami + postupom
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))

                // Nadpis pre celý zoznam ingrediencií
                Text(
                    text = "Všetky suroviny:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                recipe.ingredients.forEach { ing ->
                    Text(
                        text = "- $ing",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nadpis pre postup
                Text(
                    text = "Postup:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = recipe.instructions,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
