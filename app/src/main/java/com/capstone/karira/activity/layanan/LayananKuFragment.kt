package com.capstone.karira.activity.layanan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
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
import com.capstone.karira.databinding.FragmentLayananDetailBinding
import com.capstone.karira.databinding.FragmentLayananKuBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.User
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananKuViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananKuFragment : Fragment() {

    private var _binding: FragmentLayananKuBinding? = null
    private val binding get() = _binding!!
    val layananKuViewModel: LayananKuViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLayananKuBinding.inflate(inflater, container, false)
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
                    LayananKuApp(layananKuViewModel, view)
                }
            }
        }

    }

}


@Composable
private fun LayananKuApp(layananKuViewModel: LayananKuViewModel, view: View) {

    val context = LocalContext.current
    val user = layananKuViewModel.user.collectAsState(initial = User("", "ssss"))

    layananKuViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    layananKuViewModel.getUser()
                }
                is UiState.Success -> {
                    val data = uiState.data
                    val listState = rememberLazyListState()
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item {
                            Column(modifier = Modifier) {
                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                                    TitleSection(
                                        title = stringResource(id = R.string.layanan_title, "", "ku"),
                                        subtitle = stringResource(
                                            id = R.string.layanan_titlesub_ku
                                        )
                                    )
                                    Row(
                                        modifier = Modifier,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Button(
                                            onClick = {
                                                view.findNavController().navigate(R.id.action_layananKuFragment_to_layananBuatFragment)
                                            },
                                            shape = RoundedCornerShape(16),
                                            colors = ButtonDefaults.buttonColors(
                                                contentColor = Color.White,
                                                containerColor = colorResource(R.color.purple_500)
                                            ),
                                            modifier = Modifier
                                                .padding(start = 4.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(stringResource(id = R.string.layanan_buat_button))
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
                                        view.findNavController().navigate(R.id.action_layananKuFragment_to_layananDetailFragment, bundle)
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
    LayananKuApp(layananKuViewModel = LayananKuViewModel(Injection.provideAuthRepostory(context)), View(context))
}