package com.capstone.karira.activity.layanan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
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
import com.capstone.karira.activity.payment.PaymentActivity
import com.capstone.karira.component.compose.CenterHeadingWithDesc
import com.capstone.karira.component.compose.ImageCarousel
import com.capstone.karira.component.compose.SmallButton
import com.capstone.karira.component.compose.dialog.BiddingDialog
import com.capstone.karira.component.compose.dialog.OrderFromService
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.databinding.FragmentLayananDetailBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Images
import com.capstone.karira.model.Order
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananDetailViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananDetailFragment : Fragment() {

    private lateinit var id: String
    private var _binding: FragmentLayananDetailBinding? = null
    private val binding get() = _binding!!
    val layananDetailViewModel: LayananDetailViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(LayananBuatFragment.EXTRA_ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLayananDetailBinding.inflate(inflater, container, false)
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
                    LayananDetailApp(id, layananDetailViewModel, view)
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
private fun LayananDetailApp(
    id: String,
    layananDetailViewModel: LayananDetailViewModel,
    view: View,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val userDataStore =
        layananDetailViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    layananDetailViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    layananDetailViewModel.getServiceById(id)
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

                    val showDialog = remember { mutableStateOf(false) }

                    if (showDialog.value) {
                        OrderFromService(
                            service = service,
                            userDataStore = userDataStore.value,
                            layananDetailViewModel = layananDetailViewModel,
                            setShowDialog = {
                                showDialog.value = it
                            },
                            closeDialog = {
                                showDialog.value = false
                            })
                    }

                    layananDetailViewModel.isCreated.collectAsState().value.let { isCreated ->
                        when (isCreated) {
                            is UiState.Loading -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            is UiState.Success -> {
                                Toast.makeText(
                                    context,
                                    "Berhasil memesan, lanjutkan ke pembayaran",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val orderData = isCreated.data as Order

                                val intent = Intent(context, PaymentActivity::class.java)
                                intent.putExtra("URL", orderData.midtransRedirectUri)
                                context.startActivity(intent)
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
                        if (service.type.toString() == "ONREVIEW" || service.type.toString() == "CLOSED") {
                            Text(
                                text = if (service.type.toString() == "ONREVIEW") stringResource(id = R.string.layanan_detail_status_review) else stringResource(
                                    id = R.string.layanan_detail_status_closed
                                ),
                                fontSize = 16.sp,
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
                                service.worker?.user?.picture.toString(),
                                contentDescription = service.worker?.user?.fullName.toString(),
                                modifier = Modifier
                                    .width(72.dp)
                                    .aspectRatio(1f / 1f)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    text = service.title.toString(),
                                    modifier = modifier,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (service.categoryId != null) StaticDatas.categories[service.categoryId!! - 1] else stringResource(
                                        R.string.layanan_detail_type_null
                                    ),
                                    modifier = modifier,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = service.worker?.user?.fullName.toString(),
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
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Text(
                                    "Rp${createDotInNumber(service.price.toString())}",
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    stringResource(id = R.string.layanan_buat_price_formtitle)
                                )
                            }
                            Divider(
                                color = colorResource(R.color.gray_200),
                                modifier = Modifier
                                    .height(16.dp) //fill the max height
                                    .width(1.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                Text(
                                    if (service.orders != null) service.orders.filter { it.type != "CANCELLED" }.size.toString() else "0",
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    stringResource(id = R.string.layanan_detail_user)
                                )
                            }

                        }
                        if (service.images?.foto1 != null) {
                            ImageCarousel(service.images as Images)
                        }
                        Text(
                            text = service.description.toString(),
                            modifier = modifier.padding(vertical = 12.dp)
                        )
                        if (service.skills?.isNotEmpty() == true) {
                            FlowRow(
                                modifier = Modifier
                                    .padding(bottom = 8.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start,
                                content = {
                                    val skills =
                                        service.skills.map { StaticDatas.skills[(it.id as Int) - 1] }
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
                            if (userDataStore.value.id != service.worker?.userId.toString() && userDataStore.value.role == "CLIENT") {
                                if (service.type == "APPROVED") {
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
                                        Text(stringResource(id = R.string.layanan_detail_primary_button))
                                    }
                                }
                            } else if (userDataStore.value.id == service.worker?.userId.toString()) {
                                if (service.type.toString() != "ONREVIEW") {
                                    Button(
                                        onClick = {
                                            val bundle = Bundle()
                                            bundle.putInt(
                                                LayananOrderFragment.EXTRA_ID,
                                                service.id!!
                                            )
                                            bundle.putString(
                                                LayananOrderFragment.EXTRA_TYPE,
                                                service.title
                                            )
                                            view.findNavController().navigate(
                                                R.id.action_layananDetailFragment_to_layananOrderFragment,
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
                                        Text(stringResource(id = R.string.layanan_detail_primary_button_alter))
                                    }
                                }
                                OutlinedButton(
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(
                                            LayananBuatFragment.EXTRA_ID,
                                            service.id.toString()
                                        )
                                        view.findNavController().navigate(
                                            R.id.action_layananDetailFragment_to_layananBuatFragment,
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
                                        .padding()
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(stringResource(id = R.string.layanan_detail_outlined_button_alter))
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
        LayananDetailApp(
            "1",
            LayananDetailViewModel(Injection.provideLayananRepostory(context)),
            View(context)
        )
    }
}
