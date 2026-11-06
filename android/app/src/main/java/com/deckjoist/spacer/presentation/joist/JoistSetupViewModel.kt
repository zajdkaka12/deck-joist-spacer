package com.deckjoist.spacer.presentation.joist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import com.deckjoist.spacer.domain.repository.SettingsRepository
import com.deckjoist.spacer.domain.usecase.BuildMaterialListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.math.max

class JoistSetupViewModel(
    private val draftPlanRepository: DraftPlanRepository,
    private val settingsRepository: SettingsRepository,
    private val buildMaterialList: BuildMaterialListUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(JoistSetupUiState())
    val state: StateFlow<JoistSetupUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(draftPlanRepository.draft, settingsRepository.settings) { plan, settings ->
                JoistSetupUiState(
                    report = buildMaterialList(plan),
                    unitSystem = settings.unitSystem,
                    spacingIndex = indexOf(JoistOptions.spacing, plan.spacingMm),
                    directionIndex = max(0, JoistOptions.directions.indexOf(plan.direction)),
                    depthIndex = indexOf(JoistOptions.depth, plan.joistDepthMm),
                    boardWidthIndex = indexOf(JoistOptions.boardWidth, plan.boardWidthMm),
                    gapIndex = indexOf(JoistOptions.gap, plan.gapMm),
                    wasteIndex = indexOf(JoistOptions.waste, plan.wastePercent)
                )
            }.collect { next ->
                uiState.value = next
            }
        }
    }

    fun selectSpacing(index: Int) = draftPlanRepository.update { plan ->
        plan.copy(spacingMm = JoistOptions.spacing[index])
    }

    fun selectDirection(index: Int) = draftPlanRepository.update { plan ->
        plan.copy(direction = JoistOptions.directions[index])
    }

    fun selectDepth(index: Int) = draftPlanRepository.update { plan ->
        plan.copy(joistDepthMm = JoistOptions.depth[index])
    }

    fun selectBoardWidth(index: Int) = draftPlanRepository.update { plan ->
        plan.copy(boardWidthMm = JoistOptions.boardWidth[index])
    }

    fun selectGap(index: Int) = draftPlanRepository.update { plan ->
        plan.copy(gapMm = JoistOptions.gap[index])
    }

    fun selectWaste(index: Int) = draftPlanRepository.update { plan ->
        plan.copy(wastePercent = JoistOptions.waste[index])
    }

    private fun indexOf(options: List<Int>, value: Int): Int {
        val index = options.indexOf(value)
        return if (index >= 0) index else 0
    }
}
