package com.capstone.karira.activity.layanan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.LayananCarousel
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.FragmentLayananKuBinding
import com.capstone.karira.databinding.FragmentLayananMainBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.User
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananMainViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananMainFragment : Fragment() {

    private var _binding: FragmentLayananMainBinding? = null
    private val binding get() = _binding!!
    val layananMainViewModel: LayananMainViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

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
                    LayananMainApp(layananMainViewModel, view)
                }
            }
        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LayananMainApp(layananMainViewModel: LayananMainViewModel, view: View) {

    val context = LocalContext.current
    val user = layananMainViewModel.user.collectAsState(initial = User("", "ssss"))

    layananMainViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    layananMainViewModel.getUser()
                }
                is UiState.Success -> {
                    val data = uiState.data
                    val listState = rememberLazyListState()
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item {
                            Column(modifier = Modifier) {
                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                                    TitleSection(
                                        title = stringResource(id = R.string.layanan_title, "", ""),
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
                                                .padding()
                                                .fillMaxWidth()
                                        ) {
                                            Text(stringResource(id = R.string.layanan_cari_button))
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
                        item {
                            Column {
                                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                                    Text(text = stringResource(id = R.string.layanan_recommend_title), fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 24.dp))
                                    user.value.let {
                                        if (data.isNotEmpty()) {
                                            LayananCarousel(data.subList(0, 3), it, onClick = { id ->
                                                val bundle = Bundle()
                                                bundle.putString(LayananDetailFragment.EXTRA_ID, id)
                                                view.findNavController().navigate(R.id.action_layananMainFragment_to_layananDetailFragment, bundle)
                                            })
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
                        items(data, key = { it.id }) { service ->
                            user.value.let {
                                ItemCard(
                                    image = service.images,
                                    title = service.title,
                                    subtitle = it.email,
                                    price = service.price,
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(LayananDetailFragment.EXTRA_ID, service.id.toString())
                                        view.findNavController().navigate(R.id.action_layananMainFragment_to_layananDetailFragment, bundle)
                                    })
                            }
                        }
                    }
                }

                is UiState.Error -> {}
            }
        }
}

@Preview(showBackground = true)
@Composable
private fun Preview(){
    val context = LocalContext.current
    LayananMainApp(layananMainViewModel = LayananMainViewModel(Injection.provideAuthRepostory(context)), View(context))
}