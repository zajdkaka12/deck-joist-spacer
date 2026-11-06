package com.deckjoist.spacer.data.repository

import com.deckjoist.spacer.data.local.DeckPreferences
import com.deckjoist.spacer.data.sample.SampleData
import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.repository.DeckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DeckRepositoryImpl(private val preferences: DeckPreferences) : DeckRepository {

    private val stored = preferences.load()
    private val state = MutableStateFlow(stored ?: SampleData.decks)

    override val decks: StateFlow<List<DeckPlan>> = state.asStateFlow()

    init {
        if (stored == null) preferences.save(state.value)
    }

    override fun save(plan: DeckPlan) {
        val existing = state.value
        val index = existing.indexOfFirst { it.id == plan.id }
        val updated = if (index >= 0) {
            existing.toMutableList().also { it[index] = plan }
        } else {
            listOf(plan) + existing
        }
        state.value = updated
        preferences.save(updated)
    }

    override fun delete(id: String) {
        val updated = state.value.filterNot { it.id == id }
        state.value = updated
        preferences.save(updated)
    }
}
