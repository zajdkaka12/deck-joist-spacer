package com.deckjoist.spacer.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deckjoist.spacer.presentation.app.AppViewModel
import com.deckjoist.spacer.presentation.boards.BoardPlanViewModel
import com.deckjoist.spacer.presentation.entry.SizeEntryViewModel
import com.deckjoist.spacer.presentation.fasteners.FastenerCountViewModel
import com.deckjoist.spacer.presentation.game.GameViewModel
import com.deckjoist.spacer.presentation.joist.JoistSetupViewModel
import com.deckjoist.spacer.presentation.marks.LayoutMarksViewModel
import com.deckjoist.spacer.presentation.menu.MenuViewModel
import com.deckjoist.spacer.presentation.notes.BuildNotesViewModel
import com.deckjoist.spacer.presentation.options.UnitOptionsViewModel
import com.deckjoist.spacer.presentation.result.MaterialResultViewModel
import com.deckjoist.spacer.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val decks = ServiceLocator.deckRepository
        val settings = ServiceLocator.settingsRepository
        val draft = ServiceLocator.draftPlanRepository
        val report = ServiceLocator.buildMaterialList

        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(decks, settings, draft, report)
            modelClass.isAssignableFrom(SizeEntryViewModel::class.java) ->
                SizeEntryViewModel(draft, settings)
            modelClass.isAssignableFrom(JoistSetupViewModel::class.java) ->
                JoistSetupViewModel(draft, settings, report)
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(draft, settings, report)
            modelClass.isAssignableFrom(LayoutMarksViewModel::class.java) ->
                LayoutMarksViewModel(draft, settings, report)
            modelClass.isAssignableFrom(BoardPlanViewModel::class.java) ->
                BoardPlanViewModel(draft, settings, report)
            modelClass.isAssignableFrom(FastenerCountViewModel::class.java) ->
                FastenerCountViewModel(draft, settings, report)
            modelClass.isAssignableFrom(MaterialResultViewModel::class.java) ->
                MaterialResultViewModel(draft, settings, decks, report)
            modelClass.isAssignableFrom(BuildNotesViewModel::class.java) ->
                BuildNotesViewModel(draft, settings)
            modelClass.isAssignableFrom(UnitOptionsViewModel::class.java) ->
                UnitOptionsViewModel(settings, draft)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
