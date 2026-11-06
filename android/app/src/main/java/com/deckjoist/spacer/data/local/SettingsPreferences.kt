package com.deckjoist.spacer.data.local

import android.content.Context
import android.content.SharedPreferences
import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.model.AppSettings
import com.deckjoist.spacer.domain.model.UnitSystem

class SettingsPreferences(context: Context) {

    private val store: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun load(): AppSettings {
        val unitName = store.getString(KEY_UNITS, UnitSystem.METRIC.name).orEmpty()
        val checked = store.getString(KEY_CHECKED_NOTES, "").orEmpty()
        return AppSettings(
            unitSystem = runCatching { UnitSystem.valueOf(unitName) }.getOrDefault(UnitSystem.METRIC),
            defaultWastePercent = store.getInt(KEY_WASTE, GameConfig.DEFAULT_WASTE_PERCENT),
            showOnCentreDeltas = store.getBoolean(KEY_DELTAS, true),
            hapticOnSnap = store.getBoolean(KEY_HAPTIC, true),
            checkedNotes = checked.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        )
    }

    fun save(settings: AppSettings) {
        store.edit()
            .putString(KEY_UNITS, settings.unitSystem.name)
            .putInt(KEY_WASTE, settings.defaultWastePercent)
            .putBoolean(KEY_DELTAS, settings.showOnCentreDeltas)
            .putBoolean(KEY_HAPTIC, settings.hapticOnSnap)
            .putString(KEY_CHECKED_NOTES, settings.checkedNotes.sorted().joinToString(","))
            .apply()
    }

    private companion object {
        const val FILE_NAME = "deck_joist_spacer_settings"
        const val KEY_UNITS = "unit_system"
        const val KEY_WASTE = "default_waste"
        const val KEY_DELTAS = "show_deltas"
        const val KEY_HAPTIC = "haptic_on_snap"
        const val KEY_CHECKED_NOTES = "checked_notes"
    }
}
