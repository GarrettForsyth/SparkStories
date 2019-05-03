package com.example.android.sparkstories.di.sparkservicestoriesmodules

import androidx.lifecycle.MutableLiveData
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.model.Resource
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import javax.inject.Singleton

/**
 * Service Module that returns false when queried for the currently
 * authenticated user's ID. This will cause the app to remain on
 * the [NewScreenNameFragment].
 */
@Module
class NewUserSparkStoryServiceModule {

    companion object {
        const val TEST_USER_SCREEN_NAME = "test_user_screen_name"
    }

    @Singleton
    @Provides
    fun provideRemoteDatabase(): SparkStoriesService {
        val sparkService: SparkStoriesService = mockk(relaxed = true)

        val userExistence = MutableLiveData<Resource<Boolean>>()
        userExistence.postValue(Resource.success(false))
        every { sparkService.checkUserExists() } returns userExistence

        val userHasScreenNameResponse = MutableLiveData<Resource<Boolean>>()
        userHasScreenNameResponse.postValue(Resource.success(false))
        every { sparkService.checkUserHasScreenName() } returns userHasScreenNameResponse

        val screenNameAvailable = MutableLiveData<Resource<Pair<Boolean, String>>>()
        screenNameAvailable.postValue(Resource.success(Pair(true, TEST_USER_SCREEN_NAME))); every { sparkService.checkScreenNameAvailable(any()) } returns screenNameAvailable

        val submitScreenName = MutableLiveData<Resource<Unit>>()
        submitScreenName.postValue(Resource.success(null))
        every { sparkService.submitScreenName(any()) } returns submitScreenName

        return sparkService
    }
}
