package com.deckjoist.spacer.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object SizeEntry : Screen
    data object JoistSetup : Screen
    data object Game : Screen
    data object LayoutMarks : Screen
    data object BoardPlan : Screen
    data object FastenerCount : Screen
    data object Result : Screen
    data object BuildNotes : Screen
    data object UnitOptions : Screen
}
