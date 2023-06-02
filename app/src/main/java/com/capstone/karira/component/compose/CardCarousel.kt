package com.capstone.karira.component.compose

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.activity.layanan.LayananDetailActivity
import com.capstone.karira.activity.layanan.LayananDetailFragment
import com.capstone.karira.model.Service
import com.capstone.karira.model.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LayananCarousel(data: List<Service>, user: User, onClick: (String) -> Unit) {
    val pageCount = 3
    val pagerState = rememberPagerState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
    ) {
        HorizontalPager(pageCount = pageCount, state = pagerState, modifier = Modifier) {
            for (service in data) {
                HighlightCard(
                    image = service.images,
                    title = service.title,
                    subtitle = user.email,
                    price = service.price,
                    onClick = {
                        onClick(service.id.toString())
                    })
            }
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
}