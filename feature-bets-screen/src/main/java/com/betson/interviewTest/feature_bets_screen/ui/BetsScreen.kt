package com.betson.interviewTest.feature_bets_screen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.betson.interviewTest.core_model.common.Bet
import com.betson.interviewTest.core_ui.component.LifecycleEffect
import com.betson.interviewTest.core_ui.component.LoadingIndicator
import com.betson.interviewTest.core_resources.R


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BetsScreen() {
    val viewModel = hiltViewModel<BetsViewModel>()
    val state = viewModel.viewState
    LifecycleEffect(
        lifecycleOwner = LocalLifecycleOwner.current,
        lifecycleEvent = Lifecycle.Event.ON_RESUME
    ) {
        viewModel.setEvent(Event.GetInitBets)
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.surfaceTint
            ), title = { Text(text = stringResource(id = R.string.app_name)) })
    }) { paddingValues ->

        if (state.value.isLoading) {
            LoadingIndicator()
        }
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            state.value.bets?.size?.let { list ->
                items(list) { item ->
                    state.value.bets?.get(item).let { bet ->
                        bet?.let {
                            SingleListItem(item = it)
                        }
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
fun SingleListItem(item: Bet) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column {
            item.image.let {
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
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                )
            }

        }
        Column {
            Text(text = item.type)
            Text(text = item.sellIn.toString())
            Text(text = item.odds.toString())
        }
    }
}

