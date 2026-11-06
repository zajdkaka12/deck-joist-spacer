package com.deckjoist.spacer.domain.model

data class FastenerTally(
    val rows: Int,
    val joistCount: Int,
    val interiorJoists: Int,
    val postCount: Int,
    val wastePercent: Int,
    val screws: Int,
    val hangers: Int,
    val brackets: Int,
    val structuralScrews: Int,
    val screwsWithWaste: Int,
    val hangersWithWaste: Int,
    val bracketsWithWaste: Int,
    val structuralWithWaste: Int,
    val screwBoxes: Int,
    val screwBoxSize: Int
)
