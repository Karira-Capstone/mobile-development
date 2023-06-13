package com.capstone.karira.component.compose.dialog

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.capstone.karira.R
import com.capstone.karira.component.compose.CustomTextField
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.component.compose.SmallButton
import com.capstone.karira.component.compose.TitleWithValue
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.model.Bid
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.utils.downloadFile
import com.capstone.karira.utils.getFileNameFromUri
import com.capstone.karira.utils.uriToFile
import com.capstone.karira.viewmodel.proyek.ProyekTawaranViewModel
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConfirmBiddingDialog(
    userDataStore: UserDataStore,
    bid: Bid,
    proyekTawaranViewModel: ProyekTawaranViewModel,
    setShowDialog: (Boolean) -> Unit,
    closeDialog: () -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Log.d("TTTTTTTTTTTTT", userDataStore.token.toString())

    var messageField by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf<String?>("") }
    var file by remember { mutableStateOf<File?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            file = uriToFile(uri, context)
            fileName = getFileNameFromUri(uri, context)
        }
    }

    Dialog(
        onDismissRequest = { setShowDialog(false) }, properties = DialogProperties(
            usePlatformDefaultWidth = false // experimental
        )
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 36.dp)
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(id = R.string.proyek_detail_dialog_title),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    TitleWithValue(title = stringResource(id = R.string.proyek_tawaran_dialog_name), value = bid.worker?.user?.fullName.toString())
                    TitleWithValue(title = stringResource(id = R.string.proyek_tawaran_dialog_email), value = bid.worker?.user?.email.toString())
                    TitleWithValue(title = stringResource(id = R.string.proyek_detail_dialog_price), value = "Rp${createDotInNumber(bid.price.toString())}")
                    TitleWithValue(title = stringResource(id = R.string.proyek_detail_dialog_message), value = bid.message.toString())
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text(
                            text = stringResource(id = R.string.proyek_tawaran_dialog_skill),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        FlowRow(
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start,
                            content = {
                                val skills = bid.worker?.skills?.map { StaticDatas.skills[(it.id as Int) - 1] }
                                if (skills != null) {
                                    for (skill in skills) {
                                        if (skill != "") SmallButton(
                                            text = skill,
                                            isClosable = false,
                                            onClick = { })
                                    }
                                }
                            }
                        )
                    }
                    if (bid.attachment != null || bid.attachment != "null" || bid.attachment != "") {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(colorResource(id = R.color.gray_200)),
                        ) {
                            DashedButton(
                                text = bid.attachment?.split("/")?.last().toString(),
                                asInput = true,
                                onClick = {
                                    downloadFile(
                                        context,
                                        bid.attachment,
                                        bid.attachment?.split("/")?.last().toString()
                                    )
                                },
                            )
                        }
                    }
                    Divider(
                        color = colorResource(R.color.blackAlpha_300),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .height(1.dp)
                            .fillMaxWidth()
                    )
                    CustomTextField(
                        title = stringResource(id = R.string.layanan_detail_dialog_message),
                        text = messageField,
                        setText = { newText ->
                            messageField = newText
                        })
                    Column(modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.proyek_buat_file_title),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (file == null) {
                            DashedButton(
                                text = stringResource(id = R.string.proyek_buat_file_uploadboxtitle),
                                onClick = { launcher.launch("*/*") }
                            )
                        } else {
                            fileName?.let {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(colorResource(id = R.color.gray_200))
                                ) {
                                    DashedButton(
                                        text = it,
                                        asInput = true,
                                        onClick = { },
                                    )
                                    FilledTonalIconButton(
                                        onClick = {
                                            file = null
                                            fileName = null
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .align(Alignment.TopEnd)
                                            .size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.Close,
                                            contentDescription = "Close",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Button(
                            enabled = (messageField != ""),
                            onClick = {
                                proyekTawaranViewModel.createOrderFromProjectBid(bid, userDataStore.token, messageField, file)
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
                            Text(stringResource(id = R.string.proyek_tawaran_dialog_primary_button))
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