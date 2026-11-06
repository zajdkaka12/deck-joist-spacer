package com.deckjoist.spacer.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import com.deckjoist.spacer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class BuildNotesViewModel(
    private val draftPlanRepository: DraftPlanRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val uiState = MutableStateFlow(BuildNotesUiState())
    val state: StateFlow<BuildNotesUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(draftPlanRepository.draft, settingsRepository.settings) { plan, settings ->
                BuildNotesUiState(
                    deckName = plan.name,
                    notes = plan.notes,
                    checked = settings.checkedNotes
                )
            }.collect { next ->
                uiState.value = next
            }
        }
    }

    fun setNotes(value: String) {
        draftPlanRepository.update { plan -> plan.copy(notes = value) }
    }

    fun toggleChecklistItem(index: Int) {
        settingsRepository.toggleCheckedNote(index)
    }
}
