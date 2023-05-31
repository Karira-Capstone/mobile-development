package com.capstone.karira.activity.layanan

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.capstone.karira.R
import com.capstone.karira.component.compose.CustomDropdownMenu
import com.capstone.karira.component.compose.CustomTextField
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.component.compose.ImageCarousel
import com.capstone.karira.component.compose.ImageCarouselUri
import com.capstone.karira.component.xml.fragment.TitleFragment
import com.capstone.karira.databinding.ActivityLayananBuatBinding
import com.capstone.karira.model.ImageUrl
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.uriToFile
import kotlin.reflect.KProperty

class LayananBuatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananBuatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLayananBuatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleFragmentContent()
        handleBinding()
    }

    fun handleFragmentContent() {
        val titleFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_top) as TitleFragment

        val titleBundle = Bundle()

        titleBundle.putString(TitleFragment.TYPE, "LAYANAN BUAT")

        titleFragment.arguments = titleBundle
    }

    private fun handleBinding() {
        binding.formSection.setContent {
            KariraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LayananBuatApp()
                }
            }
        }
    }
}

@Composable
private fun LayananBuatApp(modifier: Modifier = Modifier) {
    var titleTextField by remember { mutableStateOf(TextFieldValue("")) }
    var durationTextField by remember { mutableStateOf(TextFieldValue("")) }
    var categoryTextField by remember { mutableStateOf(TextFieldValue("")) }
    var descriptionTextField by remember { mutableStateOf(TextFieldValue("")) }
    var imagesUri = remember { mutableStateListOf<ImageUrl>() }

    val launcher = rememberLauncherForActivityResult(contract =
        ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imagesUri.add(element = ImageUrl(uri))
            }
    }

    Column(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 48.dp, top = 32.dp)
            .fillMaxWidth()
    ) {
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
            ImageCarouselUri(images = imagesUri, handleImage = { index -> imagesUri.removeAt(index) })
        }
        Button(
            onClick = { /*TODO*/ },
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

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    KariraTheme {
        LayananBuatApp()
    }
}