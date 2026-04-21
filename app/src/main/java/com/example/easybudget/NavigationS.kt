package com.example.easybudget

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostS(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homes"){
        composable(route="homes"){
            HomeS(navController)
        }
        composable(route="add"){
            AddTran(navController)
        }
    }
}