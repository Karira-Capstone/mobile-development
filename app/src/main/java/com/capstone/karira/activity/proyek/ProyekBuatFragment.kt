package com.capstone.karira.activity.proyek

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.component.compose.CustomTextField
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.component.compose.ImageCarouselUri
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.databinding.FragmentLayananBuatBinding
import com.capstone.karira.databinding.FragmentProyekBuatBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.DummyDatas
import com.capstone.karira.model.ImageUrl
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.getFileNameFromUri
import com.capstone.karira.utils.reduceFileImage
import com.capstone.karira.utils.uriToFile
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananBuatViewModel
import com.capstone.karira.viewmodel.proyek.ProyekBuatViewModel
import com.dicoding.jetreward.ui.common.UiState
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Collections.addAll

class ProyekBuatFragment : Fragment() {

    private lateinit var id: String
    private var _binding: FragmentProyekBuatBinding? = null
    private val binding get() = _binding!!
    val proyekBuatViewModel: ProyekBuatViewModel by viewModels {
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
        _binding = FragmentProyekBuatBinding.inflate(inflater, container, false)
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
                    ProyekBuatApp(id, proyekBuatViewModel, view)
                }
            }
        }
    }


    companion object {
        const val EXTRA_ID = "kinlian"
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProyekBuatApp(
    id: String,
    proyekBuatViewModel: ProyekBuatViewModel,
    view: View,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()
    val userDataStore =
        proyekBuatViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    proyekBuatViewModel.isCreated.collectAsState(initial = UiState.Initiate).value.let { isCreated ->
        when (isCreated) {
            is UiState.Success -> {
                val project = isCreated.data as Project

                var message = "";
                if (id == "null") message = "Proyek ${project.title.toString()} berhasil dibuat"
                else message = "Proyek ${project.title.toString()} berhasil diedit"

                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()

                val bundle = Bundle()
                bundle.putString(ProyekBuatFragment.EXTRA_ID, project.id.toString())
                view.findNavController().navigate(R.id.action_proyekBuatFragment_to_proyekDetailFragment, bundle)
            }
            else -> {}
        }
    }

    proyekBuatViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    proyekBuatViewModel.getProjectById(id)
                }

                is UiState.Success -> {
                    val project = uiState.data as Project

                    var titleField by remember { mutableStateOf(project.title) }
                    var durationField by remember { mutableStateOf(project.duration.toString()) }
                    var descriptionField by remember { mutableStateOf(project.description) }
                    var fileUri by remember { mutableStateOf(project.attachment?.split("/")?.last()) }
                    var file by remember { mutableStateOf<File?>(null) }

                    val launcher = rememberLauncherForActivityResult(
                        contract =
                        ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        if (uri != null) {
                            file = uriToFile(uri, context)
                            fileUri = uri.toString()
                        }
                    }

                    Column(
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                            .fillMaxWidth()
                    ) {
                        TitleSection(
                            title = stringResource(id = R.string.proyek_title, "Edit ", ""),
                            subtitle = stringResource(
                                id = R.string.proyek_titlesub_buat
                            )
                        )
                        titleField?.let {
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_title_formtitle),
                                text = it,
                                setText = { newText -> titleField = newText })
                        }
                        CustomTextField(
                            stringResource(id = R.string.layanan_buat_duration_formtitle),
                            text = durationField,
                            setText = { newText -> durationField = newText },
                            type = "Number"
                        )
                        descriptionField?.let {
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_description_formtitle),
                                text = it, setText = { newText ->
                                    descriptionField = newText
                                })
                        }
                        if (fileUri == null && file == null) {
                            DashedButton(
                                text = stringResource(id = R.string.proyek_buat_file_uploadboxtitle),
                                onClick = { launcher.launch("*/*") }
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colorResource(id = R.color.gray_200))
                            ) {
                                DashedButton(
                                    text = if (file != null) getFileNameFromUri(Uri.parse(fileUri), context).toString() else fileUri.toString(),
                                    asInput = true,
                                    onClick = { },
                                )
                                FilledTonalIconButton(
                                    onClick = {
                                        file = null
                                        fileUri = null
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.TopEnd)
                                        .size(32.dp)
                                ) {
                                    Icon(Icons.Outlined.Close, contentDescription = "Close", modifier = Modifier.size(24.dp))
                                }
                            }
                        }
