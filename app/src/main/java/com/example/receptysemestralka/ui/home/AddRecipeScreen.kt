package com.example.receptysemestralka.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receptysemestralka.R
import com.example.receptysemestralka.data.RecipeData
import com.example.receptysemestralka.ui.home.views.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    var recipeName by rememberSaveable { mutableStateOf("") }
    var instructions by rememberSaveable { mutableStateOf("") }
    var currentIngredient by rememberSaveable { mutableStateOf("") }
    /*
    * inspirovane internetom
    * save {it.toList()} -> Compose vezme SnapshotStateList<String> (ingredientsList)
    * a spraví z neho obyčajný List<String>
    *
    * restore ->Keď sa aplikácia obnovuje (napr. po otočení),
    * Compose dostane späť ten uložený List<String> a cez
    *  tento blok ho premení späť na sledovateľný SnapshotStateList<String>,
    *  aby sa opäť správne reagovalo na zmeny v UI
    * */
    val ingredientsList = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { restored -> mutableStateListOf(*restored.toTypedArray()) }
        )
    ) { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_recipe_title), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = recipeName,
                onValueChange = { recipeName = it },
                label = { Text(stringResource(R.string.label_recipe_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text(stringResource(R.string.label_instructions)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false
            )

            Spacer(Modifier.height(24.dp))

            Text(stringResource(R.string.section_add_ingredients), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = currentIngredient,
                onValueChange = { currentIngredient = it },
                label = { Text(stringResource(R.string.label_recipe_name).replace("Názov receptu","Surovina")) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    currentIngredient.trim().takeIf { it.isNotEmpty() }?.let {
                        ingredientsList.add(it)
                        currentIngredient = ""
                    }
                },
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.add_ingredient_button), color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(Modifier.height(16.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (ingredientsList.isEmpty()) {
                    Text(
                        stringResource(R.string.no_ingredients),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                } else {
                    ingredientsList.forEachIndexed { i, ing ->
                        Text(
                            text = "${i + 1}. $ing",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { ingredientsList.removeAt(i) }
                                .padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (recipeName.isBlank() || instructions.isBlank() || ingredientsList.isEmpty()) return@Button
                    homeViewModel.addRecipe(
                        RecipeData(
                            name = recipeName.trim(),
                            instructions = instructions.trim(),
                            ingredients = ingredientsList.toList()
                        )
                    )
                    navController.popBackStack()
                },
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(stringResource(R.string.save_recipe_button), color = MaterialTheme.colorScheme.onSecondary)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    }
}
