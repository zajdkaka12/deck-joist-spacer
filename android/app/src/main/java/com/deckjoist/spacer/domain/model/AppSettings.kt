package com.deckjoist.spacer.domain.model

import com.deckjoist.spacer.core.config.GameConfig

data class AppSettings(
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val defaultWastePercent: Int = GameConfig.DEFAULT_WASTE_PERCENT,
    val showOnCentreDeltas: Boolean = true,
    val hapticOnSnap: Boolean = true,
    val checkedNotes: Set<Int> = emptySet()
)
