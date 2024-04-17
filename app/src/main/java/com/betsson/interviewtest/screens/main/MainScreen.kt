package com.betsson.interviewtest.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.material.PlaceholderState
import androidx.wear.compose.material.placeholder
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.betsson.interviewtest.R
import com.betsson.interviewtest.helpers.LifecycleEffect
import com.betsson.interviewtest.helpers.LoadingIndicator
import com.betsson.interviewtest.model.Bet

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val viewModel = hiltViewModel<MainViewModel>()
    val state = viewModel.viewState
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LifecycleEffect(
        lifecycleOwner = LocalLifecycleOwner.current,
        lifecycleEvent = Lifecycle.Event.ON_RESUME
    ) {
        viewModel.setEvent(Event.GetInitBets)
    }

    Scaffold {
        if (state.value.isLoading) {
            LoadingIndicator()
        }
        LazyColumn {
            state.value.bets?.size?.let { list ->
                items(list) { item ->
                    state.value.bets?.get(item).let { bet ->
                        SingleListItem(item = bet)

                    }
                }
                item {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        viewModel.setEvent(Event.UpdateOdds(state.value.bets))
                    }) {
                        Text(text = "Update Odds")
                    }
                }
            }
        }
    }
}


@Composable
fun SingleListItem(item: Bet?) {

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column {
            item?.image?.let {
                val builder =
                    ImageRequest.Builder(LocalContext.current).data(it).memoryCacheKey(it)
                        .diskCacheKey(it).diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .placeholder(R.drawable.ic_launcher_foreground)


                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    painter = rememberAsyncImagePainter(remember(it) {
                        builder.build()
                    }),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                )
            }

        }
        Column {
            item?.type?.let { Text(text = it) }
            item?.sellIn?.let { Text(text = it.toString()) }
            item?.odds?.let { Text(text = it.toString()) }

        }

    }
}