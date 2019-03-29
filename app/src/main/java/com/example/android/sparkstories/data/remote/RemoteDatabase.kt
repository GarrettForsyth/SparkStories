package com.example.android.sparkstories.data.remote

import androidx.lifecycle.LiveData
import com.example.android.sparkstories.api.ApiResponse
import com.example.android.sparkstories.model.Resource


interface RemoteDatabase {

    fun checkUserExists(): LiveData<Resource<Boolean>>

    fun submitScreenName(screenName: String): LiveData<Resource<Unit>>

    fun checkScreenNameAvailable(screenName: String): LiveData<Resource<Pair<Boolean, String>>>

}