package com.devpair.offline.data.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Ticker che emette un segnale ogni N secondi per simulare eventi realtime
 */
object RealtimeTicker {
    private const val TICK_INTERVAL_MS = 3000L // 3 secondi

    fun tickFlow(): Flow<Unit> = flow {
        while (true) {
            delay(TICK_INTERVAL_MS)
            emit(Unit)
        }
    }
}
