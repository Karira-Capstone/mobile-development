package com.capstone.karira.activity.layanan

import android.app.ListActivity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.capstone.karira.R
import com.capstone.karira.component.compose.HighlightCard
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.LayananCarousel
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.ActivityLayananMainBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.User
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananMainViewModel
import com.dicoding.jetreward.ui.common.UiState

class LayananMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananMainBinding

    val layananMainViewModel: LayananMainViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleBinding()
    }

    fun handleBinding() {

        binding.mainSection.setContent {
            KariraTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LayananMainApp(layananMainViewModel)
                }
            }
        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LayananMainApp(layananMainViewModel: LayananMainViewModel) {

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
                                                val intent = Intent(context, LayananSearchActivity::class.java)
                                                context.startActivity(intent)
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
                                    HorizontalPager(pageCount = 3, modifier = Modifier) { page ->
                                        user.value.let {
                                            if (data.isNotEmpty()) {
                                                LayananCarousel(data, it)
                                            }
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
    LayananMainApp(layananMainViewModel = LayananMainViewModel(Injection.provideAuthRepostory(context)))
}