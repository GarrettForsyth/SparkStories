package com.example.android.writeitsayithearit.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.mockk.mockk

object MockUtils {

    /**
     * Sets a mock to observe LiveData
     */
    fun <T> mockObserver(liveData: LiveData<T>) {
        val mockObserver: Observer<T> = mockk()
        liveData.observeForever(mockObserver)
    }

    fun <T> mockObservers(liveDataList: List<LiveData<T>>) {
        liveDataList.forEach { liveData -> mockObserver(liveData) }
    }

}