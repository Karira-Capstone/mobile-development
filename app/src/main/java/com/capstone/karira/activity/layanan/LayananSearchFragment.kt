package com.capstone.karira.activity.layanan

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
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.FragmentLayananSearchBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananSearchViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananSearchFragment : Fragment() {

    private var _binding: FragmentLayananSearchBinding? = null
    private val binding get() = _binding!!
    val layananSearchViewModel: LayananSearchViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLayananSearchBinding.inflate(inflater, container, false)
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
                    LayananSearchApp(layananSearchViewModel, view)
                }
            }
        }

    }
}


@Composable
private fun LayananSearchApp(layananSearchViewModel: LayananSearchViewModel, view: View) {

    val context = LocalContext.current
    val query by layananSearchViewModel.query
    val userDataStore = layananSearchViewModel.userDataStore.collectAsState(initial = UserDataStore("", "ssss"))

    layananSearchViewModel.uiState
        .collectAsState(initial = UiState.Initiate).value.let { uiState ->
            when (uiState) {
                is UiState.Initiate -> {
                    Column(modifier = Modifier) {
                        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                            TitleSection(
                                title = stringResource(id = R.string.layanan_title, "Cari ", ""),
                                subtitle = stringResource(
                                    id = R.string.layanan_titlesub_search
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                BasicTextField(
                                    value = query,
                                    onValueChange = { newText: String -> layananSearchViewModel.changeQuery(newText) },
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
                                    layananSearchViewModel.search()
                                    layananSearchViewModel.changeQuery("")
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
                                title = stringResource(id = R.string.layanan_title, "Cari ", ""),
                                subtitle = stringResource(
                                    id = R.string.layanan_titlesub_search
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                BasicTextField(
                                    value = query,
                                    onValueChange = { newText: String -> layananSearchViewModel.changeQuery(newText) },
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
                                    layananSearchViewModel.search()
                                    layananSearchViewModel.changeQuery("")
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
                is UiState.Success -> {
                    val data = uiState.data as List<Service>
                    val listState = rememberLazyListState()

                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item {
                            Column(modifier = Modifier) {
                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
                                    TitleSection(
                                        title = stringResource(id = R.string.layanan_cari_found),
                                        subtitle = stringResource(
                                            id = R.string.layanan_titlesub_search
                                        )
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        BasicTextField(
                                            value = query,
                                            onValueChange = { newText: String -> layananSearchViewModel.changeQuery(newText) },
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
                                            layananSearchViewModel.search()
                                            layananSearchViewModel.changeQuery("")
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
                            items(data, key = { it.id.toString() }) { service ->
                                ItemCard(
                                    image = service.worker?.user?.picture.toString(),
                                    title = service.title.toString(),
                                    subtitle = service.worker?.user?.fullName.toString(),
                                    price = service.price.toString(),
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putString(LayananDetailFragment.EXTRA_ID, service.id.toString())
                                        view.findNavController().navigate(R.id.action_layananSearchFragment_to_layananDetailFragment, bundle)
                                    })
                            }
                        } else {
                            item {
                                Text(
                                    text = stringResource(id = R.string.layanan_cari_notfound),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 96.dp)
                                )
                            }
                        }
                    }
                }

                is UiState.Error -> {}
            }
        }


//    val listState = rememberLazyListState()
//
//    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
//        item {
//            Column(modifier = Modifier) {
//                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp)) {
//                    TitleSection(
//                        title = stringResource(id = R.string.layanan_title, "Cari ", ""),
//                        subtitle = stringResource(
//                            id = R.string.layanan_titlesub_search
//                        )
//                    )
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                    ) {
//                        BasicTextField(
//                            value = query,
//                            onValueChange = { newText: String -> layananSearchViewModel.changeQuery(newText) },
//                            textStyle = TextStyle(
//                                fontSize = 16.sp,
//                                color = Color.Black
//                            ),
//                            decorationBox = { innerTextField ->
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .border(
//                                            width = 1.dp,
//                                            color = colorResource(id = R.color.gray_200),
//                                            shape = RoundedCornerShape(size = 6.dp)
//                                        )
//                                        .padding(
//                                            horizontal = 16.dp,
//                                            vertical = 8.dp
//                                        ), // inner padding
//                                ) {
//                                    if (query.isEmpty()) {
//                                        Text(
//                                            text = stringResource(id = R.string.layanan_input_hint),
//                                            fontSize = 16.sp,
//                                            fontWeight = FontWeight.Normal,
//                                            color = colorResource(id = R.color.blackAlpha_300)
//                                        )
//                                    }
//                                    innerTextField()
//                                }
//                            },
//                            modifier = Modifier
//                                .padding(end = 16.dp)
//                                .height(40.dp)
//                                .weight(1f)
//                        )
//                        FilledIconButton(onClick = {
//                            layananSearchViewModel.search()
//                            layananSearchViewModel.changeQuery("")
//                        }, shape = RoundedCornerShape(4.dp), modifier = Modifier
//                            .height(40.dp)
//                            .aspectRatio(1f / 1f)) {
//                            Icon(painter = painterResource(id = R.drawable.ic_search_white), contentDescription = null)
//                        }
//                    }
//                }
//                Divider(
//                    color = colorResource(R.color.gray_200),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(1.dp)
//                )
//            }
//        }
//        item {
//            layananSearchViewModel.uiState
//                .collectAsState(initial = UiState.Loading).value.let { uiState ->
//                    when (uiState) {
//                        is UiState.Loading -> {
//                            layananSearchViewModel.search()
//                        }
//                        is UiState.Success -> {
//                            val data = uiState.data as List<Service>
//
//                            if (data.size > 0) {
//                                items(data, key = { it.id.toString() }) { service ->
//                                    ItemCard(
//                                        image = service.worker?.user?.picture.toString(),
//                                        title = service.title.toString(),
//                                        subtitle = service.worker?.user?.fullName.toString(),
//                                        price = service.price.toString(),
//                                        onClick = {
//                                            val bundle = Bundle()
//                                            bundle.putString(LayananDetailFragment.EXTRA_ID, service.id.toString())
//                                            view.findNavController().navigate(R.id.action_layananSearchFragment_to_layananDetailFragment, bundle)
//                                        })
//                                }
//                            } else {
//                                item {
//                                    Text(
//                                        text = stringResource(id = R.string.layanan_cari_notfound),
//                                        fontSize = 24.sp,
//                                        fontWeight = FontWeight.SemiBold,
//                                        color = Color.Black,
//                                        modifier = Modifier.padding(top = 96.dp)
//                                    )
//                                }
//                            }
//                        }
//
//                        is UiState.Error -> {}
//                    }
//                }
//        }
//
//    }


}

@Preview(showBackground = true)
@Composable
private fun Preview(){
    val context = LocalContext.current
    LayananSearchApp(layananSearchViewModel = LayananSearchViewModel(Injection.provideLayananRepostory(context)), View(context))
}

