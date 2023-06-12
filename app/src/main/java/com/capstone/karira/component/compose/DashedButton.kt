package com.capstone.karira.component.compose

import android.util.Log
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.karira.R

@Composable
fun DashedButton(
    text: String,
    onClick: () -> Unit,
    asInput: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Button(
            enabled = !asInput,
            onClick = { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            modifier = if (asInput) Modifier
                .fillMaxWidth()
                .height(96.dp) else Modifier
                .drawBehind {
                    drawRoundRect(
                        color = Color.Gray,
                        style = Stroke(
                            width = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    )
                }
                .fillMaxWidth()
                .height(96.dp)
        ) {
            Text(
                text = text,
                color = if (asInput) colorResource(id = R.color.blackAlpha_800) else colorResource(
                    id = R.color.blackAlpha_300
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    DashedButton("Hello World!", { Log.d("A", "Hello") })
}