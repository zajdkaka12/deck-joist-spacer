package com.deckjoist.spacer.data.sample

import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.model.JoistDirection

object SampleData {

    const val DEFAULT_DECK_NAME = "BACK DECK"

    val decks: List<DeckPlan> = listOf(
        DeckPlan(
            id = "deck-back",
            name = "BACK DECK",
            lengthMm = 4800,
            widthMm = 3600,
            heightMm = 600,
            spacingMm = 400,
            direction = JoistDirection.ACROSS_WIDTH,
            joistDepthMm = 190,
            boardWidthMm = 140,
            gapMm = 5,
            wastePercent = 10,
            notes = "SOUTH FACING, FALL AWAY FROM THE KITCHEN DOOR"
        ),
        DeckPlan(
            id = "deck-landing",
            name = "SIDE LANDING",
            lengthMm = 2400,
            widthMm = 1800,
            heightMm = 450,
            spacingMm = 450,
            direction = JoistDirection.ACROSS_WIDTH,
            joistDepthMm = 140,
            boardWidthMm = 120,
            gapMm = 5,
            wastePercent = 10,
            notes = "STEP DOWN TO THE PATH, TWO RISERS"
        ),
        DeckPlan(
            id = "deck-pool",
            name = "POOL SURROUND",
            lengthMm = 6000,
            widthMm = 2400,
            heightMm = 300,
            spacingMm = 300,
            direction = JoistDirection.ALONG_LENGTH,
            joistDepthMm = 240,
            boardWidthMm = 90,
            gapMm = 8,
            wastePercent = 15,
            notes = "WIDER GAPS FOR DRAINAGE, STAINLESS FIXINGS"
        )
    )
}
