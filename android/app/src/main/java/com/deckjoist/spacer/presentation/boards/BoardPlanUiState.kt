package com.deckjoist.spacer.presentation.boards

import com.deckjoist.spacer.domain.model.DeckReport
import com.deckjoist.spacer.domain.model.UnitSystem

data class BoardPlanUiState(
    val report: DeckReport? = null,
    val unitSystem: UnitSystem = UnitSystem.METRIC
)
