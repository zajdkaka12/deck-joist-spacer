package com.deckjoist.spacer.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.RustTint
import com.deckjoist.spacer.core.theme.TextSecondary

@Composable
fun CounterChip(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    accent: Color = RustAccent,
    container: Color = RustTint
) {
    Row(
        modifier = modifier
            .height(28.dp)
            .background(container, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                fadeIn(tween(180)) togetherWith fadeOut(tween(120))
            }
        ) { shown ->
            Text(
                text = shown,
                style = MaterialTheme.typography.titleMedium,
                color = accent,
                maxLines = 1
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            maxLines = 1
        )
    }
}
