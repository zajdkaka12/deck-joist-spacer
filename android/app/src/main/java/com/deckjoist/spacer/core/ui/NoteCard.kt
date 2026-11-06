package com.deckjoist.spacer.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.TextSecondary

@Composable
fun NoteCard(
    text: String,
    spriteRes: Int,
    spriteDescription: String,
    modifier: Modifier = Modifier,
    spriteSize: Int = 36
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(PaperSurface)
            .height(IntrinsicSize.Min)
            .padding(end = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(RustAccent)
        )
        Image(
            painter = painterResource(spriteRes),
            contentDescription = spriteDescription,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .size(spriteSize.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}
