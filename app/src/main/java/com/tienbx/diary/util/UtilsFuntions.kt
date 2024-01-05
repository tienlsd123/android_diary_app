package com.tienbx.diary.util

import io.realm.kotlin.types.RealmInstant
import java.time.Instant

fun RealmInstant.toInstance(): Instant {
    val sec = this.epochSeconds
    val nano = this.nanosecondsOfSecond
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}
