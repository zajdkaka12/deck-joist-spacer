package com.deckjoist.spacer.domain.usecase

import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.model.DeckReport
import com.deckjoist.spacer.domain.model.MaterialItem
import com.deckjoist.spacer.domain.model.MaterialKind

class BuildMaterialListUseCase(
    private val calculateJoists: CalculateJoistLayoutUseCase,
    private val calculateBoards: CalculateBoardPlanUseCase,
    private val calculateFasteners: CalculateFastenersUseCase
) {

    operator fun invoke(plan: DeckPlan): DeckReport {
        val joists = calculateJoists(plan)
        val boards = calculateBoards(plan, joists)
        val fasteners = calculateFasteners(joists, boards, plan.wastePercent)
        val waste = plan.wastePercent

        val materials = listOf(
            MaterialItem(
                kind = MaterialKind.JOIST,
                quantity = applyWaste(joists.interiorJoists, waste),
                depthMm = plan.joistDepthMm,
                widthMm = GameConfig.JOIST_WIDTH_MM,
                lengthMm = joists.spanMm
            ),
            MaterialItem(
                kind = MaterialKind.RIM_JOIST,
                quantity = 2,
                depthMm = plan.joistDepthMm,
                widthMm = GameConfig.JOIST_WIDTH_MM,
                lengthMm = joists.runMm
            ),
            MaterialItem(
                kind = MaterialKind.BEAM,
                quantity = joists.beamPieces,
                depthMm = plan.joistDepthMm,
                widthMm = GameConfig.JOIST_WIDTH_MM,
                lengthMm = joists.runMm
            ),
            MaterialItem(
                kind = MaterialKind.DECK_BOARD,
                quantity = applyWaste(boards.boardPieces, waste),
                depthMm = plan.boardWidthMm,
                widthMm = GameConfig.BOARD_THICKNESS_MM,
                lengthMm = GameConfig.STOCK_LENGTH_MM
            ),
            MaterialItem(
                kind = MaterialKind.POST,
                quantity = joists.postCount,
                depthMm = GameConfig.POST_SECTION_MM,
                widthMm = GameConfig.POST_SECTION_MM,
                lengthMm = joists.postLengthMm
            ),
            MaterialItem(
                kind = MaterialKind.JOIST_HANGER,
                quantity = fasteners.hangersWithWaste,
                depthMm = plan.joistDepthMm,
                widthMm = 0,
                lengthMm = 0
            ),
            MaterialItem(
                kind = MaterialKind.POST_BRACKET,
                quantity = fasteners.bracketsWithWaste,
                depthMm = GameConfig.POST_SECTION_MM,
                widthMm = 0,
                lengthMm = 0
            ),
            MaterialItem(
                kind = MaterialKind.DECK_SCREW,
                quantity = fasteners.screwsWithWaste,
                depthMm = GameConfig.DECK_SCREW_MM,
                widthMm = 0,
                lengthMm = 0
            ),
            MaterialItem(
                kind = MaterialKind.STRUCTURAL_SCREW,
                quantity = fasteners.structuralWithWaste,
                depthMm = GameConfig.STRUCTURAL_SCREW_MM,
                widthMm = 0,
                lengthMm = 0
            )
        ).filter { item -> item.quantity > 0 }

        val linealMm = joists.joistCount.toLong() * joists.spanMm +
            2L * joists.runMm +
            joists.beamPieces.toLong() * joists.runMm +
            boards.boardPieces.toLong() * GameConfig.STOCK_LENGTH_MM

        return DeckReport(
            plan = plan,
            joists = joists,
            boards = boards,
            fasteners = fasteners,
            materials = materials,
            linealMetres = linealMm / 1000f
        )
    }
}
