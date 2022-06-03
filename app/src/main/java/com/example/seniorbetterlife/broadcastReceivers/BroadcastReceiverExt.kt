package com.example.seniorbetterlife.broadcastReceivers

import android.content.BroadcastReceiver
import kotlinx.coroutines.*

fun BroadcastReceiver.goAsync(
    coroutineScope: CoroutineScope = GlobalScope,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> Unit
) {
    val result = goAsync()
    coroutineScope.launch {
        try {
            block()
        } finally {
            // Always call finish(), even if the coroutineScope was cancelled
            result.finish()
        }
    }
}