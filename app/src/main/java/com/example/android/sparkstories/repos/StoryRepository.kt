package com.example.android.sparkstories.repos

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.api.WriteItSayItHearItService
import com.example.android.sparkstories.data.local.StoryDao
import com.example.android.sparkstories.repos.utils.WSHQueryHelper
import com.example.android.sparkstories.model.story.Story
import com.example.android.sparkstories.ui.util.QueryParameters
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val storyDao: StoryDao,
    private val service: WriteItSayItHearItService,
    private val wshQueryHelper: WSHQueryHelper
) {

    fun stories(queryParameters: QueryParameters): LiveData<PagedList<Story>> {
        val factory =  storyDao.stories(wshQueryHelper.stories(
            queryParameters
        ))
        return LivePagedListBuilder<Int, Story>(factory, getStoryPagedListConfig()).build()
    }

    fun story(id: Int) = storyDao.story(id)

    fun update(story: Story) {
        appExecutors.diskIO().execute { storyDao.update(story) }
    }

    fun submitStory(story: Story) {
        appExecutors.diskIO().execute { storyDao.insert(story) }
    }

    companion object {
        private const val PAGE_SIZE = 45
        private const val PREFETCH_DISTANCE = 90
        private const val INITIAL_LOAD_HINT = 90

        private fun getStoryPagedListConfig(): PagedList.Config {
            return PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setInitialLoadSizeHint(INITIAL_LOAD_HINT)
                .build()
        }
    }

}
