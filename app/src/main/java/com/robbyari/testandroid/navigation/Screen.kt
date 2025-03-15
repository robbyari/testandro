package com.robbyari.testandroid.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Test1 : Screen("test1")
    object Test2 : Screen("test2")
    object Test3 : Screen("test3")
    object Test4 : Screen("test4")
}