package com.deckjoist.spacer.data.local

import android.content.Context
import android.content.SharedPreferences
import com.deckjoist.spacer.domain.model.DeckPlan
import com.deckjoist.spacer.domain.model.JoistDirection

class DeckPreferences(context: Context) {

    private val store: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun load(): List<DeckPlan>? {
        if (!store.contains(KEY_DECKS)) return null
        val raw = store.getString(KEY_DECKS, "").orEmpty()
        if (raw.isBlank()) return emptyList()
        return raw.split(RECORD_SEPARATOR).mapNotNull { decode(it) }
    }

    fun save(decks: List<DeckPlan>) {
        store.edit().putString(KEY_DECKS, decks.joinToString(RECORD_SEPARATOR) { encode(it) }).apply()
    }

    private fun encode(plan: DeckPlan): String = listOf(
        plan.id,
        clean(plan.name),
        plan.lengthMm.toString(),
        plan.widthMm.toString(),
        plan.heightMm.toString(),
        plan.spacingMm.toString(),
        plan.direction.name,
        plan.joistDepthMm.toString(),
        plan.boardWidthMm.toString(),
        plan.gapMm.toString(),
        plan.wastePercent.toString(),
        clean(plan.notes)
    ).joinToString(FIELD_SEPARATOR)

    private fun decode(record: String): DeckPlan? {
        val parts = record.split(FIELD_SEPARATOR)
        if (parts.size < 12) return null
        return DeckPlan(
            id = parts[0],
            name = parts[1],
            lengthMm = parts[2].toIntOrNull() ?: return null,
            widthMm = parts[3].toIntOrNull() ?: return null,
            heightMm = parts[4].toIntOrNull() ?: return null,
            spacingMm = parts[5].toIntOrNull() ?: return null,
            direction = runCatching { JoistDirection.valueOf(parts[6]) }
                .getOrDefault(JoistDirection.ACROSS_WIDTH),
            joistDepthMm = parts[7].toIntOrNull() ?: return null,
            boardWidthMm = parts[8].toIntOrNull() ?: return null,
            gapMm = parts[9].toIntOrNull() ?: return null,
            wastePercent = parts[10].toIntOrNull() ?: return null,
            notes = parts[11]
        )
    }

    private fun clean(value: String): String =
        value.replace(FIELD_SEPARATOR, " ").replace(RECORD_SEPARATOR, " ").trim()

    private companion object {
        const val FILE_NAME = "deck_joist_spacer_decks"
        const val KEY_DECKS = "saved_decks"
        const val FIELD_SEPARATOR = "|"
        const val RECORD_SEPARATOR = "\n"
    }
}
