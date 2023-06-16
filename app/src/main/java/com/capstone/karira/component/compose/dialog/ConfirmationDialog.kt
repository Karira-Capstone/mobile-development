package com.capstone.karira.component.compose.dialog

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Icon
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.capstone.karira.R
import com.capstone.karira.component.compose.CustomTextField
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.utils.getFileNameFromUri
import com.capstone.karira.utils.uriToFile
import com.capstone.karira.viewmodel.proyek.ProyekDetailViewModel
import java.io.File

@Composable
fun ConfirmationDialog(
    mainText: String,
    buttonText: String,
    onClick: () -> Unit,
    setShowDialog: (Boolean) -> Unit,
    closeDialog: () -> Unit
) {

    Dialog(
        onDismissRequest = { setShowDialog(false) }, properties = DialogProperties(
            usePlatformDefaultWidth = false // experimental
        )
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = mainText,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.order_dialog_alerttext),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Button(
                            onClick = {
                                onClick()
                            },
                            shape = RoundedCornerShape(16),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = colorResource(R.color.purple_500)
                            ),
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(buttonText)
                        }
                        OutlinedButton(
                            onClick = { closeDialog() },
                            shape = RoundedCornerShape(16),
                            border = BorderStroke(1.dp, colorResource(R.color.purple_500)),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = colorResource(R.color.purple_500),
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(stringResource(id = R.string.proyek_detail_dialog_button_outlined))
                        }
                    }

                }
            }
        }
    }
}