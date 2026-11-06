package com.deckjoist.spacer.presentation.boards

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

class BoardPlanViewModel(
    draftPlanRepository: DraftPlanRepository,
    settingsRepository: SettingsRepository,
    buildMaterialList: BuildMaterialListUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(BoardPlanUiState())
    val state: StateFlow<BoardPlanUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(draftPlanRepository.draft, settingsRepository.settings) { plan, settings ->
                BoardPlanUiState(
                    report = buildMaterialList(plan),
                    unitSystem = settings.unitSystem
                )
            }.collect { next ->
                uiState.value = next
            }
        }
    }
}
