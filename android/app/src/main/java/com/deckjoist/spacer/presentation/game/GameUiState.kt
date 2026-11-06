package com.deckjoist.spacer.presentation.game

import com.deckjoist.spacer.domain.model.DeckReport
import com.deckjoist.spacer.domain.model.UnitSystem

data class GameUiState(
    val report: DeckReport? = null,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val hapticEnabled: Boolean = true,
    val highlight: Int = 0,
    val revision: Int = 0,
    val selectedJoist: Int? = null
)
