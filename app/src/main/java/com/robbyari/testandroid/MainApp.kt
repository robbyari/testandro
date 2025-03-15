package com.robbyari.testandroid

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.robbyari.testandroid.navigation.Screen
import com.robbyari.testandroid.ui.home.HomeScreen
import com.robbyari.testandroid.ui.test1.TestScreen1
import com.robbyari.testandroid.ui.test2.TestScreen2
import com.robbyari.testandroid.ui.test3.TestScreen3
import com.robbyari.testandroid.ui.test4.TestScreen4

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navigateToTest1 = {
                    navController.navigate(Screen.Test1.route)
                },
                navigateToTest2 = {
                    navController.navigate(Screen.Test2.route)
                },
                navigateToTest3 = {
                    navController.navigate(Screen.Test3.route)
                },
                navigateToTest4 = {
                    navController.navigate(Screen.Test4.route)
                }
            )
        }
        composable(Screen.Test1.route) {
            TestScreen1()
        }
        composable(Screen.Test2.route) {
            TestScreen2()
        }
        composable(Screen.Test3.route) {
            TestScreen3()
        }
        composable(Screen.Test4.route) {
            TestScreen4()
        }
    }
}