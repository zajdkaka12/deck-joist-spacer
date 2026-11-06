package com.deckjoist.spacer.domain.usecase

import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.model.JoistDirection
import com.deckjoist.spacer.domain.model.JoistLayout
import kotlin.math.max
import kotlin.math.min

class CalculateJoistLayoutUseCase {

    operator fun invoke(plan: DeckPlan): JoistLayout {
        val acrossWidth = plan.direction == JoistDirection.ACROSS_WIDTH
        val spanMm = if (acrossWidth) plan.widthMm else plan.lengthMm
        val runMm = if (acrossWidth) plan.lengthMm else plan.widthMm
        val spacing = max(1, plan.spacingMm)

        val bays = ceilDiv(runMm, spacing)
        val interior = max(0, bays - 1)
        val count = interior + 2
        val lastUsable = max(0, runMm - GameConfig.JOIST_WIDTH_MM)
        val marks = (0 until count).map { index -> min(index * spacing, lastUsable) }

        val beamLines = max(2, ceilDiv(spanMm, GameConfig.BEAM_MAX_SPAN_MM) + 1)
        val beamPieces = beamLines * 2
        val postsPerBeam = runMm / GameConfig.POST_MAX_SPACING_MM + 1
        val postCount = beamLines * postsPerBeam

        return JoistLayout(
            spanMm = spanMm,
            runMm = runMm,
            spacingMm = spacing,
            joistWidthMm = GameConfig.JOIST_WIDTH_MM,
            joistDepthMm = plan.joistDepthMm,
            marksMm = marks,
            joistCount = count,
            interiorJoists = interior,
            beamLines = beamLines,
            beamPieces = beamPieces,
            postsPerBeam = postsPerBeam,
            postCount = postCount,
            postLengthMm = plan.heightMm + GameConfig.POST_EMBEDMENT_MM
        )
    }
}

internal fun ceilDiv(value: Int, divisor: Int): Int {
    if (divisor <= 0) return 0
    return (value + divisor - 1) / divisor
}
