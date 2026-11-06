package com.deckjoist.spacer.domain.repository

import com.deckjoist.spacer.domain.model.DeckPlan
import kotlinx.coroutines.flow.StateFlow

interface DeckRepository {
    val decks: StateFlow<List<DeckPlan>>

    fun save(plan: DeckPlan)

    fun delete(id: String)
}
