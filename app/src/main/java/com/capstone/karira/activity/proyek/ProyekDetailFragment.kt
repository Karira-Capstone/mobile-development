package com.capstone.karira.activity.proyek

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import coil.compose.AsyncImage
import com.capstone.karira.R
import com.capstone.karira.activity.layanan.LayananBuatFragment
import com.capstone.karira.component.compose.CenterHeadingWithDesc
import com.capstone.karira.component.compose.ImageCarousel
import com.capstone.karira.component.compose.SmallButton
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.databinding.FragmentLayananDetailBinding
import com.capstone.karira.databinding.FragmentProyekDetailBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Bid
import com.capstone.karira.model.Images
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananDetailViewModel
import com.capstone.karira.viewmodel.proyek.ProyekDetailViewModel
import com.dicoding.jetreward.ui.common.UiState

class ProyekDetailFragment : Fragment() {

    private lateinit var id: String
    private var _binding: FragmentProyekDetailBinding? = null
    private val binding get() = _binding!!
    val proyekDetailViewModel: ProyekDetailViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ProyekBuatFragment.EXTRA_ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProyekDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBinding(view)

    }

    fun handleBinding(view: View) {

        binding.pageSection.setContent {
            KariraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProyekDetailApp(id, proyekDetailViewModel, view)
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
private fun ProyekDetailApp(
    id: String,
    proyekDetailViewModel: ProyekDetailViewModel,
    view: View,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val userDataStore =
        proyekDetailViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    proyekDetailViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    proyekDetailViewModel.getProjectById(id)
                }

                is UiState.Success -> {
                    val project = uiState.data as Project
                    Column(
                        modifier = modifier
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                            .fillMaxWidth()
                    ) {
                        Row(modifier = Modifier) {
                            AsyncImage(
                                project.client?.user?.picture.toString(),
                                contentDescription = project.client?.user?.fullName.toString(),
                                modifier = Modifier
                                    .width(72.dp)
                                    .aspectRatio(1f / 1f)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    text = project.title.toString(),
                                    modifier = modifier,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = project.categoryId?.let { StaticDatas.categories[it - 1] }
                                        .toString(),
                                    modifier = modifier,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = project.client?.user?.fullName.toString(),
                                    modifier = modifier,
                                    color = colorResource(id = R.color.blackAlpha_300),
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .padding(top = 12.dp)
                                .height(IntrinsicSize.Min)
                                .padding(vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(end = 16.dp)) {
                                Text(
                                    project.bids?.size.toString(),
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    stringResource(id = R.string.proyek_detail_bids)
                                )
                            }
                            Divider(
                                color = colorResource(R.color.gray_200),
                                modifier = Modifier
                                    .height(16.dp) //fill the max height
                                    .width(1.dp)
                            )
                            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    "${project.duration.toString()} Hari",
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    stringResource(id = R.string.proyek_detail_duration)
                                )
                            }

                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .height(IntrinsicSize.Min)
                                .padding(vertical = 8.dp)
                        ) {
                            CenterHeadingWithDesc(
                                main = "Rp${createDotInNumber(project.lowerBound.toString())} - Rp${createDotInNumber(project.upperBound.toString())}",
                                subtext = stringResource(id = R.string.proyek_detail_payment)
                            )
                        }

                        Text(
                            text = project.description.toString(),
                            modifier = modifier.padding(vertical = 12.dp)
                        )
                        if (project.skills?.isNotEmpty() == true) {
                            FlowRow(
                                modifier = Modifier
                                    .padding(bottom = 8.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start,
                                content = {
                                    val skills = project.skills.map { StaticDatas.skills[(it.id as Int) - 1] }
                                    for (skill in skills) {
                                        if (skill != "") SmallButton(
                                            text = skill,
                                            isClosable = false,
                                            onClick = { })
                                    }
                                }
                            )
                        }
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            // ---------------------------------------------------------------
                            // ------------------------- GANTI COK ---------------------------
                            // ---------------------------------------------------------------
                            if (userDataStore.value.role == "WORKER") {
                                Button(
                                    onClick = { /*TODO*/ },
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
                                    Text(stringResource(id = R.string.proyek_detail_primary_button))
                                }
                                OutlinedButton(
                                    onClick = { /*TODO*/ },
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
                                    Text(stringResource(id = R.string.proyek_detail_outlined_button))
                                }
                            } else if (userDataStore.value.id == project.client?.userId.toString()) {
                                Button(
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(ProyekTawaranFragment.EXTRA_ID, id)
                                        view.findNavController().navigate(R.id.action_proyekDetailFragment_to_proyekTawaranFragment, bundle)
                                    },
                                    shape = RoundedCornerShape(16),
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(stringResource(id = R.string.proyek_detail_primary_button_alter))
                                }
                                OutlinedButton(
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(LayananBuatFragment.EXTRA_ID, project.id.toString())
                                        view.findNavController().navigate(R.id.action_proyekDetailFragment_to_proyekBuatFragment, bundle)
                                    },
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
                                    Text(stringResource(id = R.string.proyek_detail_outlined_button_alter))
                                }
                            }
                        }
                    }
                }

                is UiState.Error -> {}
                is UiState.Initiate -> {}
            }
        }

}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    val context = LocalContext.current
    KariraTheme {
        ProyekDetailApp(
            "1",
            ProyekDetailViewModel(Injection.provideProyekRepostory(context)),
            View(context)
        )
    }
}
