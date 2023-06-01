package com.capstone.karira.activity.layanan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.capstone.karira.databinding.ActivityLayananBuatBinding
import com.capstone.karira.databinding.ActivityLayananDetailBinding
import com.capstone.karira.model.Service
import com.capstone.karira.ui.theme.KariraTheme
import java.util.Date

class LayananDetailActivity : ComponentActivity() {

    private lateinit var binding: ActivityLayananDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleBinding()
    }

    fun handleBinding() {
        val service = Service(
            1,
            "Karira Hadehhhhh",
            5,
            "Accepted",
            "",
            "PT Kerja Keras Sejahtera is a company that tries to make Indonesia a better place to work. We are here to create a better work-life balance lifestyle through mentorship, in-class training, and others.\n" +
                    "\n" +
                    "We currently are looking for an frontend intern to be placed in one of our project with a reputable track record. We are aiming to create a fully usable website that is entertain to our user. The developer will design, develop, and maintain the platform’s customer-facing features to drive engagement and bolster growth. PT Kerja Keras Sejahtera is a company that tries to make Indonesia a better place to work. We are here to create a better work-life balance lifestyle through mentorship, in-class training, and others.\n" +
                    "\n" +
                    "We currently are looking for an frontend intern to be placed in one of our project with a reputable track record. We are aiming to create a fully usable website that is entertain to our user. The developer will design, develop, and maintain the platform’s customer-facing features to drive engagement and bolster growth.",
            "200000",
            last_updated = Date()
        )

        binding.pageSection.setContent {
            KariraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LayananDetailApp(service)
                }
            }
        }
    }

}

@Composable
private fun LayananDetailApp(service: Service, modifier: Modifier = Modifier) {
    val images = listOf(
        "https://wallpaperaccess.com/full/7889539.png",
        "https://wallpaperaccess.com/full/7889584.jpg",
        "https://wallpaperaccess.com/full/7889635.jpg"
    )

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, bottom = 48.dp, top = 48.dp)
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    val service =
        Service(1, "Kotlin App", 5, "Accepted", "", "Description", "200000", last_updated = Date())

    KariraTheme {
        LayananDetailApp(service)
    }
}