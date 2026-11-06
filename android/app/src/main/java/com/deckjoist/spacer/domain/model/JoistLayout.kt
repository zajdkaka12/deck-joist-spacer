package com.deckjoist.spacer.domain.model

data class JoistLayout(
    val spanMm: Int,
    val runMm: Int,
    val spacingMm: Int,
    val joistWidthMm: Int,
    val joistDepthMm: Int,
    val marksMm: List<Int>,
    val joistCount: Int,
    val interiorJoists: Int,
    val beamLines: Int,
    val beamPieces: Int,
    val postsPerBeam: Int,
    val postCount: Int,
    val postLengthMm: Int
)
