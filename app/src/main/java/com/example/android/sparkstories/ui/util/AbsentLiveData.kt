package com.example.android.sparkstories.ui.util

import androidx.lifecycle.LiveData

/**
 * Used to post an empty result.
 */
class AbsentLiveData<T : Any?> private constructor() : LiveData<T>() {
    init {
        postValue(null)
    }
    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}