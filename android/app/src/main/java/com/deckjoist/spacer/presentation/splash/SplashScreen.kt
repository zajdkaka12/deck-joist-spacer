package com.deckjoist.spacer.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.theme.MossBackground
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.SplashMist
import com.deckjoist.spacer.core.theme.SplashOverlay
import com.deckjoist.spacer.core.ui.ScreenBackground

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    var revealed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        revealed = true
    }

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    val transition = rememberInfiniteTransition()
    val sweep by transition.animateFloatSafe(0f, 1f, 2400, 0)
    val sweepSecond by transition.animateFloatSafe(0f, 1f, 2400, 260)
    val sweepThird by transition.animateFloatSafe(0f, 1f, 2400, 520)
    val gridAlpha by transition.animateFloatReverse(0.10f, 0.22f, 3200)

    ScreenBackground(overlay = SplashOverlay) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val pitch = 48.dp.toPx()
            var x = 0f
            while (x < size.width) {
                drawLine(
                    color = MossBackground.copy(alpha = gridAlpha),
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += pitch
            }
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = MossBackground.copy(alpha = gridAlpha),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += pitch
            }
            val baseY = size.height * 0.72f
            listOf(sweep, sweepSecond, sweepThird).forEachIndexed { index, value ->
                val lineY = baseY + index * 22.dp.toPx()
                val lineWidth = size.width * 0.42f
                val start = -lineWidth + value * (size.width + lineWidth)
                drawLine(
                    color = RustAccent.copy(alpha = 0.38f),
                    start = Offset(start, lineY),
                    end = Offset(start + lineWidth, lineY),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = revealed,
                enter = fadeIn(tween(420)) + scaleIn(
                    initialScale = 0.82f,
                    animationSpec = spring(
                        dampingRatio = 0.62f,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    modifier = Modifier.size(148.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 34.sp,
                color = MossBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = SplashMist.copy(alpha = 0.78f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(34.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .width(184.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = RustAccent,
                trackColor = SplashMist.copy(alpha = 0.26f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = SplashMist.copy(alpha = 0.6f)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 26.dp)
        ) {
            Text(
                text = stringResource(R.string.splash_footer),
                style = MaterialTheme.typography.labelSmall,
                color = SplashMist.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
private fun InfiniteTransition.animateFloatSafe(
    start: Float,
    end: Float,
    durationMillis: Int,
    delayMillis: Int
) = animateFloat(
    initialValue = start,
    targetValue = end,
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = LinearEasing
        ),
        repeatMode = RepeatMode.Restart
    )
)

@Composable
private fun InfiniteTransition.animateFloatReverse(
    start: Float,
    end: Float,
    durationMillis: Int
) = animateFloat(
    initialValue = start,
    targetValue = end,
    animationSpec = infiniteRepeatable(
        animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
    )
)
