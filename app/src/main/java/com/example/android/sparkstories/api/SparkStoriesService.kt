package com.example.android.sparkstories.api

import androidx.lifecycle.LiveData
import com.example.android.sparkstories.api.ApiResponse
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.story.Story
import com.example.android.sparkstories.ui.util.QueryParameters


interface SparkStoriesService {

    fun checkUserExists(): LiveData<Resource<Boolean>>

    fun checkUserHasScreenName(): LiveData<Resource<Boolean>>

    fun submitScreenName(screenName: String): LiveData<Resource<Unit>>

    fun checkScreenNameAvailable(screenName: String): LiveData<Resource<Pair<Boolean, String>>>

    fun getCues(queryParameters: QueryParameters): LiveData<ApiResponse<List<Cue>>>

    fun getCue(cueId: String): LiveData<ApiResponse<Cue>>

    fun submitCue(cue: Cue): LiveData<Resource<Boolean>>

    fun getStories(queryParameters: QueryParameters): LiveData<ApiResponse<List<Story>>>


}