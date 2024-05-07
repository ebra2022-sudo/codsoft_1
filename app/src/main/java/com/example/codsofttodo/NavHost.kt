package com.example.codsofttodo

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/*
The first step in adding navigation to an app project is to
create a NavHostController instance. This is responsible for
managing the back stack and keeping track of which
composable is the current destination.
 */
@Composable
fun ScreenContainer() {
    
    val navController = rememberNavController() // this function creates the navController instance
    NavHost(navController = navController , startDestination = "get started" ) {
        composable("get started") {
            GetStartScreen(navController = navController)
        }
        composable("todo screen") {
            TodoScreen()
        }
        composable("new task") {
            NewTaskScreen(navController = navController)
        }
        composable("todo list Screen") {
            val owner = LocalViewModelStoreOwner.current
            owner?.let {
                val viewModel: TodoViewModel = viewModel(
                    it,
                    "TodoViewModel",
                    MainViewModelFactory(
                        LocalContext.current.applicationContext as Application
                    )
                )
                ScreenSetUp(mainViewModel = viewModel, navController = navController)
            }
            
        }
    }

}