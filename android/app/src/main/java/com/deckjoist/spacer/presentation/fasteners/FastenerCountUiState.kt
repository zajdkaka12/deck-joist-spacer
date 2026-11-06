package com.deckjoist.spacer.presentation.fasteners

import com.deckjoist.spacer.domain.model.DeckReport
import com.deckjoist.spacer.domain.model.UnitSystem

data class FastenerCountUiState(
    val report: DeckReport? = null,
    val unitSystem: UnitSystem = UnitSystem.METRIC
)
