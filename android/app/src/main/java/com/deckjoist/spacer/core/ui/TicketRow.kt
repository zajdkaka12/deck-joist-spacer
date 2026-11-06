package com.deckjoist.spacer.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.MossBackground
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.TextPrimary

@Composable
fun TicketRow(
    label: String,
    quantity: String?,
    ticked: Boolean,
    stateLabel: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = CedarPrimary
) {
    val strike by animateFloatAsState(
        targetValue = if (ticked) 1f else 0f,
        animationSpec = tween(durationMillis = 150, easing = LinearEasing)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable { onToggle() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .semantics { stateDescription = stateLabel },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (ticked) tint else Color.Transparent)
                .border(1.dp, tint.copy(alpha = 0.7f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (ticked) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = MossBackground,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            modifier = Modifier
                .weight(1f)
                .alpha(if (ticked) 0.55f else 1f)
                .drawWithContent {
                    drawContent()
                    if (strike > 0f) {
                        drawLine(
                            color = TextPrimary,
                            start = Offset(0f, size.height / 2f),
                            end = Offset(size.width * strike, size.height / 2f),
                            strokeWidth = 2f
                        )
                    }
                }
        )
        if (quantity != null) {
            Text(
                text = quantity,
                style = MaterialTheme.typography.titleMedium,
                color = RustAccent,
                modifier = Modifier.alpha(if (ticked) 0.55f else 1f)
            )
        }
    }
}
