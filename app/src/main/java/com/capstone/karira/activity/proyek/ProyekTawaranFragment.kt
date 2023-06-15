package com.capstone.karira.activity.proyek

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.karira.R
import com.capstone.karira.activity.payment.PaymentActivity
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.component.compose.dialog.ConfirmBiddingDialog
import com.capstone.karira.databinding.FragmentProyekTawaranBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Bid
import com.capstone.karira.model.Project
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.viewmodel.ViewModelFactory
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val data = uiState.data as Project
                    val listState = rememberLazyListState()

                    val showDialog = remember { mutableStateOf(false) }
                    val selectedBidId = remember { mutableStateOf(0) }

                    if (showDialog.value) {
                        ConfirmBiddingDialog(
                            bid = data.bids?.find { it.id == selectedBidId.value } as Bid,
                            userDataStore = userDataStore.value,
                            proyekTawaranViewModel = proyekTawaranViewModel,
                            setShowDialog = {
                                showDialog.value = it
                            },
                            closeDialog = {
                                showDialog.value = false
                            })
                    }

                    proyekTawaranViewModel.isCreated.collectAsState().value.let { isCreated ->
                        when (isCreated) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                Toast.makeText(context, "Berhasil memesan, lanjutkan ke pembayaran", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, PaymentActivity::class.java)
                                intent.putExtra("URL", "https://app.midtrans.com/snap/v3/redirection/97c74c12-0956-4c84-aa7d-e2a6efd77f82")
                                context.startActivity(intent)
                            }
                            is UiState.Initiate -> {}
                            is UiState.Error -> {
                                Toast.makeText(context, isCreated.errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

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
                                    subtitle = bid.message.toString(),
                                    price = createDotInNumber(bid.price.toString()),
                                    onClick = {
                                        selectedBidId.value = bid.id!!
                                        showDialog.value = true
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
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp, vertical = 96.dp)
                                        .fillMaxWidth()
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