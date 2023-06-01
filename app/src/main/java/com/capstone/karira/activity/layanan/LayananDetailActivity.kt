package com.capstone.karira.activity.layanan

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.karira.R
import com.capstone.karira.component.compose.CenterHeadingWithDesc
import com.capstone.karira.component.compose.ImageCarousel
import com.capstone.karira.component.compose.ImageCarouselUri
import com.capstone.karira.component.compose.ItemCard
import com.capstone.karira.component.compose.TitleSection
import com.capstone.karira.databinding.ActivityLayananBuatBinding
import com.capstone.karira.databinding.ActivityLayananDetailBinding
import com.capstone.karira.di.Injection
import com.capstone.karira.model.Service
import com.capstone.karira.model.User
import com.capstone.karira.ui.theme.KariraTheme
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.layanan.LayananDetailViewModel
import com.capstone.karira.viewmodel.layanan.LayananKuViewModel
import com.dicoding.jetreward.ui.common.UiState
import java.util.Date

class LayananDetailActivity : AppCompatActivity() {

    private lateinit var id: String
    private lateinit var binding: ActivityLayananDetailBinding
    val layananDetailViewModel: LayananDetailViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra(EXTRA_ID).toString();

        handleBinding()
    }

    fun handleBinding() {


        binding.pageSection.setContent {
            KariraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LayananDetailApp(id, layananDetailViewModel)
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "kinlian"
    }

}

@Composable
private fun LayananDetailApp(id: String, layananDetailViewModel: LayananDetailViewModel, modifier: Modifier = Modifier) {
    val images = listOf(
        "https://wallpaperaccess.com/full/7889539.png",
        "https://wallpaperaccess.com/full/7889584.jpg",
        "https://wallpaperaccess.com/full/7889635.jpg"
    )

    val context = LocalContext.current
    val user = layananDetailViewModel.user.collectAsState(initial = User("", "ssss"))

    layananDetailViewModel.uiState
        .collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    layananDetailViewModel.getServiceById(id)
                }
                is UiState.Success -> {
                    val service = uiState.data
                    val listState = rememberLazyListState()
                    Column(
                        modifier = modifier
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp, top = 48.dp)
                            .fillMaxWidth()
                    ) {
                        Row(modifier = Modifier) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_karira_logo_purple),
                                contentDescription = "Karira coy",
                                modifier = Modifier
                                    .width(64.dp)
                                    .aspectRatio(1f / 1f)
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    text = service.title,
                                    modifier = modifier,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = service.title,
                                    modifier = modifier,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .height(IntrinsicSize.Min)
                                .padding(vertical = 8.dp)
                        ) {
                            CenterHeadingWithDesc(
                                main = service.price,
                                subtext = if (service.isNegotiable) stringResource(id = R.string.layanan_detail_isnegotiable) else stringResource(
                                    id = R.string.layanan_detail_isnotnegotiable
                                )
                            )
                            Divider(
                                color = colorResource(R.color.gray_200),
                                modifier = Modifier
                                    .height(16.dp) //fill the max height
                                    .width(1.dp)
                            )
                            CenterHeadingWithDesc(
                                main = service.usedBy.toString(),
                                subtext = stringResource(id = R.string.layanan_detail_user)
                            )
                            Divider(
                                color = colorResource(R.color.gray_200),
                                modifier = Modifier
                                    .height(16.dp)  //fill the max height
                                    .width(1.dp)
                            )
                            CenterHeadingWithDesc(
                                main = service.max_duration.toString(),
                                subtext = stringResource(id = R.string.layanan_detail_duration)
                            )
                        }
                        ImageCarousel(images)
                        Text(
                            text = service.description,
                            modifier = modifier.padding(top = 12.dp, bottom = 24.dp)
                        )
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            // ---------------------------------------------------------------
                            // ------------------------- GANTI COK ---------------------------
                            // ---------------------------------------------------------------
                            if (!user.value.isActivated) {
                                Button(
                                    onClick = { /*TODO*/ },
                                    shape = RoundedCornerShape(16),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Color.White,
                                        containerColor = colorResource(R.color.purple_500)
                                    ),
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(stringResource(id = R.string.layanan_detail_primary_button))
                                }
                                OutlinedButton(
                                    onClick = { /*TODO*/ },
                                    shape = RoundedCornerShape(16),
                                    border = BorderStroke(1.dp, colorResource(R.color.purple_500)),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(R.color.purple_500),
                                        containerColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(stringResource(id = R.string.layanan_detail_outlined_button))
                                }
                            } else {
                                Button(
                                    onClick = {
                                        val intent = Intent(context, LayananBuatActivity::class.java)
                                        intent.putExtra(LayananBuatActivity.EXTRA_ID, id)
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(16),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Color.White,
                                        containerColor = colorResource(R.color.purple_500)
                                    ),
                                    modifier = Modifier
                                        .padding()
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(stringResource(id = R.string.layanan_detail_primary_button_alter))
                                }
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
private fun GreetingPreview() {
    val context = LocalContext.current
    KariraTheme {
        LayananDetailApp("1", LayananDetailViewModel(Injection.provideAuthRepostory(context)))
    }
}