package com.capstone.karira.component.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.karira.R
import com.capstone.karira.activity.auth.AuthActivity

@Composable
fun SmallButton(
    text: String,
    isClosable: Boolean = true,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick(text) },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isClosable) Color.White else colorResource(
                id = R.color.gray_800
            )
        ),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .heightIn(min = 32.dp)
            .defaultMinSize(minHeight = 32.dp)
            .padding(start = 0.dp, end = 10.dp, top = 0.dp, bottom = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isClosable) colorResource(id = R.color.gray_800) else Color.White
        )
        if (isClosable) {
            Icon(
                Icons.Filled.Close,
                "Close",
                modifier = Modifier
                    .height(18.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun preview() {
    SmallButton(
        text = "Hello World",
        isClosable = false,
        onClick = { AuthActivity().authViewModel.removeUserSkill("Hello World") })
}
