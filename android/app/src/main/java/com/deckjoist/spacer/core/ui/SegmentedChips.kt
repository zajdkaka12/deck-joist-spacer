package com.deckjoist.spacer.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.MossBackground
import com.deckjoist.spacer.core.theme.SecondarySage
import com.deckjoist.spacer.core.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SegmentedChips(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .defaultMinSize(minWidth = 72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selected) CedarPrimary else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = if (selected) CedarPrimary else SecondarySage,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onSelect(index) }
                    .padding(horizontal = 14.dp)
                    .semantics { stateDescription = option },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) MossBackground else TextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}
