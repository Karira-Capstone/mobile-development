package com.capstone.karira.activity.dashboard.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.activity.layanan.LayananDetailFragment
import com.capstone.karira.activity.layanan.LayananMainFragment
import com.capstone.karira.activity.proyek.ProyekDetailFragment
import com.capstone.karira.activity.proyek.ProyekMainFragment
import com.capstone.karira.component.compose.HighlightCard
import com.capstone.karira.databinding.FragmentHomeBinding
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.dashboard.home.HomeViewModel
import com.capstone.karira.viewmodel.layanan.LayananDetailViewModel
import com.dicoding.jetreward.ui.common.UiState

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val homeViewModel: HomeViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBinding(view)
        handleOnClick(view)

    }

    private fun handleBinding(view: View) {
        binding.recommedationSection.setContent {
            KariraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecommendationSection(homeViewModel, view)
                }
            }
        }
    }

    private fun handleOnClick(view: View) {
        // Main Button
        binding.buttonSearchProject.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_proyekSearchFragment)
        }
        binding.buttonSearchService.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_layananSearchFragment)
        }

        // Proyek Button
        binding.proyek3d.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 1)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekGame.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 2)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekGraphic.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 3)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekIt.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 4)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekMobile.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 5)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekLainnya.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 6)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekProgramming.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 7)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekProperty.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 8)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekUi.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 9)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }
        binding.proyekWeb.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ProyekMainFragment.EXTRA_ID, 10)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_proyekMainFragment, bundle)
        }

        // Layanan Button
        binding.layanan3d.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 1)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananGame.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 2)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananGraphic.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 3)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananIt.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 4)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananMobile.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 5)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananLainnya.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 6)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananProgramming.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 7)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananProperty.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 8)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananUi.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 9)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
        binding.layananWeb.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(LayananMainFragment.EXTRA_ID, 10)
            view?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_layananMainFragment, bundle)
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecommendationSection(homeViewModel: HomeViewModel, view: View) {

    val context = LocalContext.current
    val userDataStore =
        homeViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    if (userDataStore.value.role == "WORKER") {
        homeViewModel.uiStateProject.collectAsState().value.let { uiState ->
            when (uiState) {
                is UiState.Initiate -> {
                    homeViewModel.getUserProjectRecommendation(userDataStore.value.token)
                }

                is UiState.Error -> {
                    Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                }

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
                    var data = uiState.data as List<Project>
                    if (data.size > 3) data = data.subList(0, 3)

                    if (data.size > 0) {
                        val pageCount = data.size
                        val pagerState = rememberPagerState()

                        Box(
                            modifier = Modifier
                        ) {
                            HorizontalPager(pageCount = pageCount, state = pagerState, modifier = Modifier) {
                                HighlightCard(
                                    image = data[it].client?.user?.picture.toString(),
                                    title = data[it].title.toString(),
                                    subtitle = data[it].client?.user?.fullName.toString(),
                                    price = "Rp${createDotInNumber(data[it].lowerBound.toString())} - Rp${
                                        createDotInNumber(
                                            data[it].upperBound.toString()
                                        )
                                    }",
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(
                                            ProyekDetailFragment.EXTRA_ID,
                                            data[it].id.toString()
                                        )
                                        view.findNavController().navigate(
                                            R.id.action_homeFragment_to_proyekDetailFragment,
                                            bundle
                                        )
                                    }
                                )
                            }
                            Row(
                                Modifier
                                    .height(24.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(pageCount) { iteration ->
                                    val color =
                                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .size(8.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.rekomendasi_empty),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 48.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    } else if (userDataStore.value.role == "CLIENT") {
        homeViewModel.uiStateService.collectAsState().value.let { uiState ->
            when (uiState) {
                is UiState.Initiate -> {
                    homeViewModel.getUserServiceRecommendation(userDataStore.value.token)
                }

                is UiState.Error -> {
                    Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                }

                is UiState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.height(48.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    var data = uiState.data as List<Service>
                    if (data.size > 3) data = data.subList(0, 3)

                    if (data.size > 0) {
                        val pageCount = data.size
                        val pagerState = rememberPagerState()

                        Box(
                            modifier = Modifier
                        ) {
                            HorizontalPager(pageCount = pageCount, state = pagerState, modifier = Modifier) {
                                HighlightCard(
                                    image = data[it].worker?.user?.picture.toString(),
                                    title = data[it].title.toString(),
                                    subtitle = data[it].worker?.user?.fullName.toString(),
                                    price = "Rp${createDotInNumber(data[it].price.toString())}",
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(
                                            LayananDetailFragment.EXTRA_ID,
                                            data[it].id.toString()
                                        )
                                        view.findNavController().navigate(
                                            R.id.action_homeFragment_to_layananDetailFragment,
                                            bundle
                                        )
                                    }
                                )
                            }
                            Row(
                                Modifier
                                    .height(24.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(pageCount) { iteration ->
                                    val color =
                                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .size(8.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.rekomendasi_empty),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 48.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

}