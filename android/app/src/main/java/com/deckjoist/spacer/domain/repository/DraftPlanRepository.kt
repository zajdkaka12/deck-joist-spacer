package com.deckjoist.spacer.domain.repository

import com.deckjoist.spacer.domain.model.DeckPlan
import kotlinx.coroutines.flow.StateFlow

interface DraftPlanRepository {
    val draft: StateFlow<DeckPlan>

    fun startNew(wastePercent: Int)

    fun load(plan: DeckPlan)

    fun update(transform: (DeckPlan) -> DeckPlan)
}
