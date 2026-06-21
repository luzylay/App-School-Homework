package com.example.appcolegioclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appcolegioclass.navegacion.Navegar
import com.example.appcolegioclass.ui.theme.AppColegioClassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppColegioClassTheme {
                Navegar()
            }
        }
    }
}
