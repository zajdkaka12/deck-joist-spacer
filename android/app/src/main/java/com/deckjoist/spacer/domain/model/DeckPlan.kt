package com.deckjoist.spacer.domain.model

import com.deckjoist.spacer.core.config.GameConfig

data class DeckPlan(
    val id: String,
    val name: String,
    val lengthMm: Int = GameConfig.DEFAULT_LENGTH_MM,
    val widthMm: Int = GameConfig.DEFAULT_WIDTH_MM,
    val heightMm: Int = GameConfig.DEFAULT_HEIGHT_MM,
    val spacingMm: Int = GameConfig.DEFAULT_SPACING_MM,
    val direction: JoistDirection = JoistDirection.ACROSS_WIDTH,
    val joistDepthMm: Int = GameConfig.DEFAULT_JOIST_DEPTH_MM,
    val boardWidthMm: Int = GameConfig.DEFAULT_BOARD_WIDTH_MM,
    val gapMm: Int = GameConfig.DEFAULT_GAP_MM,
    val wastePercent: Int = GameConfig.DEFAULT_WASTE_PERCENT,
    val notes: String = ""
)
