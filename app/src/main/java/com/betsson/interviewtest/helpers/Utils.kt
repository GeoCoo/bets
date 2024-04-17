package com.betsson.interviewtest.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.betsson.interviewtest.model.Bet

/**
 * Execute an effect based on [Lifecycle.Event]
 *
 *  @param lifecycleOwner The lifecycle owner
 *  @param lifecycleEvent The lifecycle event that code needs to be executed
 */
@Composable
fun LifecycleEffect(
    lifecycleOwner: LifecycleOwner, lifecycleEvent: Lifecycle.Event, block: () -> Unit,
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                block()
            }
        }
        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

private fun getItemsFromNetwork(): List<Bet> {
    return listOf(
        Bet("Winning team", 10, 20, "https://i.imgur.com/mx66SBD.jpeg"),
        Bet("Total score", 2, 0, "https://i.imgur.com/VnPRqcv.jpeg"),
        Bet("Player performance", 5, 7, "https://i.imgur.com/Urpc00H.jpeg"),
        Bet("First goal scorer", 0, 80, "https://i.imgur.com/Wy94Tt7.jpeg"),
        Bet("Number of fouls", 5, 49, "https://i.imgur.com/NMLpcKj.jpeg"),
        Bet("Corner kicks", 3, 6, "https://i.imgur.com/TiJ8y5l.jpeg")
    )
}
