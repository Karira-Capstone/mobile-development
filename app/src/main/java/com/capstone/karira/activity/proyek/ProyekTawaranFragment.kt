package com.capstone.karira.activity.proyek

import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.core.os.BundleCompat.getParcelableArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.databinding.FragmentProyekBuatBinding
import com.capstone.karira.databinding.FragmentProyekTawaranBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Bid
import com.capstone.karira.model.Project
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.proyek.ProyekBuatViewModel
import com.capstone.karira.viewmodel.proyek.ProyekMainViewModel
import com.capstone.karira.viewmodel.proyek.ProyekTawaranViewModel
import com.dicoding.jetreward.ui.common.UiState

class ProyekTawaranFragment : Fragment() {

    private lateinit var id: String
    private var _binding: FragmentProyekTawaranBinding? = null
    private val binding get() = _binding!!
    val proyekTawaranViewModel: ProyekTawaranViewModel by viewModels {
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
        _binding = FragmentProyekTawaranBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBinding(view)

    }

    private fun handleBinding(view: View) {
        binding.pageSection.setContent {
            KariraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProyekTawaranApp(id, proyekTawaranViewModel, view)
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
private fun ProyekTawaranApp(id: String, proyekTawaranViewModel: ProyekTawaranViewModel, view: View) {

    val context = LocalContext.current
    val userDataStore = proyekTawaranViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    proyekTawaranViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    proyekTawaranViewModel.getProjectById(id)
                }
                is UiState.Success -> {
                    val data = uiState.data as Project
                    val listState = rememberLazyListState()
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item {
                            Column(modifier = Modifier) {
                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                                    TitleSection(
                                        title = stringResource(id = R.string.proyek_tawaran_title,  "${data.title}"),
                                        subtitle = stringResource(
                                            id = R.string.proyek_tawaran_subtitle, (data.bids as List<Bid>).size
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
                        if (data.bids != null && data.bids?.size!! > 0) {
                            items(data.bids, key = { it.id.toString() }) { bid ->
                                ItemCard(
                                    image = bid.worker?.user?.picture.toString(),
                                    title = bid.worker?.user?.fullName.toString(),
                                    subtitle = "",
                                    price = bid.price.toString(),
                                    onClick = {
//                                    val bundle = Bundle()
//                                    bundle.putString(ProyekDetailFragment.EXTRA_ID, project.id.toString())
//                                    view.findNavController().navigate(R.id.action_proyekMainFragment_to_proyekDetailFragment, bundle)
                                    })
                            }
                        } else {
                            item {
                                Text(
                                    text = stringResource(id = R.string.proyek_tawaran_empty),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 96.dp)
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

@Preview(showBackground = true)
@Composable
private fun Preview(){
    val context = LocalContext.current
    ProyekTawaranApp("1", proyekTawaranViewModel = ProyekTawaranViewModel(Injection.provideProyekRepostory(context)), View(context))
}