package com.deckjoist.spacer.presentation.notes

data class BuildNotesUiState(
    val deckName: String = "",
    val notes: String = "",
    val checked: Set<Int> = emptySet()
)
