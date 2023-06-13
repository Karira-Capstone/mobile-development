package com.capstone.karira.activity.layanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.FragmentRekomendasiBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.rekomendasi.RekomendasiViewModel
import com.dicoding.jetreward.ui.common.UiState


class RekomendasiFragment : Fragment() {

    private var _binding: FragmentRekomendasiBinding? = null
    private val binding get() = _binding!!
    val rekomendasiViewModel: RekomendasiViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRekomendasiBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBinding(view)

    }

    fun handleBinding(view: View) {

        binding.mainSection.setContent {
            KariraTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RekomendasiApp(rekomendasiViewModel, view)
                }
            }
        }

    }

}

@Composable
private fun RekomendasiApp(rekomendasiViewModel: RekomendasiViewModel, view: View) {

    val context = LocalContext.current
    val userDataStore = rekomendasiViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    if (userDataStore.value.role == "WORKER") {
        rekomendasiViewModel.uiStateProject
            .collectAsState(initial = UiState.Loading).value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        rekomendasiViewModel.getUserProjectRecommendation(userDataStore.value.token)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Success -> {
                        val data = uiState.data as List<Project>
                        val listState = rememberLazyListState()
                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                            item {
                                Column(modifier = Modifier) {
                                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                                        TitleSection(
                                            title = stringResource(id = R.string.proyek_title, "Rekomendasi ", ""),
                                            subtitle = stringResource(
                                                id = R.string.rekomendasi_subtitle, "proyek"
                                            )
                                        )
                                    }
                                    Divider(
                                        color = colorResource(R.color.gray_200),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                    )
                                }
                            }
                            if (data.size > 0) {
                                items(data, key = { it.id.toString() }) { project ->
                                    ItemCard(
                                        image = project.client?.user?.picture.toString(),
                                        title = project.title.toString(),
                                        subtitle = project.client?.user?.fullName.toString(),
                                        price = "${project.lowerBound.toString()} - Rp${project.upperBound.toString()}",
                                        onClick = {
                                            val bundle = Bundle()
                                            bundle.putString(LayananDetailFragment.EXTRA_ID, project.id.toString())
                                            view.findNavController().navigate(R.id.action_rekomendasiFragment_to_proyekDetailFragment, bundle)
                                        })
                                }
                            } else {
                                item {
                                    Text(
                                        text = stringResource(id = R.string.rekomendasi_empty),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 96.dp).fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Error -> {}
                    is UiState.Initiate -> {}
                }
            }
    }
    else if (userDataStore.value.role == "CLIENT") {
        rekomendasiViewModel.uiStateService
            .collectAsState(initial = UiState.Loading).value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        rekomendasiViewModel.getUserServiceRecommendation(userDataStore.value.token)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Success -> {
                        val data = uiState.data as List<Service>
                        val listState = rememberLazyListState()
                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                            item {
                                Column(modifier = Modifier) {
                                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                                        TitleSection(
                                            title = stringResource(id = R.string.layanan_title, "Rekomendasi ", ""),
                                            subtitle = stringResource(
                                                id = R.string.rekomendasi_subtitle, "layanan"
                                            )
                                        )
                                    }
                                    Divider(
                                        color = colorResource(R.color.gray_200),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                    )
                                }
                            }
                            if (data.size > 0) {
                                items(data, key = { it.id.toString() }) { service ->
                                    ItemCard(
                                        image = service.worker?.user?.picture.toString(),
                                        title = service.title.toString(),
                                        subtitle = service.worker?.user?.fullName.toString(),
                                        price = service.price.toString(),
                                        onClick = {
                                            val bundle = Bundle()
                                            bundle.putString(LayananDetailFragment.EXTRA_ID, service.id.toString())
                                            view.findNavController().navigate(R.id.action_rekomendasiFragment_to_layananDetailFragment, bundle)
                                        })
                                }
                            } else {
                                item {
                                    Text(
                                        text = stringResource(id = R.string.rekomendasi_empty),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 96.dp).fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Error -> {}
                    is UiState.Initiate -> {}
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(){
    val context = LocalContext.current
    RekomendasiApp(RekomendasiViewModel(Injection.provideRekomendasiRepostory(context)), View(context))
}