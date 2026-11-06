package com.deckjoist.spacer.presentation.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deckjoist.spacer.domain.model.UnitSystem
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import com.deckjoist.spacer.domain.repository.SettingsRepository
import com.deckjoist.spacer.presentation.joist.JoistOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class UnitOptionsViewModel(
    private val settingsRepository: SettingsRepository,
    draftPlanRepository: DraftPlanRepository
) : ViewModel() {

    private val uiState = MutableStateFlow(UnitOptionsUiState())
    val state: StateFlow<UnitOptionsUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(settingsRepository.settings, draftPlanRepository.draft) { settings, plan ->
                UnitOptionsUiState(
                    unitSystem = settings.unitSystem,
                    wasteIndex = JoistOptions.waste
                        .indexOf(settings.defaultWastePercent)
                        .coerceAtLeast(0),
                    showOnCentreDeltas = settings.showOnCentreDeltas,
                    hapticOnSnap = settings.hapticOnSnap,
                    previewMm = plan.lengthMm
                )
            }.collect { next ->
                uiState.value = next
            }
        }
    }

    fun selectUnitSystem(index: Int) {
        settingsRepository.setUnitSystem(
            if (index == 0) UnitSystem.METRIC else UnitSystem.IMPERIAL
        )
    }

    fun selectWaste(index: Int) {
        settingsRepository.setDefaultWaste(JoistOptions.waste[index])
    }

    fun setShowOnCentreDeltas(enabled: Boolean) {
        settingsRepository.setShowOnCentreDeltas(enabled)
    }

    fun setHapticOnSnap(enabled: Boolean) {
        settingsRepository.setHapticOnSnap(enabled)
    }
}
