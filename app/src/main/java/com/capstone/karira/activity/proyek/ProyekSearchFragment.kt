package com.capstone.karira.activity.proyek

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.FragmentLayananSearchBinding
import com.capstone.karira.databinding.FragmentProyekSearchBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.utils.createDotInNumber
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananSearchViewModel
import com.capstone.karira.viewmodel.proyek.ProyekSearchViewModel
import com.dicoding.jetreward.ui.common.UiState

class ProyekSearchFragment : Fragment() {

    private var _binding: FragmentProyekSearchBinding? = null
    private val binding get() = _binding!!
    val proyekSearchViewModel: ProyekSearchViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProyekSearchBinding.inflate(inflater, container, false)
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
                    ProyekSearchApp(proyekSearchViewModel, view)
                }
            }
        }

    }
}


@Composable
private fun ProyekSearchApp(proyekSearchViewModel: ProyekSearchViewModel, view: View) {

    val context = LocalContext.current
    val query by proyekSearchViewModel.query
    val userDataStore = proyekSearchViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    proyekSearchViewModel.uiState
        .collectAsState(initial = UiState.Initiate).value.let { uiState ->
            when (uiState) {
                is UiState.Initiate -> {
                    Column(modifier = Modifier) {
                        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                            TitleSection(
                                title = stringResource(id = R.string.proyek_title, "Cari ", ""),
                                subtitle = stringResource(
                                    id = R.string.proyek_titlesub_search
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                BasicTextField(
                                    value = query,
                                    onValueChange = { newText: String -> proyekSearchViewModel.changeQuery(newText) },
                                    textStyle = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    ),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    width = 1.dp,
                                                    color = colorResource(id = R.color.gray_200),
                                                    shape = RoundedCornerShape(size = 6.dp)
                                                )
                                                .padding(
                                                    horizontal = 16.dp,
                                                    vertical = 8.dp
                                                ), // inner padding
                                        ) {
                                            if (query.isEmpty()) {
                                                Text(
                                                    text = stringResource(id = R.string.proyek_input_hint),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = colorResource(id = R.color.blackAlpha_300)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .height(40.dp)
                                        .weight(1f)
                                )
                                FilledIconButton(onClick = {
                                    proyekSearchViewModel.search()
                                    proyekSearchViewModel.changeQuery("")
                                }, shape = RoundedCornerShape(4.dp), modifier = Modifier
                                    .height(40.dp)
                                    .aspectRatio(1f / 1f)) {
                                    Icon(painter = painterResource(id = R.drawable.ic_search_white), contentDescription = null)
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
                is UiState.Loading -> {
                    Column(modifier = Modifier) {
                        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                            TitleSection(
                                title = stringResource(id = R.string.proyek_title, "Mencari ", ""),
                                subtitle = stringResource(
                                    id = R.string.proyek_titlesub_search
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                BasicTextField(
                                    value = query,
                                    onValueChange = { newText: String -> proyekSearchViewModel.changeQuery(newText) },
                                    textStyle = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    ),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    width = 1.dp,
                                                    color = colorResource(id = R.color.gray_200),
                                                    shape = RoundedCornerShape(size = 6.dp)
                                                )
                                                .padding(
                                                    horizontal = 16.dp,
                                                    vertical = 8.dp
                                                ), // inner padding
                                        ) {
                                            if (query.isEmpty()) {
                                                Text(
                                                    text = stringResource(id = R.string.layanan_input_hint),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = colorResource(id = R.color.blackAlpha_300)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .height(40.dp)
                                        .weight(1f)
                                )
                                FilledIconButton(onClick = {
                                    proyekSearchViewModel.search()
                                    proyekSearchViewModel.changeQuery("")
                                }, shape = RoundedCornerShape(4.dp), modifier = Modifier
                                    .height(40.dp)
                                    .aspectRatio(1f / 1f)) {
                                    Icon(painter = painterResource(id = R.drawable.ic_search_white), contentDescription = null)
                                }
                            }
                        }
                        Divider(
                            color = colorResource(R.color.gray_200),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxHeight().fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                        }
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
                                        title = stringResource(id = R.string.proyek_title, "Mencari ", ""),
                                        subtitle = stringResource(
                                            id = R.string.proyek_titlesub_search
                                        )
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        BasicTextField(
                                            value = query,
                                            onValueChange = { newText: String -> proyekSearchViewModel.changeQuery(newText) },
                                            textStyle = TextStyle(
                                                fontSize = 16.sp,
                                                color = Color.Black
                                            ),
                                            decorationBox = { innerTextField ->
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .border(
                                                            width = 1.dp,
                                                            color = colorResource(id = R.color.gray_200),
                                                            shape = RoundedCornerShape(size = 6.dp)
                                                        )
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        ), // inner padding
                                                ) {
                                                    if (query.isEmpty()) {
                                                        Text(
                                                            text = stringResource(id = R.string.proyek_input_hint),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Normal,
                                                            color = colorResource(id = R.color.blackAlpha_300)
                                                        )
                                                    }
                                                    innerTextField()
                                                }
                                            },
                                            modifier = Modifier
                                                .padding(end = 16.dp)
                                                .height(40.dp)
                                                .weight(1f)
                                        )
                                        FilledIconButton(onClick = {
                                            proyekSearchViewModel.search()
                                            proyekSearchViewModel.changeQuery("")
                                        }, shape = RoundedCornerShape(4.dp), modifier = Modifier
                                            .height(40.dp)
                                            .aspectRatio(1f / 1f)) {
                                            Icon(painter = painterResource(id = R.drawable.ic_search_white), contentDescription = null)
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
                        if (data.size > 0) {
                            items(data, key = { it.id.toString() }) { project ->
                                ItemCard(
                                    image = project.client?.user?.picture.toString(),
                                    title = project.title.toString(),
                                    subtitle = project.client?.user?.fullName.toString(),
                                    price = "${createDotInNumber(project.lowerBound.toString())} - Rp${createDotInNumber(project.upperBound.toString())}",
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(ProyekDetailFragment.EXTRA_ID, project.id.toString())
                                        view.findNavController().navigate(R.id.action_proyekSearchFragment_to_proyekDetailFragment, bundle)
                                    })
                            }
                        } else {
                            item {
                                Text(
                                    text = stringResource(id = R.string.proyek_cari_notfound),
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
            }
        }

}

@Preview(showBackground = true)
@Composable
private fun Preview(){
    val context = LocalContext.current
    ProyekSearchApp(proyekSearchViewModel = ProyekSearchViewModel(Injection.provideProyekRepostory(context)), View(context))
}

