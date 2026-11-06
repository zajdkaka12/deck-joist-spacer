package com.deckjoist.spacer.data.repository

import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.data.sample.SampleData
import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DraftPlanRepositoryImpl : DraftPlanRepository {

    private val state = MutableStateFlow(newPlan(GameConfig.DEFAULT_WASTE_PERCENT))

    override val draft: StateFlow<DeckPlan> = state.asStateFlow()

    override fun startNew(wastePercent: Int) {
        state.value = newPlan(wastePercent)
    }

    override fun load(plan: DeckPlan) {
        state.value = plan
    }

    override fun update(transform: (DeckPlan) -> DeckPlan) {
        state.value = transform(state.value)
    }

    private fun newPlan(wastePercent: Int): DeckPlan = DeckPlan(
        id = PREFIX + System.currentTimeMillis().toString(),
        name = SampleData.DEFAULT_DECK_NAME,
        wastePercent = wastePercent
    )

    private companion object {
        const val PREFIX = "deck-"
    }
}
