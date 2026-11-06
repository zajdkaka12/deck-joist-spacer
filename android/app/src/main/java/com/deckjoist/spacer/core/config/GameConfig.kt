package com.deckjoist.spacer.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val STOCK_LENGTH_MM = 3600
    const val BEAM_MAX_SPAN_MM = 2400
    const val POST_MAX_SPACING_MM = 1800
    const val JOIST_WIDTH_MM = 45
    const val BOARD_THICKNESS_MM = 22
    const val POST_SECTION_MM = 90
    const val POST_EMBEDMENT_MM = 300
    const val SCREW_BOX_SIZE = 500
    const val DECK_SCREW_MM = 65
    const val STRUCTURAL_SCREW_MM = 100
    const val GRID_PITCH_MM = 500

    const val DEFAULT_LENGTH_MM = 4800
    const val DEFAULT_WIDTH_MM = 3600
    const val DEFAULT_HEIGHT_MM = 600
    const val DEFAULT_SPACING_MM = 400
    const val DEFAULT_JOIST_DEPTH_MM = 190
    const val DEFAULT_BOARD_WIDTH_MM = 140
    const val DEFAULT_GAP_MM = 5
    const val DEFAULT_WASTE_PERCENT = 10

    const val MIN_LENGTH_MM = 1200
    const val MAX_LENGTH_MM = 12000
    const val MIN_WIDTH_MM = 1200
    const val MAX_WIDTH_MM = 9000
    const val MIN_HEIGHT_MM = 0
    const val MAX_HEIGHT_MM = 3000
    const val PLAN_STEP_MM = 100
    const val HEIGHT_STEP_MM = 50
    const val MAX_DECK_NAME_CHARS = 24

    const val MIN_ZOOM = 0.6f
    const val MAX_ZOOM = 3.2f
    const val FAN_OUT_PIECE_MS = 420
    const val FAN_OUT_STAGGER_MS = 240
    const val FAN_OUT_MAX_MS = 3000
    const val HIGHLIGHT_STEP_MS = 900L
}
