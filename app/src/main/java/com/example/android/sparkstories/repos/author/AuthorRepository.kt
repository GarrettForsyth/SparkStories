package com.example.android.sparkstories.repos.author

import androidx.lifecycle.LiveData
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.data.local.AuthorDao
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.model.Resource
import javax.inject.Inject

class AuthorRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val authorDao: AuthorDao,
    private val sparkStoriesService: SparkStoriesService
) {

    fun checkUserExists(): LiveData<Resource<Boolean>> = sparkStoriesService.checkUserExists()

    fun checkUserHasScreenName(): LiveData<Resource<Boolean>> = sparkStoriesService.checkUserHasScreenName()

    fun submitScreenName(screenName: String): LiveData<Resource<Unit>> = sparkStoriesService.submitScreenName(screenName)

    fun checkScreenNameAvailable(screenName: String): LiveData<Resource<Pair<Boolean, String>>> {
        return sparkStoriesService.checkScreenNameAvailable(screenName)
    }
}
