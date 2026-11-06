package com.deckjoist.spacer.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import com.deckjoist.spacer.domain.repository.SettingsRepository
import com.deckjoist.spacer.domain.usecase.BuildMaterialListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(
    private val draftPlanRepository: DraftPlanRepository,
    private val settingsRepository: SettingsRepository,
    private val buildMaterialList: BuildMaterialListUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = uiState.asStateFlow()

    private var signature = ""
    private var highlightJob: Job? = null

    init {
        viewModelScope.launch {
            combine(draftPlanRepository.draft, settingsRepository.settings) { plan, settings ->
                plan to settings
            }.collect { pair ->
                val plan = pair.first
                val report = buildMaterialList(plan)
                val nextSignature = plan.spacingMm.toString() + plan.direction.name +
                    plan.lengthMm.toString() + plan.widthMm.toString()
                val changed = nextSignature != signature
                signature = nextSignature
                uiState.update { current ->
                    current.copy(
                        report = report,
                        unitSystem = pair.second.unitSystem,
                        hapticEnabled = pair.second.hapticOnSnap,
                        revision = if (changed) current.revision + 1 else current.revision,
                        highlight = if (changed) 0 else current.highlight,
                        selectedJoist = if (changed) null else current.selectedJoist
                    )
                }
            }
        }
    }

    fun resume() {
        if (highlightJob?.isActive == true) return
        highlightJob = viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.HIGHLIGHT_STEP_MS)
                uiState.update { current ->
                    val total = current.report?.joists?.joistCount ?: 0
                    if (total <= 0) current else current.copy(highlight = (current.highlight + 1) % total)
                }
            }
        }
    }

    fun pause() {
        highlightJob?.cancel()
        highlightJob = null
    }

    fun selectJoist(index: Int) {
        uiState.update { it.copy(selectedJoist = index, highlight = index) }
    }

    fun dismissJoist() {
        uiState.update { it.copy(selectedJoist = null) }
    }

    override fun onCleared() {
        pause()
        super.onCleared()
    }
}
