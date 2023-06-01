package com.capstone.karira.activity.layanan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.karira.R
import com.capstone.karira.component.compose.CenterHeadingWithDesc
import com.capstone.karira.component.compose.CustomTextField
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.component.compose.ImageCarousel
import com.capstone.karira.component.compose.ImageCarouselUri
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.ActivityLayananBuatBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.ImageUrl
import com.capstone.karira.model.Service
import com.capstone.karira.model.User
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananBuatViewModel
import com.capstone.karira.viewmodel.layanan.LayananDetailViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananBuatActivity : AppCompatActivity() {

    private lateinit var id: String
    private lateinit var binding: ActivityLayananBuatBinding
    val layananBuatViewModel: LayananBuatViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananBuatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra(EXTRA_ID).toString();

        handleBinding()
    }

    private fun handleBinding() {
        binding.formSection.setContent {
            KariraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LayananBuatApp(id, layananBuatViewModel)
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "kinlian"
    }

}

@Composable
private fun LayananBuatApp(id: String? = null, layananBuatViewModel: LayananBuatViewModel, modifier: Modifier = Modifier) {
    var titleTextField by remember { mutableStateOf(TextFieldValue("")) }
    var durationTextField by remember { mutableStateOf(TextFieldValue("")) }
    var categoryTextField by remember { mutableStateOf(TextFieldValue("")) }
    var descriptionTextField by remember { mutableStateOf(TextFieldValue("")) }
    var imagesUri = remember { mutableStateListOf<ImageUrl>() }

    val context = LocalContext.current as Activity
    val user = layananBuatViewModel.user.collectAsState(initial = User("", "ssss"))

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagesUri.add(element = ImageUrl(uri))
        }
    }

    if (id != null) {
        layananBuatViewModel.uiState
            .collectAsState(initial = UiState.Loading).value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        layananBuatViewModel.getServiceById(id)
                    }
                    is UiState.Success -> {
                        val service = uiState.data

                        titleTextField = TextFieldValue(service.title)
                        durationTextField = TextFieldValue(service.max_duration.toString())
                        descriptionTextField = TextFieldValue(service.description)

                        Column(
                            modifier = modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                                .fillMaxWidth()
                        ) {
                            TitleSection(
                                title = stringResource(id = R.string.layanan_title, "Buat ", ""),
                                subtitle = stringResource(
                                    id = R.string.layanan_titlesub_buat
                                )
                            )
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_title_formtitle),
                                text = titleTextField,
                                setText = { newText -> titleTextField = newText })
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_duration_formtitle),
                                text = durationTextField,
                                setText = { newText -> durationTextField = newText },
                                type = "Number"
                            )
                //        CustomDropdownMenu(
                //            stringResource(id = R.string.layanan_buat_category_formtitle),
                //            text = categoryTextField,
                //            setText = { newText -> categoryTextField = newText })
                            CustomTextField(stringResource(id = R.string.layanan_buat_description_formtitle),
                                text = descriptionTextField, setText = { newText -> descriptionTextField = newText })
                            DashedButton(
                                text = stringResource(id = R.string.layanan_buat_images_uploadboxtitle),
                                onClick = { launcher.launch("image/*") }
                            )
                            if (imagesUri.isNotEmpty()) {
                                ImageCarouselUri(
                                    images = imagesUri,
                                    handleImage = { index -> imagesUri.removeAt(index) })
                            }
                            Button(
                                onClick = { context.finish() },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = colorResource(R.color.purple_500)
                                ),
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(stringResource(id = R.string.layanan_buat_button))
                            }
                        }
                    }

                    is UiState.Error -> {}
                }
            }
    }

}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    val context = LocalContext.current
    KariraTheme {
        LayananBuatApp("1", LayananBuatViewModel(Injection.provideAuthRepostory(context)))
    }
}

