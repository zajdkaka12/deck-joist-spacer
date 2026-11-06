package com.deckjoist.spacer.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.navigation.Screen
import com.deckjoist.spacer.core.theme.MossBackground
import com.deckjoist.spacer.presentation.boards.BoardPlanScreen
import com.deckjoist.spacer.presentation.entry.SizeEntryScreen
import com.deckjoist.spacer.presentation.fasteners.FastenerCountScreen
import com.deckjoist.spacer.presentation.game.GameScreen
import com.deckjoist.spacer.presentation.joist.JoistSetupScreen
import com.deckjoist.spacer.presentation.marks.LayoutMarksScreen
import com.deckjoist.spacer.presentation.menu.MenuScreen
import com.deckjoist.spacer.presentation.notes.BuildNotesScreen
import com.deckjoist.spacer.presentation.options.UnitOptionsScreen
import com.deckjoist.spacer.presentation.result.MaterialResultScreen
import com.deckjoist.spacer.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MossBackground)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                if (initialState == Screen.Splash) {
                    fadeIn(tween(320)) togetherWith fadeOut(tween(240))
                } else {
                    (
                        fadeIn(tween(240)) +
                            slideInHorizontally(tween(300)) { full -> full / 5 }
                        ) togetherWith (
                        fadeOut(tween(240)) +
                            slideOutHorizontally(tween(300)) { full -> -full / 6 }
                        )
                }
            }
        ) { target ->
            when (target) {
                Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.replaceAll(Screen.Menu) }
                )

                Screen.Menu -> MenuScreen(
                    onStartNewDeck = { appViewModel.navigate(Screen.SizeEntry) },
                    onOpenDeck = { appViewModel.navigate(Screen.Game) },
                    onUnitOptions = { appViewModel.navigate(Screen.UnitOptions) }
                )

                Screen.SizeEntry -> SizeEntryScreen(
                    onBack = { appViewModel.back() },
                    onNext = { appViewModel.navigate(Screen.JoistSetup) }
                )

                Screen.JoistSetup -> JoistSetupScreen(
                    onBack = { appViewModel.back() },
                    onDrawPlan = { appViewModel.navigate(Screen.Game) }
                )

                Screen.Game -> GameScreen(
                    onBack = { appViewModel.back() },
                    onMarks = { appViewModel.navigate(Screen.LayoutMarks) },
                    onBoards = { appViewModel.navigate(Screen.BoardPlan) },
                    onFasteners = { appViewModel.navigate(Screen.FastenerCount) },
                    onMaterialList = { appViewModel.navigate(Screen.Result) },
                    onNotes = { appViewModel.navigate(Screen.BuildNotes) }
                )

                Screen.LayoutMarks -> LayoutMarksScreen(
                    onBack = { appViewModel.back() }
                )

                Screen.BoardPlan -> BoardPlanScreen(
                    onBack = { appViewModel.back() }
                )

                Screen.FastenerCount -> FastenerCountScreen(
                    onBack = { appViewModel.back() },
                    onMaterialList = { appViewModel.navigate(Screen.Result) }
                )

                Screen.Result -> MaterialResultScreen(
                    onBack = { appViewModel.back() },
                    onPlanAgain = { appViewModel.restartFlow(Screen.SizeEntry) },
                    onNotes = { appViewModel.navigate(Screen.BuildNotes) },
                    onMenu = { appViewModel.replaceAll(Screen.Menu) }
                )

                Screen.BuildNotes -> BuildNotesScreen(
                    onBack = { appViewModel.back() },
                    onMenu = { appViewModel.replaceAll(Screen.Menu) }
                )

                Screen.UnitOptions -> UnitOptionsScreen(
                    onBack = { appViewModel.back() },
                    onMenu = { appViewModel.replaceAll(Screen.Menu) }
                )
            }
        }
    }
}
