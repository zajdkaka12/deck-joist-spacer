package com.deckjoist.spacer.presentation.app

import androidx.lifecycle.ViewModel
import com.deckjoist.spacer.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayDeque<Screen>()

    private val currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = currentScreen.asStateFlow()

    private val backAvailable = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = backAvailable.asStateFlow()

    fun navigate(target: Screen) {
        val current = currentScreen.value
        if (current == target) return
        if (current != Screen.Splash) backStack.addLast(current)
        currentScreen.value = target
        backAvailable.value = backStack.isNotEmpty()
    }

    fun replaceAll(target: Screen) {
        backStack.clear()
        currentScreen.value = target
        backAvailable.value = false
    }

    fun restartFlow(target: Screen) {
        backStack.clear()
        backStack.addLast(Screen.Menu)
        currentScreen.value = target
        backAvailable.value = true
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        currentScreen.value = previous
        backAvailable.value = backStack.isNotEmpty()
    }
}
