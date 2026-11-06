package com.deckjoist.spacer.domain.repository

import com.deckjoist.spacer.domain.model.AppSettings
import com.deckjoist.spacer.domain.model.UnitSystem
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>

    fun setUnitSystem(system: UnitSystem)

    fun setDefaultWaste(percent: Int)

    fun setShowOnCentreDeltas(enabled: Boolean)

    fun setHapticOnSnap(enabled: Boolean)

    fun toggleCheckedNote(index: Int)
}
