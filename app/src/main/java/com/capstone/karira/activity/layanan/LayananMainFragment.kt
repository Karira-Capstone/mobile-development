package com.capstone.karira.activity.layanan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.databinding.FragmentLayananMainBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananMainViewModel
import com.dicoding.jetreward.ui.common.UiState
import kotlin.properties.Delegates

class LayananMainFragment : Fragment() {

    private var id = 8
    private var _binding: FragmentLayananMainBinding? = null
    private val binding get() = _binding!!
    val layananMainViewModel: LayananMainViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(EXTRA_ID, 0) - 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLayananMainBinding.inflate(inflater, container, false)
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
                    LayananMainApp(layananMainViewModel, view, id)
                }
            }
        }

    }

    companion object {
        const val EXTRA_ID = "kinlian"
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LayananMainApp(layananMainViewModel: LayananMainViewModel, view: View, id: Int) {

    val context = LocalContext.current
    val userDataStore = layananMainViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    layananMainViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    layananMainViewModel.getLayanansByCategory(id)
                }
                is UiState.Success -> {
                    val data = uiState.data as List<Service>
                    val listState = rememberLazyListState()
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item {
                            Column(modifier = Modifier) {
                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                                    TitleSection(
                                        title = stringResource(id = R.string.layanan_title, "", " ${StaticDatas.categories[id]}"),
                                        subtitle = stringResource(
                                            id = R.string.layanan_titlesub
                                        )
                                    )
                                    Row(
                                        modifier = Modifier,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                view.findNavController().navigate(R.id.action_layananMainFragment_to_layananSearchFragment)
                                            },
                                            shape = RoundedCornerShape(16),
                                            border = BorderStroke(1.dp, colorResource(R.color.purple_500)),
                                            colors = ButtonDefaults.buttonColors(
                                                contentColor = colorResource(R.color.purple_500),
                                                containerColor = Color.White
                                            ),
                                            modifier = Modifier
                                                .padding(end = if (userDataStore.value.role == "CLIENT") 4.dp else 0.dp)
                                                .fillMaxWidth()
                                                .weight(1f)
                                        ) {
                                            Text(stringResource(id = R.string.layanan_cari_button))
                                        }
                                        Log.d("LLLLLLLLLLLLLLL", userDataStore.value.toString())
                                        if (userDataStore.value.role == "CLIENT") {
                                            Button(
                                                onClick = {
                                                    view.findNavController().navigate(R.id.action_layananMainFragment_to_rekomendasiFragment)
                                                },
                                                shape = RoundedCornerShape(16),
                                                modifier = Modifier
                                                    .padding(start = 4.dp)
                                                    .fillMaxWidth()
                                                    .weight(1f)
                                            ) {
                                                Text(stringResource(id = R.string.layanan_main_button_rekomendasi))
                                            }
                                        }
                                    }
                                }
                                Divider(
                                    color = colorResource(R.color.gray_200),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                )
                            }
                        }
                        items(data, key = { it.id.toString() }) { service ->
                            ItemCard(
                                image = service.worker?.user?.picture.toString(),
                                title = service.title.toString(),
                                subtitle = service.worker?.user?.fullName.toString(),
                                price = createDotInNumber(service.price.toString()),
                                onClick = {
                                    val bundle = Bundle()
                                    bundle.putString(LayananDetailFragment.EXTRA_ID, service.id.toString())
                                    view.findNavController().navigate(R.id.action_layananMainFragment_to_layananDetailFragment, bundle)
                                })
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
private fun Preview(){
    val context = LocalContext.current
    LayananMainApp(layananMainViewModel = LayananMainViewModel(Injection.provideLayananRepostory(context)), View(context), 1)
}