package com.capstone.karira.component.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.karira.R

@Composable
fun TitleSection(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = subtitle, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
    }
}