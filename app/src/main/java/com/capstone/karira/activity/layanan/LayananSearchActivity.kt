package com.capstone.karira.activity.layanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
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
import com.capstone.karira.R
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.ActivityLayananSearchBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.User
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananKuViewModel
import com.capstone.karira.viewmodel.layanan.LayananSearchViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananSearchBinding
    val layananSearchViewModel: LayananSearchViewModel by viewModels { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleBinding()
    }

    fun handleBinding() {

        binding.mainSection.setContent {
            LayananSearchApp(layananSearchViewModel)
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayananSearchApp(layananSearchViewModel: LayananSearchViewModel) {

    val context = LocalContext.current
    val query by layananSearchViewModel.query
    val user = layananSearchViewModel.user.collectAsState(initial = User("", "ssss"))

    layananSearchViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    layananSearchViewModel.getUser()
                }
                is UiState.Success -> {
                    val data = uiState.data
                    val listState = rememberLazyListState()

                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item {
                            Column(modifier = Modifier) {
                                Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 24.dp)) {
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
                                        FilledIconButton(onClick = { layananSearchViewModel.search() }, shape = RoundedCornerShape(4.dp), modifier = Modifier
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
                        items(data, key = { it.id }) { service ->
                            user.value.let {
                                ItemCard(
                                    image = service.images,
                                    title = service.title,
                                    subtitle = it.email,
                                    price = service.price,
                                    onClick = {
                                        val intent = Intent(context, LayananDetailActivity::class.java)
                                        context.startActivity(intent)
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
    LayananKuApp(layananKuViewModel = LayananKuViewModel(Injection.provideAuthRepostory(context)))
}