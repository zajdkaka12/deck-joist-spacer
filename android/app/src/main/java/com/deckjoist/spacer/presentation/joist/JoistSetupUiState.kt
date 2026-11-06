package com.deckjoist.spacer.presentation.joist

import com.deckjoist.spacer.domain.model.DeckReport
import com.deckjoist.spacer.domain.model.JoistDirection
import com.deckjoist.spacer.domain.model.UnitSystem

object JoistOptions {
    val spacing = listOf(300, 400, 450, 600)
    val depth = listOf(140, 190, 240)
    val boardWidth = listOf(90, 120, 140)
    val gap = listOf(3, 5, 8)
    val waste = listOf(5, 10, 15)
    val directions = listOf(JoistDirection.ACROSS_WIDTH, JoistDirection.ALONG_LENGTH)
}

data class JoistSetupUiState(
    val report: DeckReport? = null,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val spacingIndex: Int = 1,
    val directionIndex: Int = 0,
    val depthIndex: Int = 1,
    val boardWidthIndex: Int = 2,
    val gapIndex: Int = 1,
    val wasteIndex: Int = 1
)
