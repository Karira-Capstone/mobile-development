package com.capstone.karira.component.compose

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.capstone.karira.R

@Composable
fun ItemCard(image: String, title: String, subtitle: String, price: String, onClick: () -> Unit) {
    Column(modifier = Modifier
        .clickable { onClick() }
        .fillMaxWidth()) {
        Row(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .fillMaxWidth()) {
            AsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(84.dp)
                    .aspectRatio(1f / 1f)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(modifier = Modifier
                .padding(start = 16.dp)
                .defaultMinSize(minHeight = 84.dp)
                .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier) {
                    Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = subtitle, fontSize = 14.sp, color = colorResource(id = R.color.blackAlpha_300))
                }
                Row(modifier = Modifier) {
                    Image(painterResource(id = R.drawable.ic_money_black), contentDescription = null, modifier = Modifier.size(16.dp))
                    Text(text = "Rp$price", fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
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

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ItemCard(image = "https://mediaassets.airbus.com/permalinks/608923/win/lufthansa-orders-10-airbus-a350-1000-and-5-more-a350-900-aircraft.jpg", title = "Hello World", subtitle = "Muhammad Haqqi Al Farizi", price = "200000") {
        
    }
}