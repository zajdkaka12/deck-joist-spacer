package com.deckjoist.spacer.presentation.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.model.UnitSystem
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import com.deckjoist.spacer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

private const val IMPERIAL_PLAN_STEP_MM = 152
private const val IMPERIAL_HEIGHT_STEP_MM = 76

class SizeEntryViewModel(
    private val draftPlanRepository: DraftPlanRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val uiState = MutableStateFlow(SizeEntryUiState())
    val state: StateFlow<SizeEntryUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(draftPlanRepository.draft, settingsRepository.settings) { plan, settings ->
                SizeEntryUiState(
                    name = plan.name,
                    lengthMm = plan.lengthMm,
                    widthMm = plan.widthMm,
                    heightMm = plan.heightMm,
                    unitSystem = settings.unitSystem
                )
            }.collect { next ->
                uiState.value = next
            }
        }
    }

    fun stepLength(up: Boolean) {
        val step = planStep()
        draftPlanRepository.update { plan ->
            plan.copy(
                lengthMm = shift(plan.lengthMm, step, up)
                    .coerceIn(GameConfig.MIN_LENGTH_MM, GameConfig.MAX_LENGTH_MM)
            )
        }
    }

    fun stepWidth(up: Boolean) {
        val step = planStep()
        draftPlanRepository.update { plan ->
            plan.copy(
                widthMm = shift(plan.widthMm, step, up)
                    .coerceIn(GameConfig.MIN_WIDTH_MM, GameConfig.MAX_WIDTH_MM)
            )
        }
    }

    fun stepHeight(up: Boolean) {
        val step = if (isMetric()) GameConfig.HEIGHT_STEP_MM else IMPERIAL_HEIGHT_STEP_MM
        draftPlanRepository.update { plan ->
            plan.copy(
                heightMm = shift(plan.heightMm, step, up)
                    .coerceIn(GameConfig.MIN_HEIGHT_MM, GameConfig.MAX_HEIGHT_MM)
            )
        }
    }

    fun setName(value: String) {
        val trimmed = value.take(GameConfig.MAX_DECK_NAME_CHARS)
        draftPlanRepository.update { plan -> plan.copy(name = trimmed) }
    }

    private fun isMetric(): Boolean =
        settingsRepository.settings.value.unitSystem == UnitSystem.METRIC

    private fun planStep(): Int =
        if (isMetric()) GameConfig.PLAN_STEP_MM else IMPERIAL_PLAN_STEP_MM

    private fun shift(value: Int, step: Int, up: Boolean): Int =
        if (up) value + step else value - step
}
