package com.deckjoist.spacer.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.repository.DeckRepository
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import com.deckjoist.spacer.domain.repository.SettingsRepository
import com.deckjoist.spacer.domain.usecase.BuildMaterialListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val deckRepository: DeckRepository,
    private val settingsRepository: SettingsRepository,
    private val draftPlanRepository: DraftPlanRepository,
    private val buildMaterialList: BuildMaterialListUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(deckRepository.decks, settingsRepository.settings) { decks, settings ->
                val rows = decks.map { plan ->
                    MenuDeckRow(plan, buildMaterialList(plan).boards.boardPieces)
                }
                rows to settings.unitSystem
            }.collect { pair ->
                uiState.update { current ->
                    current.copy(
                        rows = pair.first,
                        unitSystem = pair.second,
                        totalBoards = pair.first.sumOf { row -> row.boardCount }
                    )
                }
            }
        }
    }

    fun startNewDeck() {
        draftPlanRepository.startNew(settingsRepository.settings.value.defaultWastePercent)
    }

    fun openDeck(plan: DeckPlan) {
        draftPlanRepository.load(plan)
    }

    fun requestDelete(plan: DeckPlan) {
        uiState.update { it.copy(pendingDelete = plan) }
    }

    fun dismissDelete() {
        uiState.update { it.copy(pendingDelete = null) }
    }

    fun confirmDelete() {
        val target = uiState.value.pendingDelete ?: return
        deckRepository.delete(target.id)
        uiState.update { it.copy(pendingDelete = null) }
    }
}
