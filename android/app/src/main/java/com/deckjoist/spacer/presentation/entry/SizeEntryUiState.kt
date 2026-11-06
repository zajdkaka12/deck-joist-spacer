package com.deckjoist.spacer.presentation.entry

import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.model.UnitSystem

data class SizeEntryUiState(
    val name: String = "",
    val lengthMm: Int = GameConfig.DEFAULT_LENGTH_MM,
    val widthMm: Int = GameConfig.DEFAULT_WIDTH_MM,
    val heightMm: Int = GameConfig.DEFAULT_HEIGHT_MM,
    val unitSystem: UnitSystem = UnitSystem.METRIC
) {
    val canDecreaseLength: Boolean get() = lengthMm > GameConfig.MIN_LENGTH_MM
    val canIncreaseLength: Boolean get() = lengthMm < GameConfig.MAX_LENGTH_MM
    val canDecreaseWidth: Boolean get() = widthMm > GameConfig.MIN_WIDTH_MM
    val canIncreaseWidth: Boolean get() = widthMm < GameConfig.MAX_WIDTH_MM
    val canDecreaseHeight: Boolean get() = heightMm > GameConfig.MIN_HEIGHT_MM
    val canIncreaseHeight: Boolean get() = heightMm < GameConfig.MAX_HEIGHT_MM
    val canContinue: Boolean
        get() = name.isNotBlank() &&
            lengthMm in GameConfig.MIN_LENGTH_MM..GameConfig.MAX_LENGTH_MM &&
            widthMm in GameConfig.MIN_WIDTH_MM..GameConfig.MAX_WIDTH_MM
}
