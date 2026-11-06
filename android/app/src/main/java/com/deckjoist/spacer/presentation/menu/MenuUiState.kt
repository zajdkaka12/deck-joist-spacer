package com.deckjoist.spacer.presentation.menu

import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.model.UnitSystem

data class MenuDeckRow(
    val plan: DeckPlan,
    val boardCount: Int
)

data class MenuUiState(
    val rows: List<MenuDeckRow> = emptyList(),
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val totalBoards: Int = 0,
    val pendingDelete: DeckPlan? = null
)
