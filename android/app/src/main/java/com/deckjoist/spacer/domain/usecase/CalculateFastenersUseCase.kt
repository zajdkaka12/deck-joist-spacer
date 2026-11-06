package com.deckjoist.spacer.domain.usecase

import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.model.BoardLayout
import com.deckjoist.spacer.domain.model.FastenerTally
import com.deckjoist.spacer.domain.model.JoistLayout
import kotlin.math.ceil
import kotlin.math.max

class CalculateFastenersUseCase {

    operator fun invoke(joists: JoistLayout, boards: BoardLayout, wastePercent: Int): FastenerTally {
        val screws = boards.rows * joists.joistCount * 2
        val hangers = joists.interiorJoists * 2
        val brackets = joists.postCount
        val structural = hangers * 2 + joists.postCount * 4
        val screwsWithWaste = applyWaste(screws, wastePercent)
        val boxes = max(1, ceilDiv(screwsWithWaste, GameConfig.SCREW_BOX_SIZE))

        return FastenerTally(
            rows = boards.rows,
            joistCount = joists.joistCount,
            interiorJoists = joists.interiorJoists,
            postCount = joists.postCount,
            wastePercent = wastePercent,
            screws = screws,
            hangers = hangers,
            brackets = brackets,
            structuralScrews = structural,
            screwsWithWaste = screwsWithWaste,
            hangersWithWaste = applyWaste(hangers, wastePercent),
            bracketsWithWaste = applyWaste(brackets, wastePercent),
            structuralWithWaste = applyWaste(structural, wastePercent),
            screwBoxes = boxes,
            screwBoxSize = GameConfig.SCREW_BOX_SIZE
        )
    }
}

internal fun applyWaste(amount: Int, wastePercent: Int): Int {
    if (amount <= 0) return 0
    return ceil(amount.toDouble() * (100 + wastePercent) / 100.0).toInt()
}
