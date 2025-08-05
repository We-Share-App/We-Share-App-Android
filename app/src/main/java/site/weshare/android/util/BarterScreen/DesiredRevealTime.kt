package site.weshare.android.util.BarterScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesiredRevealTime(
    modifier: Modifier = Modifier,
    initialRevealTime: Int,
    onRevealTimeChanged: (Int) -> Unit
) {
    var revealTimeText by remember(initialRevealTime) { mutableStateOf(initialRevealTime.toString()) }

    LaunchedEffect(initialRevealTime) {
        revealTimeText = initialRevealTime.toString()
    }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val newTime = (revealTimeText.toIntOrNull() ?: 0) - 1
                    onRevealTimeChanged(if (newTime > 0) newTime else 0)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "-", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
            }

            OutlinedTextField(
                value = revealTimeText,
                onValueChange = { newValue ->
                    revealTimeText = newValue.filter { it.isDigit() }
                    onRevealTimeChanged(revealTimeText.toIntOrNull() ?: 0)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(56.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray,
                )
            )

            Button(
                onClick = {
                    val newTime = (revealTimeText.toIntOrNull() ?: 0) + 1
                    onRevealTimeChanged(newTime)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "+", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
            }
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val timeIncrements = listOf(1, 6, 12, 24)
            timeIncrements.forEach { increment ->
                Button(
                    onClick = {
                        val newTime = (revealTimeText.toIntOrNull() ?: 0) + increment
                        onRevealTimeChanged(newTime)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2FB475),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF2FB475).copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = "+${increment}시간")
                }
            }
        }
    }
}