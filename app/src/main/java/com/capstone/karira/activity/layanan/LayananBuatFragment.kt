package com.capstone.karira.activity.layanan

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.activity.MockupActivity
import com.capstone.karira.activity.auth.AuthActivity
import com.capstone.karira.component.compose.CustomTextField
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.component.compose.ImageCarouselUri
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.FragmentLayananBuatBinding
import com.capstone.karira.databinding.FragmentSelectRoleBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.ImageUrl
import com.capstone.karira.model.User
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananBuatViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananBuatFragment : Fragment() {

    private lateinit var id: String
    private var _binding: FragmentLayananBuatBinding? = null
    private val binding get() = _binding!!
    val layananBuatViewModel: LayananBuatViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

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
    val user = layananBuatViewModel.user.collectAsState(initial = User("", "ssss"))

    if (id != "null") {
        layananBuatViewModel.uiState
            .collectAsState(initial = UiState.Loading).value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        layananBuatViewModel.getServiceById(id)
                    }

                    is UiState.Success -> {
                        val service = uiState.data

                        var titleField by remember { mutableStateOf(service.title) }
                        var durationField by remember { mutableStateOf(service.max_duration.toString()) }
                        var categoryField by remember { mutableStateOf("") }
                        var descriptionField by remember { mutableStateOf(service.description) }
                        var imagesUri = remember { mutableStateListOf<ImageUrl>() }

                        val launcher = rememberLauncherForActivityResult(
                            contract =
                            ActivityResultContracts.GetContent()
                        ) { uri: Uri? ->
                            if (uri != null) {
                                imagesUri.add(element = ImageUrl(uri))
                            }
                        }

                        Column(
                            modifier = modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                                .fillMaxWidth()
                        ) {
                            TitleSection(
                                title = if (id == "null") stringResource(
                                    id = R.string.layanan_title,
                                    "Buat ",
                                    ""
                                ) else stringResource(id = R.string.layanan_title, "Edit ", ""),
                                subtitle = stringResource(
                                    id = R.string.layanan_titlesub_buat
                                )
                            )
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_title_formtitle),
                                text = titleField,
                                setText = { newText -> titleField = newText })
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_duration_formtitle),
                                text = durationField,
                                setText = { newText -> durationField = newText },
                                type = "Number"
                            )
                            //        CustomDropdownMenu(
                            //            stringResource(id = R.string.layanan_buat_category_formtitle),
                            //            text = categoryField,
                            //            setText = { newText -> categoryField = newText })
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_description_formtitle),
                                text = descriptionField, setText = { newText ->
                                    descriptionField = newText
                                })
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
                                onClick = {
                                    view.findNavController().popBackStack()
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
                                    if (id == "null") stringResource(id = R.string.layanan_buat_button) else stringResource(
                                        id = R.string.layanan_buat_button_alter
                                    )
                                )
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
        LayananBuatApp("1", LayananBuatViewModel(Injection.provideAuthRepostory(context)), View(context))
    }
}

