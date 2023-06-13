package com.capstone.karira.component.compose

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.karira.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    title: String,
    text: String,
    setText: (String) -> Unit,
    type: String = "Text",
    undertext: String? = null,
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier)
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                setText(newText)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                unfocusedBorderColor = colorResource(
                    id = R.color.gray_200
                ),
                placeholderColor = colorResource(id = R.color.blackAlpha_300)
            ),
            readOnly = if (type == "Dropdown") true else false,
            keyboardOptions = KeyboardOptions(keyboardType = if (type == "Number") KeyboardType.Number else KeyboardType.Text),
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = if (type == "LongText") 128.dp else 0.dp)
        )
        undertext?.let {
            Text(text = undertext, fontSize = 14.sp, modifier = Modifier.padding(top = 6.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    var text by remember { mutableStateOf("") }

    CustomTextField("Nama", text = text, setText = { newText -> text = newText }, undertext = "Tes 1 2 3")
}