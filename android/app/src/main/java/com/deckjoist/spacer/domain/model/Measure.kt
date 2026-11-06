package com.deckjoist.spacer.domain.model

import kotlin.math.roundToInt

data class FeetInches(val feet: Int, val inches: Int)

private const val MM_PER_INCH = 25.4

fun millimetresToFeetInches(millimetres: Int): FeetInches {
    val totalInches = (millimetres / MM_PER_INCH).roundToInt()
    return FeetInches(totalInches / 12, totalInches % 12)
}

fun millimetresToInches(millimetres: Int): Int = (millimetres / MM_PER_INCH).roundToInt()

fun millimetresToMetres(millimetres: Int): Float = millimetres / 1000f

fun squareMetres(lengthMm: Int, widthMm: Int): Float =
    (lengthMm.toLong() * widthMm.toLong()) / 1_000_000f

fun squareFeet(lengthMm: Int, widthMm: Int): Int =
    (squareMetres(lengthMm, widthMm) * 10.7639f).roundToInt()
