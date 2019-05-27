package com.example.android.sparkstories.repos.story

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.api.ApiResponse
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.api.WriteItSayItHearItService
import com.example.android.sparkstories.data.local.StoryDao
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.repos.utils.SparkStoriesQueryHelper
import com.example.android.sparkstories.model.story.Story
import com.example.android.sparkstories.repos.cue.CueBoundaryCallback
import com.example.android.sparkstories.repos.utils.NetworkBoundResource
import com.example.android.sparkstories.test.asLiveData
import com.example.android.sparkstories.ui.util.QueryParameters
import timber.log.Timber
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val storyDao: StoryDao,
    private val sparkStoriesService: SparkStoriesService,
    private val wshQueryHelper: SparkStoriesQueryHelper
) {

//    fun stories(queryParameters: QueryParameters): LiveData<PagedList<Story>> {
//        Timber.d("storytest getting stories with $queryParameters")
//        val factory =  storyDao.stories(wshQueryHelper.stories(
//            queryParameters
//        ))
//        return LivePagedListBuilder<Int, Story>(factory,
//            getStoryPagedListConfig()
//        ).build()
//    }

    fun stories(queryParameters: QueryParameters): LiveData<Resource<PagedList<Story>>> {
        return object : NetworkBoundResource<PagedList<Story>, List<Story>>(appExecutors) {

            override fun loadFromDb(): LiveData<PagedList<Story>> {
                Timber.d("mytest loadFromDbCalled")

                val factory = storyDao.stories(
                    wshQueryHelper.stories(
                        queryParameters
                    )
                )

                val storyBoundaryCallback = StoryBoundaryCallback(
                    queryParameters,
                    this@StoryRepository,
                    sparkStoriesService
                )

                return LivePagedListBuilder<Int, Story>(
                    factory,
                    getStoryPagedListConfig()
                ).setBoundaryCallback(storyBoundaryCallback)
                    .build()
            }

            // Always fetch from the network to get most recent data
            override fun shouldFetch(data: PagedList<Story>?): Boolean {
                Timber.d("mytest shouldFetchCalled")
                return true
            }

            override fun createCall(): LiveData<ApiResponse<List<Story>>> {
                Timber.d("mytest createCall called ")
                return sparkStoriesService.getStories(queryParameters)
            }

            override fun saveCallResult(item: List<Story>) {
                Timber.d("mytest saveCallResult called")
                storyDao.insert(item)
            }

        }.asLiveData()

    }

    fun story(id: String) = storyDao.story(id)

    fun update(story: Story) {
        appExecutors.diskIO().execute { storyDao.update(story) }
    }

    fun submitStory(story: Story) {
        appExecutors.diskIO().execute { storyDao.insert(story) }
    }

    companion object {
        private const val PAGE_SIZE = 15
        private const val PREFETCH_DISTANCE = 60

        private fun getStoryPagedListConfig(): PagedList.Config {
            return PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build()
        }
    }

}
