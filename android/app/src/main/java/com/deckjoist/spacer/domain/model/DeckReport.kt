package com.deckjoist.spacer.domain.model

data class DeckReport(
    val plan: DeckPlan,
    val joists: JoistLayout,
    val boards: BoardLayout,
    val fasteners: FastenerTally,
    val materials: List<MaterialItem>,
    val linealMetres: Float
)
