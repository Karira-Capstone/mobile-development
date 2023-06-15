package com.capstone.karira.activity.layanan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.activity.order.OrderDetailFragment
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.FragmentLayananKuBinding
import com.capstone.karira.databinding.FragmentLayananOrderBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Order
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananKuViewModel
import com.capstone.karira.viewmodel.layanan.LayananOrderViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananOrderFragment : Fragment() {

    private var id: String = "-1"
    private var type: String = "USER"
    private var _binding: FragmentLayananOrderBinding? = null
    private val binding get() = _binding!!
    val layananOrderViewModel: LayananOrderViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            id = it.getInt(EXTRA_ID, 0).toString()
            type = it.getString(EXTRA_TYPE, "USER")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLayananOrderBinding.inflate(inflater, container, false)
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
                    LayananOrderApp(id, type, layananOrderViewModel, view)
                }
            }
        }

    }

    companion object {
        const val EXTRA_TYPE = "kinlian_type" // Isinya bakal nama service
        const val EXTRA_ID = "kinlian_id"    // id service
    }

}


@Composable
private fun LayananOrderApp(
    id: String,
    type: String,
    layananOrderViewModel: LayananOrderViewModel,
    view: View
) {

    val context = LocalContext.current
    val userDataStore =
        layananOrderViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    if (userDataStore.value.role == "WORKER") {
        layananOrderViewModel.uiState
            .collectAsState(initial = UiState.Loading).value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        if (type == "USER") layananOrderViewModel.getOrderByFreelance(userDataStore.value.token)
                        else layananOrderViewModel.getOrderByService(id)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        val data = uiState.data as List<Order>
                        val listState = rememberLazyListState()

                        if (type != "USER") {
                            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                                item {
                                    Column(modifier = Modifier) {
                                        Column(
                                            modifier = Modifier.padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 48.dp,
                                                bottom = 24.dp
                                            )
                                        ) {
                                            TitleSection(
                                                title = stringResource(
                                                    id = R.string.layanan_title,
                                                    "Pesanan ",
                                                    " $type"
                                                ),
                                                subtitle = stringResource(
                                                    id = R.string.layanan_order_subtitle, data.size, " ini"
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
                                    items(data, key = { it.id.toString() }) { order ->
                                        ItemCard(
                                            image = order.client?.user?.picture.toString(),
                                            title = order.client?.user?.fullName.toString(),
                                            subtitle = order.status.toString(),
                                            price = createDotInNumber(order.price.toString()),
                                            onClick = {
                                                val bundle = Bundle()
                                                bundle.putString(
                                                    OrderDetailFragment.EXTRA_ID,
                                                    order.id.toString()
                                                )
                                                view.findNavController().navigate(
                                                    R.id.action_layananOrderFragment_to_orderDetailFragment,
                                                    bundle
                                                )
                                            })
                                    }
                                } else {
                                    item {
                                        Text(
                                            text = stringResource(id = R.string.layanan_order_notfound),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = 24.dp,
                                                    vertical = 96.dp
                                                )
                                                .fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        } else {
                            var tabIndex = remember { mutableStateOf(0) }
                            val tabs = listOf("Proyek", "Layanan")

                            val bidData = data.filter { it.type == "BID" }
                            val serviceData = data.filter { it.type == "SERVICE" }

                            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                                item {
                                    Column(modifier = Modifier) {
                                        Column(
                                            modifier = Modifier.padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 48.dp,
                                                bottom = 24.dp
                                            )
                                        ) {
                                            TitleSection(
                                                title = stringResource(
                                                    id = R.string.layanan_order_title,
                                                    "Pesanan ",
                                                    "ku"
                                                ),
                                                subtitle = stringResource(
                                                    id = R.string.layanan_order_main_subtitle,
                                                    data.size,
                                                    "mu"
                                                )
                                            )
                                        }
                                        TabRow(selectedTabIndex = tabIndex.value) {
                                            tabs.forEachIndexed { index, title ->
                                                Tab(text = { Text(title) },
                                                    selected = tabIndex.value == index,
                                                    onClick = { tabIndex.value = index }
                                                )
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
                                if ((tabIndex.value == 0 && bidData.size > 0) || (tabIndex.value == 1 && serviceData.size > 0)) {
                                    items(if (tabIndex.value == 0) bidData else serviceData , key = { it.id.toString() }) { order ->
                                        ItemCard(
                                            image = order.client?.user?.picture.toString(),
                                            title = if (order.projectId != null) order.project?.title.toString() else order.service?.title.toString(),
                                            thinTitle = order.client?.user?.fullName.toString(),
                                            subtitle = order.status.toString(),
                                            price = createDotInNumber(order.price.toString()),
                                            onClick = {
                                                val bundle = Bundle()
                                                bundle.putString(
                                                    OrderDetailFragment.EXTRA_ID,
                                                    order.id.toString()
                                                )
                                                view.findNavController().navigate(
                                                    R.id.action_layananOrderFragment_to_orderDetailFragment,
                                                    bundle
                                                )
                                            })
                                    }
                                } else {
                                    item {
                                        Text(
                                            text = stringResource(id = R.string.layanan_order_notfound),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = 24.dp,
                                                    vertical = 96.dp
                                                )
                                                .fillMaxWidth()
                                        )
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
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val context = LocalContext.current
    LayananOrderApp(
        "1", "USER",
        layananOrderViewModel = LayananOrderViewModel(Injection.provideLayananRepostory(context)),
        View(context)
    )
}