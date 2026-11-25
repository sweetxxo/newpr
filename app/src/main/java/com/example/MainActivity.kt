package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.PRPlannerScreen
import com.example.ui.PRPlannerViewModel
import com.example.ui.theme.PRPlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PRPlannerTheme {
                val vm: PRPlannerViewModel = viewModel()
                PRPlannerScreen(viewModel = vm)
            }
        }
    }
}
