package com.deckjoist.spacer.domain.model

data class BoardLayout(
    val rows: Int,
    val pitchMm: Int,
    val boardWidthMm: Int,
    val gapMm: Int,
    val lastBoardWidthMm: Int,
    val needsRip: Boolean,
    val piecesPerRow: Int,
    val boardPieces: Int,
    val runMm: Int,
    val spanMm: Int
)
