package com.capstone.karira.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.karira.activity.auth.AuthActivity

@Composable
fun SmallButton(
    text: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick(text) },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .heightIn(min = 32.dp)
            .defaultMinSize(minHeight = 32.dp)
            .padding(start = 0.dp, end = 10.dp, top = 0.dp, bottom = 0.dp)
    ) {
        Text(text = text, color = Color.Black)
        Icon(Icons.Filled.Close,
            "Close",
            modifier = Modifier
                .height(18.dp)
                .padding(start = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun preview(){
    SmallButton(text = "Hello World", onClick = { AuthActivity().authViewModel.removeUserSkill("Hello World") })
}
