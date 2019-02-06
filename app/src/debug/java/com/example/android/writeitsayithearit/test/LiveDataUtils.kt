package com.example.android.writeitsayithearit.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 * Room calculates the value wrapped in LiveData lazily
 * only when there is an observer.
 *
 * This extension function is therefore useful for testing.
 */
@Throws(InterruptedException::class)
fun <T> LiveData<T>.getValueBlocking(): T {
    var value: T? = null
    val latch = CountDownLatch(1)

    val observer = Observer<T> { t ->
        value = t
        latch.countDown()
    }

    observeForever(observer)

    latch.await(2, TimeUnit.SECONDS)
    return value!!
}
