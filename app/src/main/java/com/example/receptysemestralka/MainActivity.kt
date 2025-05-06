package com.example.receptysemestralka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.receptysemestralka.ui.theme.ReceptySemestralkaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReceptySemestralkaTheme {
                // Surface so systémovou farbou pozadia
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Column na usporiadanie prvkov vertikálne
                    Column(
                        modifier = Modifier
                            .fillMaxSize()               // zabere celú plochu obrazovky
                            .padding(top=50.dp,start = 16.dp, end = 16.dp),             // okraje
                        horizontalAlignment = Alignment.CenterHorizontally  // vycentrovanie horizontálne
                    ) {
                        Text(
                            text = "RECEPTY PODĽA MOŽNOSTÍ",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold, // veľkosť písma (môžeš upraviť)
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge  // štýl z témy
                        )
                        // Sem neskôr pridáme ďalšie prvky (TextField, tlačidlá...)
                    }
                }
            }
        }
    }
}
