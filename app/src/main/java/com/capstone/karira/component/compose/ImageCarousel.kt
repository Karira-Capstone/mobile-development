package com.capstone.karira.component.compose

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.capstone.karira.R
import com.capstone.karira.model.ImageUrl
import com.capstone.karira.model.Images

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(images: Images) {
    var pageCount = 0
    val pagerState = rememberPagerState()
    var imageList = mutableListOf<String>()

    if (images.foto1.toString() != "null") {
        pageCount++
        imageList.add(images.foto1.toString())
    }
    if (images.foto2.toString() != "null") {
        pageCount++
        imageList.add(images.foto2.toString())
    }
    if (images.foto3.toString() != "null") {
        pageCount++
        imageList.add(images.foto3.toString())
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = colorResource(id = R.color.gray_200),
                shape = RoundedCornerShape(12.dp),
            )
    ) {
        HorizontalPager(pageCount = pageCount, state = pagerState, modifier = Modifier) { page ->
            AsyncImage(
                model = imageList[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
        }
        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarouselUri(images: List<ImageUrl>, handleImage: (Int) -> Unit) {
    val pageCount = images.size
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = colorResource(id = R.color.gray_200),
                shape = RoundedCornerShape(12.dp),
            )
    ) {
        HorizontalPager(pageCount = pageCount, state = pagerState, modifier = Modifier) { page ->
            if (images.isNotEmpty()) {
                AsyncImage(
                    model = images[page].url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            }
        }
        FilledTonalIconButton(
            onClick = { handleImage(pagerState.currentPage) },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopEnd)
                .size(32.dp)
        ) {
            Icon(Icons.Outlined.Close, contentDescription = "Close", modifier = Modifier.size(24.dp))
        }
        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
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
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val images = Images(
        "https://wallpaperaccess.com/full/7889539.png",
        "https://wallpaperaccess.com/full/7889584.jpg",
        "https://wallpaperaccess.com/full/7889635.jpg"
    )

    ImageCarousel(images)
}