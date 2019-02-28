package com.example.android.writeitsayithearit.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.mockk.mockk

object MockUtils {

    /**
     * Sets a mock to observe LiveData
     */
    private fun <T> mockObserverFor(liveData: LiveData<T>) {
        val mockObserver: Observer<T> = mockk(relaxed = true)
        liveData.observeForever(mockObserver)
    }

    fun <T: Any> mockObserversFor(vararg liveDataList : LiveData<T>) {
        liveDataList.forEach { liveData -> mockObserverFor(liveData) }
    }

}