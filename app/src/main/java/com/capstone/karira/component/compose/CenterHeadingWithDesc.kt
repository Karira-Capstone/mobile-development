package com.capstone.karira.component.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CenterHeadingWithDesc(main: String, subtext: String, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = main,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = subtext,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CenterHeadingWithDesc(main = "Rp2.000.000", subtext = "Tidak dinegosiasi")
}