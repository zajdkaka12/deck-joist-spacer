package com.deckjoist.spacer.presentation.marks

import com.deckjoist.spacer.domain.model.DeckReport
import com.deckjoist.spacer.domain.model.UnitSystem

data class LayoutMarksUiState(
    val report: DeckReport? = null,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val showDeltas: Boolean = true
)
