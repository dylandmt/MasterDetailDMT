package com.example.masterdetaildmt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.data.viewmodel.HomeViewModel
import com.example.masterdetaildmt.navigation.NavigationHost
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold { innerPadding ->
                NavigationHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    context = applicationContext
                )
            }
        }
    }
}
