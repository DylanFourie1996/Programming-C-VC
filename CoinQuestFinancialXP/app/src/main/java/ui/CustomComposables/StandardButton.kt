package ui.CustomComposables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest

enum class StandardButtonTheme {
    BLUE,
    BLACK,
    ORANGEGRAND,
    DARKGRAND
}
@Composable
fun StandardButton(
    onClick: () -> Unit,
    themeType : StandardButtonTheme=StandardButtonTheme.BLUE,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
) {
    val customColors = LocalCustomColors.current

    // Define button colours
    var colorButtonTop = customColors.ActionButtonTop
    var colorButtonBot = customColors.ActionButtonBot
    var colorButtonText = customColors.ActionButtonText
    var colorButtonBackground = customColors.ActionButton3Top
    var useBackground = false

    // Chahnge button colours based on theme specified. Default =  BLUE
    when (themeType) {
        StandardButtonTheme.BLACK -> {
            colorButtonTop = customColors.ActionButton2Top
            colorButtonBot = customColors.ActionButton2Bot
            colorButtonText = customColors.ActionButton2Text
        }
        StandardButtonTheme.ORANGEGRAND -> {
            colorButtonTop = Color.Transparent
            colorButtonBot = customColors.ActionButton3Bot
            colorButtonText = customColors.ActionButton3Text
            colorButtonBackground = customColors.ActionButton3Top
            useBackground = true
        }
        StandardButtonTheme.DARKGRAND -> {
            colorButtonTop = Color.Transparent
            colorButtonBot = customColors.ActionButton4Bot
            colorButtonText = customColors.ActionButton4Text
            colorButtonBackground = customColors.ActionButton4Top
            useBackground = true
        }
        else -> {
            colorButtonTop = customColors.ActionButtonTop
            colorButtonBot = customColors.ActionButtonBot
            colorButtonText = customColors.ActionButtonText
        }
    }

    val cornerRadius = 30
    val height = 46.dp
    val restOffset = 0.dp
    val pressedOffset = 4.dp
    val shadowOffset = 4.dp

    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest {interaction: Interaction ->
            isPressed = interaction is PressInteraction.Press
        }
    }

    val animatedOffset by animateDpAsState(
        targetValue = if (isPressed) pressedOffset else restOffset,
        label = "PressOffset"
    )
    Box(
        modifier=modifier.fillMaxWidth().height(height),
        contentAlignment=Alignment.TopCenter
    ) {
        Box(modifier=Modifier.fillMaxWidth()
            .offset(y=shadowOffset)
            .clip(RoundedCornerShape(cornerRadius))
            .background(colorButtonBot)
            .fillMaxHeight()
        )

        Button(
            onClick=onClick,
            modifier= Modifier.fillMaxWidth().fillMaxHeight().offset(y=animatedOffset).then(
                if (useBackground) {
                    Modifier.background(colorButtonBackground, RoundedCornerShape(cornerRadius))
                } else {
                    Modifier
                }
            ),
            shape= RoundedCornerShape(cornerRadius),
            enabled=enabled,
            interactionSource=interactionSource,
            colors= ButtonDefaults.buttonColors(
                containerColor=colorButtonTop,
                contentColor=colorButtonText,
                disabledContainerColor=Color(0xFFB0B0B0),
                disabledContentColor=Color.White
            )
        ) {
            Text(
                text=text,
                fontSize=18.77.sp,
                fontWeight= FontWeight.Bold
            )
        }
    }

}