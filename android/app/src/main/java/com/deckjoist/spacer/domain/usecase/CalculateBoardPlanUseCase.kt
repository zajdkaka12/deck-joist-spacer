package com.deckjoist.spacer.domain.usecase

import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.model.BoardLayout
import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.model.JoistLayout
import kotlin.math.max

class CalculateBoardPlanUseCase {

    operator fun invoke(plan: DeckPlan, joists: JoistLayout): BoardLayout {
        val pitch = max(1, plan.boardWidthMm + plan.gapMm)
        val rows = max(1, ceilDiv(joists.runMm + plan.gapMm, pitch))
        val lastWidth = max(0, joists.runMm - (rows - 1) * pitch)
        val piecesPerRow = max(1, ceilDiv(joists.spanMm, GameConfig.STOCK_LENGTH_MM))

        return BoardLayout(
            rows = rows,
            pitchMm = pitch,
            boardWidthMm = plan.boardWidthMm,
            gapMm = plan.gapMm,
            lastBoardWidthMm = lastWidth,
            needsRip = lastWidth < plan.boardWidthMm - 1,
            piecesPerRow = piecesPerRow,
            boardPieces = rows * piecesPerRow,
            runMm = joists.runMm,
            spanMm = joists.spanMm
        )
    }
}
