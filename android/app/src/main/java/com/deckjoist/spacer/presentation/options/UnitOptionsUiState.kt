package com.deckjoist.spacer.presentation.options

import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.model.UnitSystem

data class UnitOptionsUiState(
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val wasteIndex: Int = 1,
    val showOnCentreDeltas: Boolean = true,
    val hapticOnSnap: Boolean = true,
    val previewMm: Int = GameConfig.DEFAULT_LENGTH_MM
)