//                        if (imagesUri.isNotEmpty()) {
//                            ImageCarouselUri(
//                                images = imagesUri,
//                                handleImage = { index ->
//                                    run {
//                                        imagesUri.removeAt(index)
//                                        filesUri[index] = null
//                                    }
//                                })
//                        }
                        // ------------------------------------------- RECCOMENDATION -----------------------------------
                        proyekBuatViewModel.isRecommended.collectAsState(initial = UiState.Initiate).value.let { recommendationState ->
                            when (recommendationState) {
                                is UiState.Success -> {
                                    val recommendationData = recommendationState.data as Project

                                    var lowerBoundField by remember { mutableStateOf(recommendationData.lowerBound.toString()) }
                                    var upperBoundField by remember { mutableStateOf(recommendationData.upperBound.toString()) }

                                    Column(modifier = Modifier.padding(top = 16.dp)) {
                                        Text(
                                            text = stringResource(id = R.string.layanan_buat_category_formtitle),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                        )
                                        Text(
                                            text = StaticDatas.categories[(recommendationData.categoryId as Int) - 1],
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                    lowerBoundField?.let {
                                        CustomTextField(
                                            stringResource(id = R.string.proyek_buat_lowerbound_formtitle),
                                            text = it,
                                            type = "Number",
                                            setText = { newText -> lowerBoundField = newText })
                                    }
                                    upperBoundField?.let {
                                        CustomTextField(
                                            stringResource(id = R.string.proyek_buat_upperbound_formtitle),
                                            text = it,
                                            type = "Number",
                                            setText = { newText -> upperBoundField = newText })
                                    }
                                    Column() {
                                        Text(
                                            text = stringResource(id = R.string.layanan_buat_tags_formtitle),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                        )
//                                        FlowRow(
//                                            modifier = Modifier
//                                                .padding(top = 4.dp),
//                                            verticalAlignment = Alignment.Top,
//                                            horizontalArrangement = Arrangement.Start,
//                                            content = {
//                                                for (skill in recommendationData.s) {
//                                                    if (skill != "") SmallButton(
//                                                        text = skill,
//                                                        isClosable = false,
//                                                        onClick = { })
//                                                }
//                                            }
//                                        )
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            // Todo recommendation service
                                            proyekBuatViewModel.findReccomendation(
                                                titleField.toString(),
                                                descriptionField.toString(),
                                                durationField,
                                                project
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
                                        onClick = {
                                            // Todo update service
                                            try {
                                                proyekBuatViewModel.updateProject(
                                                    project.id.toString(),
                                                    userDataStore.value.token,
                                                    titleField.toString(),
                                                    durationField.toInt(),
                                                    descriptionField.toString(),
                                                    lowerBoundField.toInt(),
                                                    upperBoundField.toInt(),
                                                    recommendationData.categoryId as Int,
                                                    recommendationData.skills,
                                                    fileUri,
                                                    file
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
                                                id = R.string.proyek_buat_button_alter
                                            )
                                        )
                                    }
                                }

                                is UiState.Initiate -> {
                                    Button(
                                        onClick = {
                                            // Todo recommendation service
                                            proyekBuatViewModel.findReccomendation(
                                                titleField.toString(),
                                                descriptionField.toString(),
                                                durationField,
                                                project
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

                                is UiState.Error -> {}
                                is UiState.Loading -> {}
                            }
                        }
                        // ----------------------------------------------------------------------------------------------

                    }
                }

                is UiState.Error -> {}
                is UiState.Initiate -> {
                    var titleField by remember { mutableStateOf("") }
                    var durationField by remember { mutableStateOf("") }
                    var descriptionField by remember { mutableStateOf("") }
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

                    Column(
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                            .fillMaxWidth()
                    ) {
                        TitleSection(
                            title = stringResource(
                                id = R.string.proyek_title,
                                "Buat ",
                                ""
                            ),
                            subtitle = stringResource(
                                id = R.string.proyek_titlesub_buat
                            )
                        )
                        titleField?.let {
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_title_formtitle),
                                text = it,
                                setText = { newText -> titleField = newText })
                        }
                        CustomTextField(
                            stringResource(id = R.string.layanan_buat_duration_formtitle),
                            text = durationField,
                            setText = { newText -> durationField = newText },
                            type = "Number"
                        )
                        descriptionField?.let {
                            CustomTextField(
                                stringResource(id = R.string.layanan_buat_description_formtitle),
                                text = it, setText = { newText ->
                                    descriptionField = newText
                                })
                        }
                        if (file == null) {
                            DashedButton(
                                text = stringResource(id = R.string.proyek_buat_file_uploadboxtitle),
                                onClick = { launcher.launch("*/*") }
                            )
                        } else {
                           fileName?.let {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(colorResource(id = R.color.gray_200))
                                ) {
                                    DashedButton(
                                        text = it,
                                        asInput = true,
                                        onClick = { },
                                    )
                                    FilledTonalIconButton(
                                        onClick = { file = null },
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .align(Alignment.TopEnd)
                                            .size(32.dp)
                                    ) {
                                        Icon(Icons.Outlined.Close, contentDescription = "Close", modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        }

                        // ------------------------------------------- RECCOMENDATION -----------------------------------
                        proyekBuatViewModel.isRecommended.collectAsState(initial = UiState.Initiate).value.let { recommendationState ->
                            when (recommendationState) {
                                is UiState.Success -> {
                                    val recommendationData = recommendationState.data as Project

                                    var lowerBoundField by remember { mutableStateOf(recommendationData.lowerBound.toString()) }
                                    var upperBoundField by remember { mutableStateOf(recommendationData.upperBound.toString()) }

                                    Column(modifier = Modifier.padding(top = 16.dp)) {
                                        Text(
                                            text = stringResource(id = R.string.layanan_buat_category_formtitle),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                        )
                                        Text(
                                            text = StaticDatas.categories[(recommendationData.categoryId as Int) - 1],
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                    lowerBoundField?.let {
                                        CustomTextField(
                                            stringResource(id = R.string.proyek_buat_lowerbound_formtitle),
                                            text = it,
                                            type = "Number",
                                            setText = { newText -> lowerBoundField = newText })
                                    }
                                    upperBoundField?.let {
                                        CustomTextField(
                                            stringResource(id = R.string.proyek_buat_upperbound_formtitle),
                                            text = it,
                                            type = "Number",
                                            setText = { newText -> upperBoundField = newText })
                                    }
                                    Column() {
                                        Text(
                                            text = stringResource(id = R.string.layanan_buat_tags_formtitle),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                        )
//                                        FlowRow(
//                                            modifier = Modifier
//                                                .padding(top = 4.dp),
//                                            verticalAlignment = Alignment.Top,
//                                            horizontalArrangement = Arrangement.Start,
//                                            content = {
//                                                for (skill in recommendationData.s) {
//                                                    if (skill != "") SmallButton(
//                                                        text = skill,
//                                                        isClosable = false,
//                                                        onClick = { })
//                                                }
//                                            }
//                                        )
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            // Todo recommendation service
                                            proyekBuatViewModel.findReccomendation(
                                                titleField,
                                                descriptionField,
                                                durationField,
                                                DummyDatas.projectDatas[0]
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
                                        onClick = {
                                            // Todo create service
                                            try {
                                                proyekBuatViewModel.createProject(
                                                    userDataStore.value.token,
                                                    titleField,
                                                    durationField.toInt(),
                                                    descriptionField,
                                                    lowerBoundField.toInt(),
                                                    upperBoundField.toInt(),
                                                    recommendationData.categoryId as Int,
                                                    recommendationData.skills,
                                                    file
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
                                                id = R.string.proyek_buat_button
                                            )
                                        )
                                    }
                                }

                                is UiState.Initiate -> {
                                    Button(
                                        onClick = {
                                            // Todo recommendation service
                                            proyekBuatViewModel.findReccomendation(
                                                titleField,
                                                descriptionField,
                                                durationField,
                                                DummyDatas.projectDatas[0]
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

                                is UiState.Error -> {}
                                is UiState.Loading -> {}
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
        ProyekBuatApp(
            "1",
            ProyekBuatViewModel(Injection.provideProyekRepostory(context)),
            View(context)
        )
    }
}

