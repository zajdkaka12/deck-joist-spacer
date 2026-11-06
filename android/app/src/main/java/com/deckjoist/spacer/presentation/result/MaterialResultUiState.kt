package com.deckjoist.spacer.presentation.result

import com.deckjoist.spacer.domain.model.DeckReport
import com.deckjoist.spacer.domain.model.MaterialKind
import com.deckjoist.spacer.domain.model.UnitSystem

data class MaterialResultUiState(
    val report: DeckReport? = null,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val ticked: Set<MaterialKind> = emptySet()
)

sealed interface MaterialResultEvent {
    data object PlanStored : MaterialResultEvent
}
