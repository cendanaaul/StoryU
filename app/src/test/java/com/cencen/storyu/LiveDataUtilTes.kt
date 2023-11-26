package com.cencen.storyu

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    times: Long = 2,
    timeUnits: TimeUnit = TimeUnit.SECONDS,
    afterObserver: () -> Unit = {}
): T {
    var dataTest: T? = null
    val latchTest = CountDownLatch(1)
    val observerTest = object : Observer<T> {
        override fun onChanged(value: T) {
            dataTest = value
            latchTest.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observerTest)

    try {
        afterObserver.invoke()
        if (!latchTest.await(times, timeUnits)) {
            throw TimeoutException("LiveData val is never set")
        }
    } finally {
        this.removeObserver(observerTest)
    }

    @Suppress("UNCHECKED_CAST")
    return dataTest as T
}

suspend fun <T> LiveData<T>.observeForTesting(part: suspend () -> Unit) {
    val observer = Observer<T> { }
    try {
        observeForever(observer)
        part()
    } finally {
        removeObserver(observer)
    }
}