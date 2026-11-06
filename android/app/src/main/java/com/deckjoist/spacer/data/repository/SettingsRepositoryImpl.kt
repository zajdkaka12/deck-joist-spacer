package com.deckjoist.spacer.data.repository

import com.deckjoist.spacer.data.local.SettingsPreferences
import com.deckjoist.spacer.domain.model.AppSettings
import com.deckjoist.spacer.domain.model.UnitSystem
import com.deckjoist.spacer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val preferences: SettingsPreferences) : SettingsRepository {

    private val state = MutableStateFlow(preferences.load())

    override val settings: StateFlow<AppSettings> = state.asStateFlow()

    override fun setUnitSystem(system: UnitSystem) = persist { it.copy(unitSystem = system) }

    override fun setDefaultWaste(percent: Int) = persist { it.copy(defaultWastePercent = percent) }

    override fun setShowOnCentreDeltas(enabled: Boolean) =
        persist { it.copy(showOnCentreDeltas = enabled) }

    override fun setHapticOnSnap(enabled: Boolean) = persist { it.copy(hapticOnSnap = enabled) }

    override fun toggleCheckedNote(index: Int) = persist { current ->
        val next = current.checkedNotes.toMutableSet()
        if (!next.add(index)) next.remove(index)
        current.copy(checkedNotes = next)
    }

    private fun persist(transform: (AppSettings) -> AppSettings) {
        val updated = transform(state.value)
        state.value = updated
        preferences.save(updated)
    }
}
