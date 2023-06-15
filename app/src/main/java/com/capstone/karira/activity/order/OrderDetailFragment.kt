package com.capstone.karira.activity.order

import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.capstone.karira.R
import com.capstone.karira.component.compose.DashedButton
import com.capstone.karira.component.compose.TitleWithValue
import com.capstone.karira.component.compose.dialog.BiddingDetail
import com.capstone.karira.component.compose.dialog.ConfirmationDialog
import com.capstone.karira.databinding.FragmentOrderDetailBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Bid
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.Order
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.utils.downloadFile
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.order.OrderDetailViewModel
import com.dicoding.jetreward.ui.common.UiState

class OrderDetailFragment : Fragment() {

    private lateinit var id: String
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    val orderDetailViewModel: OrderDetailViewModel by viewModels {
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
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
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
                    OrderDetailApp(id, orderDetailViewModel, view)
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "kinlian"
    }

}

@Composable
private fun OrderDetailApp(
    id: String,
    orderDetailViewModel: OrderDetailViewModel,
    view: View,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val userDataStore =
        orderDetailViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    if (userDataStore.value.role == "WORKER" || userDataStore.value.role == "CLIENT") {
        orderDetailViewModel.uiState
            .collectAsState(initial = UiState.Loading).value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        orderDetailViewModel.getOrder(userDataStore.value.token, id)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        val order = uiState.data as Order

                        val showDialog = remember { mutableStateOf(false) }
                        val showConfimationDialog = remember { mutableStateOf(false) }

                        if (order.bid != null && showDialog.value) {
                            BiddingDetail(
                                worker = order.worker as Freelancer,
                                bid = order.bid as Bid,
                                setShowDialog = {
                                    showDialog.value = it
                                },
                                closeDialog = {
                                    showDialog.value = false
                                }
                            )
                        }
                        if (showConfimationDialog.value) {
                            ConfirmationDialog(
                                mainText = if (order.serviceId != null) stringResource(id = R.string.order_dialog_canceltext) else stringResource(
                                    id = R.string.order_dialog_finishtext
                                ),
                                buttonText = if (order.serviceId != null) stringResource(id = R.string.order_cancelorder_button) else stringResource(
                                    id = R.string.order_finishorder_button
                                ),
                                onClick = {
                                    if (order.serviceId != null) orderDetailViewModel.cancelOrder(
                                        userDataStore.value.token,
                                        order.id.toString()
                                    ) else orderDetailViewModel.finishOrder(
                                        userDataStore.value.token,
                                        order.id.toString()
                                    )
                                },
                                setShowDialog = { showConfimationDialog.value = it },
                                closeDialog = { showConfimationDialog.value = false }
                            )
                        }

                        orderDetailViewModel.isUpdated.collectAsState().value.let { isCreated ->
                            when (isCreated) {
                                is UiState.Loading -> {}
                                is UiState.Success -> {
                                    Toast.makeText(
                                        context,
                                        isCreated.data as String,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    view.findNavController().popBackStack()
                                }

                                is UiState.Initiate -> {}
                                is UiState.Error -> {
                                    Toast.makeText(context, isCreated.errorMessage, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                        Column(
                            modifier = modifier
                                .verticalScroll(rememberScrollState())
                                .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                                .fillMaxWidth()
                        ) {

                            Text(
                                text = "Status ${order.status.toString()}",
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
                            TitleWithValue(
                                title = stringResource(id = R.string.proyek_detail_dialog_price),
                                value = "Rp${createDotInNumber(order.price.toString())}"
                            )
                            TitleWithValue(
                                title = stringResource(id = R.string.proyek_detail_dialog_message),
                                value = order.description.toString()
                            )
                            if (order.attachment != null ) {
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(colorResource(id = R.color.gray_200)),
                                ) {
                                    DashedButton(
                                        text = order.attachment?.split("/")?.last().toString(),
                                        asInput = true,
                                        onClick = {
                                            downloadFile(
                                                context,
                                                order.attachment,
                                                order.attachment?.split("/")?.last().toString()
                                            )
                                        },
                                    )
                                }
                            }
                            if (order.bid != null) {
                                Column(modifier = Modifier.padding(top = 16.dp)) {
                                    Text(
                                        text = stringResource(id = R.string.order_tawaran_formtitle),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier
                                    )
                                    OutlinedButton(
                                        onClick = {
                                            showDialog.value = true
                                        },
                                        shape = RoundedCornerShape(16),
                                        border = BorderStroke(
                                            1.dp,
                                            colorResource(R.color.purple_500)
                                        ),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = colorResource(R.color.purple_500),
                                            containerColor = Color.White
                                        ),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text(stringResource(id = R.string.order_bid_button))
                                    }
                                }

                            }
                            if (order.serviceId != null && (order.status == "CREATED" || order.status == "ACCEPTED")) {
                                OutlinedButton(
                                    onClick = {
                                        showConfimationDialog.value = true
                                    },
                                    shape = RoundedCornerShape(16),
                                    border = BorderStroke(
                                        1.dp,
                                        colorResource(R.color.purple_500)
                                    ),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(R.color.purple_500),
                                        containerColor = Color.White
                                    ),
                                    modifier = Modifier.padding(top = 24.dp).fillMaxWidth()
                                ) {
                                    Text(stringResource(id = R.string.order_cancelorder_button))
                                }
                            } else if (userDataStore.value.role == "CLIENT" && order.projectId != null && order.status == "PAID") {
                                OutlinedButton(
                                    onClick = {
                                        showConfimationDialog.value = true
                                    },
                                    shape = RoundedCornerShape(16),
                                    border = BorderStroke(
                                        1.dp,
                                        colorResource(R.color.purple_500)
                                    ),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(R.color.purple_500),
                                        containerColor = Color.White
                                    ),
                                    modifier = Modifier.padding(top = 24.dp).fillMaxWidth()
                                ) {
                                    Text(stringResource(id = R.string.order_finishorder_button))
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
private fun GreetingPreview() {
    val context = LocalContext.current
    KariraTheme {
        OrderDetailApp(
            "1",
            OrderDetailViewModel(Injection.provideOrderRepository(context)),
            View(context)
        )
    }
}
