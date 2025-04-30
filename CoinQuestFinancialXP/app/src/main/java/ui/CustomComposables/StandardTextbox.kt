package ui.CustomComposables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StandardTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder : String = "",
    isError: Boolean = false,
    enabled: Boolean = true,
) {
    val customColors = LocalCustomColors.current

    val cornerRadius = 30
    val fontSize = 13.sp
    val size = 48.dp

    val borderColor = if (isError) MaterialTheme.colorScheme.error else customColors.TextBoxBorder



    TextField(
        value=value,
        onValueChange=onValueChange,
        enabled=enabled,
        singleLine=true,
        placeholder = {
            Text(
                text=placeholder,
                fontSize=fontSize,
                color=customColors.TextBoxText
            )
        },
        textStyle = TextStyle(
            fontSize=fontSize,
            color=customColors.TextBoxText
        ),
        modifier=modifier.fillMaxWidth().height(size).clip(RoundedCornerShape(cornerRadius)).border(1.5.dp, borderColor, RoundedCornerShape(cornerRadius)),
        colors = TextFieldDefaults.colors(
            focusedTextColor=customColors.TextBoxText,
            unfocusedTextColor=customColors.TextBoxText,
            disabledTextColor=customColors.TextBoxText,
            unfocusedContainerColor=customColors.TextBoxBG,
            disabledContainerColor=customColors.TextBoxBG,
            focusedContainerColor=customColors.TextBoxBG,
            errorTextColor=MaterialTheme.colorScheme.error,
            focusedIndicatorColor=borderColor,
            unfocusedIndicatorColor=borderColor,
            cursorColor=customColors.ActionButtonTop,
            errorIndicatorColor=MaterialTheme.colorScheme.error
        ),
        isError=isError
    )

}