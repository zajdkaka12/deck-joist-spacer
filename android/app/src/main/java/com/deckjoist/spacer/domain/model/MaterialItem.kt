package com.deckjoist.spacer.domain.model

enum class MaterialKind {
    JOIST,
    RIM_JOIST,
    BEAM,
    DECK_BOARD,
    POST,
    JOIST_HANGER,
    POST_BRACKET,
    DECK_SCREW,
    STRUCTURAL_SCREW
}

data class MaterialItem(
    val kind: MaterialKind,
    val quantity: Int,
    val depthMm: Int,
    val widthMm: Int,
    val lengthMm: Int
)
