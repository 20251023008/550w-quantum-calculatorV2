package com.a550w.quantumcomputer

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Calculator : Screen("calculator")
    data object Computing : Screen("computing")
    data object Error : Screen("error/{message}") {
        fun createRoute(message: String) = "error/$message"
    }
    data object HandCounting : Screen("hand_counting/{result}") {
        fun createRoute(result: Int) = "hand_counting/$result"
    }
}
