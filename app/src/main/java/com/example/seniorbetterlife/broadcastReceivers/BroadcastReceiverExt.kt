package com.example.seniorbetterlife.broadcastReceivers

import android.content.BroadcastReceiver
import kotlinx.coroutines.*

fun BroadcastReceiver.goAsync(
    coroutineScope: CoroutineScope = GlobalScope,
    block: suspend () -> Unit
) {
    val result = goAsync()
    coroutineScope.launch {
        try {
            block()
        } finally {
            result.finish()
        }
    }
}