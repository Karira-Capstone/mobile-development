package com.capstone.karira.activity.proyek

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.capstone.karira.activity.layanan.LayananOrderFragment
import com.capstone.karira.component.compose.CenterHeadingWithDesc
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.component.compose.ImageCarousel
import com.capstone.karira.component.compose.SmallButton
import com.capstone.karira.component.compose.dialog.BiddingDialog
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
import com.capstone.karira.utils.downloadFile
import com.capstone.karira.utils.getFileNameFromUri
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val project = uiState.data as Project

                    val showDialog = remember { mutableStateOf(false) }
                    var isCreatedBidding by remember {
                        mutableStateOf(false)
                    }

                    if (showDialog.value) {
                        BiddingDialog(
                            project = project,
                            userDataStore = userDataStore.value,
                            proyekDetailViewModel = proyekDetailViewModel,
                            setShowDialog = {
                                showDialog.value = it
                            },
                            closeDialog = {
                                showDialog.value = false
                            })
                    }

                    proyekDetailViewModel.isCreated.collectAsState(initial = UiState.Initiate).value.let { isCreated ->
                        when (isCreated) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                Toast.makeText(
                                    context,
                                    "Bidding berhasil dibuat",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showDialog.value = false
                                isCreatedBidding = true
                            }

                            is UiState.Error -> {
                                Toast.makeText(context, isCreated.errorMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is UiState.Initiate -> {}
                        }
                    }

                    Column(
                        modifier = modifier
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                            .fillMaxWidth()
                    ) {
                        if (project.type.toString() == "ONREVIEW" || project.type.toString() == "CLOSED") {
                            Text(
                                text = if (project.type.toString() == "ONREVIEW") stringResource(id = R.string.proyek_detail_status_review) else stringResource(
                                    id = R.string.proyek_detail_status_closed
                                ),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(
                                        start = 24.dp,
                                        end = 24.dp,
                                        bottom = 24.dp
                                    )
                                    .fillMaxWidth()
                            )
                        }
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
                                    text = if (project.categoryId != null) StaticDatas.categories[project.categoryId!! - 1] else stringResource(
                                        R.string.proyek_detail_type_null
                                    ),
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
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .padding(top = 12.dp)
                                .height(IntrinsicSize.Min)
                                .padding(vertical = 8.dp)
                        ) {
                            CenterHeadingWithDesc(
                                main = if (isCreatedBidding) (project.bids?.size?.plus(
                                    1
                                )).toString() else project.bids?.size.toString(),
                                subtext = stringResource(id = R.string.proyek_detail_bids)
                            )
                            Divider(
                                color = colorResource(R.color.gray_200),
                                modifier = Modifier
                                    .height(16.dp) //fill the max height
                                    .width(1.dp)
                            )
                            CenterHeadingWithDesc(
                                main = "${project.duration.toString()} Hari",
                                subtext = stringResource(id = R.string.proyek_detail_duration)
                            )
                            Divider(
                                color = colorResource(R.color.gray_200),
                                modifier = Modifier
                                    .height(16.dp) //fill the max height
                                    .width(1.dp)
                            )

                            CenterHeadingWithDesc(
                                main = if (project.order != null) project.order.filter { it.type != "CANCELLED" }.size.toString() else "0",
                                subtext = stringResource(id = R.string.proyek_detail_order)
                            )
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
                                main = "Rp${createDotInNumber(project.lowerBound.toString())} - Rp${
                                    createDotInNumber(
                                        project.upperBound.toString()
                                    )
                                }",
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
                                    val skills =
                                        project.skills.map { StaticDatas.skills[(it.id as Int) - 1] }
                                    for (skill in skills) {
                                        if (skill != "") SmallButton(
                                            text = skill,
                                            isClosable = false,
                                            onClick = { })
                                    }
                                }
                            )
                        }
                        if (project.attachment != "") {
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colorResource(id = R.color.gray_200)),
                            ) {
                                DashedButton(
                                    text = project.attachment?.split("/")?.last().toString(),
                                    asInput = true,
                                    onClick = {
                                        downloadFile(
                                            context,
                                            project.attachment,
                                            project.attachment?.split("/")?.last().toString()
                                        )
                                    },
                                )
                            }
                        }
                        if (userDataStore.value.id == project.client?.userId.toString()) {
                            Row() {
                                OutlinedButton(
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(
                                            ProyekBuatFragment.EXTRA_ID,
                                            project.id.toString()
                                        )
                                        view.findNavController().navigate(
                                            R.id.action_proyekDetailFragment_to_proyekBuatFragment,
                                            bundle
                                        )
                                    },
                                    shape = RoundedCornerShape(16),
                                    border = BorderStroke(1.dp, colorResource(R.color.purple_500)),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(R.color.purple_500),
                                        containerColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(stringResource(id = R.string.proyek_detail_outlined_button_alter))
                                }
                            }
                        }
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            if (userDataStore.value.role == "WORKER") {
                                Button(
                                    onClick = {
                                        showDialog.value = true
                                    },
                                    shape = RoundedCornerShape(16),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Color.White,
                                        containerColor = colorResource(R.color.purple_500)
                                    ),
                                    modifier = Modifier
                                        .padding(end = 8.dp)
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
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(stringResource(id = R.string.proyek_detail_outlined_button))
                                }
                            } else if (userDataStore.value.id == project.client?.userId.toString()) {
                                if (project.type != "ONREVIEW") {
                                    Button(
                                        onClick = {
                                            val bundle = Bundle()
                                            bundle.putString(ProyekTawaranFragment.EXTRA_ID, id)
                                            view.findNavController().navigate(
                                                R.id.action_proyekDetailFragment_to_proyekTawaranFragment,
                                                bundle
                                            )
                                        },
                                        shape = RoundedCornerShape(16),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.White,
                                            containerColor = colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .fillMaxWidth()
                                            .weight(1f)
                                    ) {
                                        Text(stringResource(id = R.string.proyek_detail_primary_button_alter))
                                    }
                                    Button(
                                        onClick = {
                                            val bundle = Bundle()
                                            bundle.putInt(
                                                ProyekOrderFragment.EXTRA_ID,
                                                project.id!!
                                            )
                                            bundle.putString(
                                                ProyekOrderFragment.EXTRA_TYPE,
                                                project.title
                                            )
                                            view.findNavController().navigate(
                                                R.id.action_proyekDetailFragment_to_proyekOrderFragment,
                                                bundle
                                            )
                                        },
                                        shape = RoundedCornerShape(16),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.White,
                                            containerColor = colorResource(R.color.purple_500)
                                        ),
                                        modifier = Modifier
                                            .padding()
                                            .fillMaxWidth()
                                            .weight(1f)
                                    ) {
                                        Text(stringResource(id = R.string.layanan_detail_primary_button_alter))
                                    }
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
