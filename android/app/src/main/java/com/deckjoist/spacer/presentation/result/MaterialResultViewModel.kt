package com.deckjoist.spacer.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deckjoist.spacer.domain.model.MaterialKind
import com.deckjoist.spacer.domain.repository.DeckRepository
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import com.deckjoist.spacer.domain.repository.SettingsRepository
import com.deckjoist.spacer.domain.usecase.BuildMaterialListUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MaterialResultViewModel(
    private val draftPlanRepository: DraftPlanRepository,
    private val settingsRepository: SettingsRepository,
    private val deckRepository: DeckRepository,
    private val buildMaterialList: BuildMaterialListUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(MaterialResultUiState())
    val state: StateFlow<MaterialResultUiState> = uiState.asStateFlow()

    private val eventChannel = Channel<MaterialResultEvent>(Channel.BUFFERED)
    val events: Flow<MaterialResultEvent> = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(draftPlanRepository.draft, settingsRepository.settings) { plan, settings ->
                buildMaterialList(plan) to settings.unitSystem
            }.collect { pair ->
                uiState.update { current ->
                    current.copy(report = pair.first, unitSystem = pair.second)
                }
            }
        }
    }

    fun toggleTicked(kind: MaterialKind) {
        uiState.update { current ->
            val next = current.ticked.toMutableSet()
            if (!next.add(kind)) next.remove(kind)
            current.copy(ticked = next)
        }
    }

    fun storeAndPlanAgain() {
        viewModelScope.launch {
            deckRepository.save(draftPlanRepository.draft.value)
            draftPlanRepository.startNew(settingsRepository.settings.value.defaultWastePercent)
            uiState.update { current -> current.copy(ticked = emptySet()) }
            eventChannel.send(MaterialResultEvent.PlanStored)
        }
    }
}
