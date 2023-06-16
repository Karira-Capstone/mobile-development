package com.capstone.karira.activity.layanan

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.component.compose.CustomTextField
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.component.compose.ImageCarouselUri
import com.capstone.karira.component.compose.SmallButton
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.data.remote.model.response.RecommendationResponse
import com.capstone.karira.databinding.FragmentLayananBuatBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.DummyDatas
import com.capstone.karira.model.ImageUrl
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.utils.reduceFileImage
import com.capstone.karira.utils.removeDotInNumber
import com.capstone.karira.utils.uriToFile
import com.capstone.karira.utils.uriToImg
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananBuatViewModel
import com.dicoding.jetreward.ui.common.UiState
import java.io.File
import java.util.Collections.addAll

class LayananBuatFragment : Fragment() {

    private lateinit var id: String
    private var _binding: FragmentLayananBuatBinding? = null
    private val binding get() = _binding!!
    val layananBuatViewModel: LayananBuatViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(EXTRA_ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLayananBuatBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBinding(view)

    }

    private fun handleBinding(view: View) {
        binding.formSection.setContent {
            KariraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LayananBuatApp(id, layananBuatViewModel, view)
                }
            }
        }
    }


    companion object {
        const val EXTRA_ID = "kinlian"
    }
}

@Composable
private fun LayananBuatApp(
    id: String,
    layananBuatViewModel: LayananBuatViewModel,
    view: View,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current as Activity
    val userDataStore =
        layananBuatViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    layananBuatViewModel.isCreated.collectAsState(initial = UiState.Initiate).value.let { isCreated ->
        when (isCreated) {
            is UiState.Success -> {
                val service = isCreated.data as Service

                var message = "";
                if (id == "null") message = "Layanan ${service.title.toString()} berhasil dibuat, menunggu review oleh admin"
                else message = "Layanan ${service.title.toString()} berhasil diedit"

                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()

                val bundle = Bundle()
                bundle.putString(LayananBuatFragment.EXTRA_ID, service.id.toString())
                view.findNavController().navigate(R.id.action_layananBuatFragment_to_layananDetailFragment, bundle)
            }
            else -> {}
        }
    }

    layananBuatViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    layananBuatViewModel.getServiceById(id)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val service = uiState.data as Service

                    var imageList = mutableListOf<ImageUrl>()

                    if (service.images?.foto1.toString() != "null") {
                        imageList.add(ImageUrl(Uri.parse(service.images?.foto1)))
                    }
                    if (service.images?.foto2.toString() != "null") {
                        imageList.add(ImageUrl(Uri.parse(service.images?.foto2)))
                    }
                    if (service.images?.foto3.toString() != "null") {
                        imageList.add(ImageUrl(Uri.parse(service.images?.foto3)))
                    }

                    var titleField by remember { mutableStateOf(service.title) }
                    var descriptionField by remember { mutableStateOf(service.description) }
                    var imagesUri = remember {
                        mutableStateListOf<ImageUrl>().apply {
                            addAll(imageList)
                        }
                    }
                    var filesUri = remember { mutableStateListOf<File?>().apply { addAll(listOf(null, null, null)) } }

                    val launcher = rememberLauncherForActivityResult(
                        contract =
                        ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        if (uri != null) {
                            imagesUri.add(element = ImageUrl(uri))
                            val file = reduceFileImage(uriToImg(uri, context))
                            filesUri[filesUri.lastIndexOf(null)] = file
                        }
                    }

                    Column(
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                            .fillMaxWidth()
                    ) {
                        TitleSection(
                            title = stringResource(id = R.string.layanan_title, "Edit ", ""),
                            subtitle = stringResource(
                                id = R.string.layanan_titlesub_buat
                            )
                        )
                        titleField?.let {
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_title_formtitle),
                                text = it,
                                setText = { newText -> titleField = newText })
                        }
                        descriptionField?.let {
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_description_formtitle),
                                text = it, setText = { newText ->
                                    descriptionField = newText
                                })
                        }
                        if (imagesUri.size != 3) {
                            DashedButton(
                                text = stringResource(id = R.string.layanan_buat_images_uploadboxtitle),
                                onClick = { launcher.launch("image/*") }
                            )
                        }
                        if (imagesUri.isNotEmpty()) {
                            ImageCarouselUri(
                                images = imagesUri,
                                handleImage = { index ->
                                    run {
                                        imagesUri.removeAt(index)
                                        filesUri[index] = null
                                    }
                                })
                        }
                        // ------------------------------------------- RECCOMENDATION -----------------------------------
                        layananBuatViewModel.isRecommended.collectAsState(initial = UiState.Initiate).value.let { recommendationState ->
                            when (recommendationState) {
                                is UiState.Success -> {
                                    val recommendationData = recommendationState.data as RecommendationResponse

                                    var priceField by remember { mutableStateOf("") }

                                    Column(modifier = Modifier.padding(top = 24.dp)) {
                                        Text(
                                            text = stringResource(id = R.string.layanan_buat_recommendation_formtitle),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                        )
                                        Text(
                                            text = if (recommendationData.result == "Others") "Tidak ada rekomendasi" else recommendationData.result,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                    priceField?.let {
                                        CustomTextField(
                                            stringResource(id = R.string.layanan_buat_price_formtitle),
                                            text = it,
                                            type = "Number",
                                            setText = { newText -> priceField = newText })
                                    }
                                    OutlinedButton(
                                        enabled = titleField != "" && descriptionField != "",
                                        onClick = {
                                            // Todo recommendation service
                                            layananBuatViewModel.findReccomendation(
                                                titleField.toString(),
                                                descriptionField.toString()
                                            )
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = colorResource(R.color.purple_500),
                                            containerColor = Color.White
                                        ),
                                        border = BorderStroke(
                                            1.dp,
                                            colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(top = 24.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            stringResource(id = R.string.layanan_buat_button_reccomend)
                                        )
                                    }
                                    Button(
                                        enabled = titleField != "" && descriptionField != "" && priceField != "",
                                        onClick = {
                                            try {
                                                 layananBuatViewModel.updateService(
                                                    service.id.toString(),
                                                    userDataStore.value.token,
                                                    titleField.toString(),
                                                    descriptionField.toString(),
                                                    removeDotInNumber(priceField).toInt(),
                                                    service.categoryId!!.toInt(),
                                                    imagesUri,
                                                    filesUri
                                                )
                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    context,
                                                    e.message.toString(),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.White,
                                            containerColor = colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            stringResource(
                                                id = R.string.layanan_buat_button_alter
                                            )
                                        )
                                    }
                                }

                                is UiState.Initiate -> {
                                    Button(
                                        enabled = titleField != "" && descriptionField != "",
                                        onClick = {
                                            // Todo recommendation service
                                            layananBuatViewModel.findReccomendation(
                                                titleField.toString(),
                                                descriptionField.toString()
                                            )
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.White,
                                            containerColor = colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(top = 24.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            stringResource(id = R.string.layanan_buat_button_reccomend)
                                        )
                                    }
                                }

                                is UiState.Error -> {
                                    Toast.makeText(context, recommendationState.errorMessage, Toast.LENGTH_SHORT).show()
                                    Button(
                                        enabled = titleField != "" && descriptionField != "",
                                        onClick = {
                                            // Todo recommendation service
                                            layananBuatViewModel.findReccomendation(
                                                titleField.toString(),
                                                descriptionField.toString()
                                            )
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.White,
                                            containerColor = colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(top = 24.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            stringResource(id = R.string.layanan_buat_button_reccomend)
                                        )
                                    }
                                }
                                is UiState.Loading -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxHeight().fillMaxWidth()
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                        // ----------------------------------------------------------------------------------------------

                    }
                }

                is UiState.Error -> {}
                is UiState.Initiate -> {
                    var titleField by remember { mutableStateOf("") }
                    var descriptionField by remember { mutableStateOf("") }
                    var priceField by remember { mutableStateOf("") }
                    var imagesUri = remember { mutableStateListOf<ImageUrl>() }
                    var filesUri = remember { mutableStateListOf<File>() }

                    val launcher = rememberLauncherForActivityResult(
                        contract =
                        ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        if (uri != null) {
                            imagesUri.add(element = ImageUrl(uri))
                            val file = reduceFileImage(uriToImg(uri, context))
                            filesUri.add(file)
                        }
                    }

                    Column(
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                            .fillMaxWidth()
                    ) {
                        TitleSection(
                            title = stringResource(
                                id = R.string.layanan_title,
                                "Buat ",
                                ""
                            ),
                            subtitle = stringResource(
                                id = R.string.layanan_titlesub_buat
                            )
                        )
                        titleField?.let {
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_title_formtitle),
                                text = it,
                                setText = { newText -> titleField = newText })
                        }
                        descriptionField?.let {
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_description_formtitle),
                                text = it, setText = { newText ->
                                    descriptionField = newText
                                })
                        }
                        if (imagesUri.size != 3) {
                            DashedButton(
                                text = stringResource(id = R.string.layanan_buat_images_uploadboxtitle),
                                onClick = { launcher.launch("image/*") }
                            )
                        }
                        if (imagesUri.isNotEmpty()) {
                            ImageCarouselUri(
                                images = imagesUri,
                                handleImage = { index ->
                                    run {
                                        imagesUri.removeAt(index)
                                        filesUri.removeAt(index)
                                    }
                                })
                        }

                        // ------------------------------------------- RECCOMENDATION -----------------------------------
                        layananBuatViewModel.isRecommended.collectAsState(initial = UiState.Initiate).value.let { recommendationState ->
                            when (recommendationState) {
                                is UiState.Success -> {
                                    val recommendationData = recommendationState.data as RecommendationResponse

                                    Column(modifier = Modifier.padding(top = 24.dp)) {
                                        Text(
                                            text = stringResource(id = R.string.layanan_buat_recommendation_formtitle),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                        )
                                        Text(
                                            text = if (recommendationData.result == "Others") "Tidak ada rekomendasi" else recommendationData.result,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                    priceField?.let {
                                        CustomTextField(
                                            stringResource(id = R.string.layanan_buat_price_formtitle),
                                            text = it,
                                            type = "Number",
                                            setText = { newText -> priceField = newText })
                                    }
                                    OutlinedButton(
                                        enabled = titleField != "" && descriptionField != "",
                                        onClick = {
                                            layananBuatViewModel.findReccomendation(
                                                titleField,
                                                descriptionField
                                            )
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = colorResource(R.color.purple_500),
                                            containerColor = Color.White
                                        ),
                                        border = BorderStroke(
                                            1.dp,
                                            colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(top = 24.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            stringResource(id = R.string.layanan_buat_button_reccomend)
                                        )
                                    }
                                    Button(
                                        enabled = titleField != "" && descriptionField != "" && priceField.isNotEmpty(),
                                        onClick = {
                                            try {
                                                layananBuatViewModel.createService(
                                                    userDataStore.value.token,
                                                    titleField,
                                                    descriptionField,
                                                    priceField.toInt(),
                                                    filesUri
                                                )
                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    context,
                                                    e.message.toString(),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.White,
                                            containerColor = colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            stringResource(
                                                id = R.string.layanan_buat_button
                                            )
                                        )
                                    }
                                }

                                is UiState.Initiate -> {
                                    Button(
                                        enabled = titleField != "" && descriptionField != "",
                                        onClick = {
                                            // Todo recommendation service
                                            layananBuatViewModel.findReccomendation(
                                                titleField,
                                                descriptionField
                                            )
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.White,
                                            containerColor = colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(top = 24.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            stringResource(id = R.string.layanan_buat_button_reccomend)
                                        )
                                    }
                                }

                                is UiState.Error -> {
                                    Toast.makeText(context, recommendationState.errorMessage, Toast.LENGTH_SHORT).show()
                                    Button(
                                        enabled = (titleField != "" && descriptionField != "" && durationField != ""),
                                        onClick = {
                                            // Todo recommendation service
                                            proyekBuatViewModel.findReccomendation(
                                                titleField.toString(),
                                                descriptionField.toString()
                                            )
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.White,
                                            containerColor = colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(top = 24.dp)
                                            .fillMaxWidth()
                                    ) {
                                        proyekBuatViewModel.isCreated.collectAsState().value.let {
                                            when (it) {
                                                is UiState.Loading -> {
                                                    CircularProgressIndicator()
                                                }

                                                else -> {}
                                            }
                                        }
                                        Text(
                                            stringResource(id = R.string.layanan_buat_button_reccomend)
                                        )
                                    }
                                }
                                is UiState.Loading -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxHeight().fillMaxWidth()
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                        // ----------------------------------------------------------------------------------------------
                    }
                }
            }
        }

}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    val context = LocalContext.current
    KariraTheme {
        LayananBuatApp(
            "1",
            LayananBuatViewModel(Injection.provideLayananRepostory(context)),
            View(context)
        )
    }
}

