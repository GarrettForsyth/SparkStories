package com.example.android.sparkstories.di.sparkservicestoriesmodules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.sparkstories.api.ApiEmptyResponse
import com.example.android.sparkstories.api.ApiResponse
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.cue.Cue
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import javax.inject.Singleton

@Module
class AndroidTestSparkServiceModule {

    @Singleton
    @Provides
    fun provideSparkStoriesService(): SparkStoriesService {
        val sparkStoriesService: SparkStoriesService = mockk(relaxed = true)

        val userExistence = MutableLiveData<Resource<Boolean>>()
        userExistence.postValue(Resource.success(true))
        every { sparkStoriesService.checkUserExists() } returns userExistence

        val userHasScreenNameResponse = MutableLiveData<Resource<Boolean>>()
        userHasScreenNameResponse.postValue(Resource.success(true))
        every { sparkStoriesService.checkUserHasScreenName() } returns userHasScreenNameResponse

        val serverCues = MutableLiveData<ApiResponse<List<Cue>>>()
        serverCues.value = ApiEmptyResponse<List<Cue>>()
        every { sparkStoriesService.getCues(any()) } returns serverCues

        return sparkStoriesService
    }

}
