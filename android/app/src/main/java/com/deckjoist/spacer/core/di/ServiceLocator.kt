package com.deckjoist.spacer.core.di

import android.content.Context
import com.deckjoist.spacer.data.local.DeckPreferences
import com.deckjoist.spacer.data.local.SettingsPreferences
import com.deckjoist.spacer.data.repository.DeckRepositoryImpl
import com.deckjoist.spacer.data.repository.DraftPlanRepositoryImpl
import com.deckjoist.spacer.data.repository.SettingsRepositoryImpl
import com.deckjoist.spacer.domain.repository.DeckRepository
import com.deckjoist.spacer.domain.repository.DraftPlanRepository
import com.deckjoist.spacer.domain.repository.SettingsRepository
import com.deckjoist.spacer.domain.usecase.BuildMaterialListUseCase
import com.deckjoist.spacer.domain.usecase.CalculateBoardPlanUseCase
import com.deckjoist.spacer.domain.usecase.CalculateFastenersUseCase
import com.deckjoist.spacer.domain.usecase.CalculateJoistLayoutUseCase

object ServiceLocator {

    private var deckStore: DeckRepository? = null
    private var settingsStore: SettingsRepository? = null
    private var draftStore: DraftPlanRepository? = null
    private var reportBuilder: BuildMaterialListUseCase? = null

    fun init(context: Context) {
        if (deckStore != null) return
        val app = context.applicationContext
        deckStore = DeckRepositoryImpl(DeckPreferences(app))
        settingsStore = SettingsRepositoryImpl(SettingsPreferences(app))
        draftStore = DraftPlanRepositoryImpl()
        reportBuilder = BuildMaterialListUseCase(
            CalculateJoistLayoutUseCase(),
            CalculateBoardPlanUseCase(),
            CalculateFastenersUseCase()
        )
    }

    val deckRepository: DeckRepository
        get() = requireNotNull(deckStore)

    val settingsRepository: SettingsRepository
        get() = requireNotNull(settingsStore)

    val draftPlanRepository: DraftPlanRepository
        get() = requireNotNull(draftStore)

    val buildMaterialList: BuildMaterialListUseCase
        get() = requireNotNull(reportBuilder)
}
