package com.deckjoist.spacer.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.TextPrimary
import com.deckjoist.spacer.core.theme.TextSecondary

@Composable
fun JoistMarkDialog(
    joistNumber: Int,
    markText: String,
    sectionText: String,
    lengthText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(14.dp),
        containerColor = PaperSurface,
        title = {
            Text(
                text = stringResource(R.string.joist_dialog_title, joistNumber),
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.joist_dialog_mark, markText),
                    style = MaterialTheme.typography.titleMedium,
                    color = RustAccent
                )
                Text(
                    text = stringResource(R.string.joist_dialog_section, sectionText),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                Text(
                    text = stringResource(R.string.joist_dialog_length, lengthText),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.labelMedium,
                    color = CedarPrimary
                )
            }
        }
    )
}
