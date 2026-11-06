package com.deckjoist.spacer.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.deckjoist.spacer.R
import com.deckjoist.spacer.domain.model.UnitSystem
import com.deckjoist.spacer.domain.model.millimetresToFeetInches
import com.deckjoist.spacer.domain.model.millimetresToInches
import com.deckjoist.spacer.domain.model.squareFeet
import com.deckjoist.spacer.domain.model.squareMetres

@Composable
fun lengthText(millimetres: Int, system: UnitSystem): String =
    if (system == UnitSystem.METRIC) {
        stringResource(R.string.len_mm, millimetres)
    } else {
        val parts = millimetresToFeetInches(millimetres)
        stringResource(R.string.len_ft_in, parts.feet, parts.inches)
    }

@Composable
fun shortLengthText(millimetres: Int, system: UnitSystem): String =
    if (system == UnitSystem.METRIC) {
        stringResource(R.string.len_mm, millimetres)
    } else {
        stringResource(R.string.len_in, millimetresToInches(millimetres))
    }

@Composable
fun areaText(lengthMm: Int, widthMm: Int, system: UnitSystem): String =
    if (system == UnitSystem.METRIC) {
        stringResource(R.string.area_sq_m, squareMetres(lengthMm, widthMm))
    } else {
        stringResource(R.string.area_sq_ft, squareFeet(lengthMm, widthMm))
    }

@Composable
fun deckSizeText(lengthMm: Int, widthMm: Int, system: UnitSystem): String =
    if (system == UnitSystem.METRIC) {
        stringResource(R.string.deck_size_metric, lengthMm / 1000f, widthMm / 1000f)
    } else {
        val length = millimetresToFeetInches(lengthMm)
        val width = millimetresToFeetInches(widthMm)
        stringResource(
            R.string.deck_size_imperial,
            length.feet,
            length.inches,
            width.feet,
            width.inches
        )
    }
