package com.example.android.writeitsayithearit.repos

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.AuthorDao
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story
import javax.inject.Inject

class StoryRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val storyDao: StoryDao,
        private val service: WriteItSayItHearItService,
        private val wshQueryHelper: WSHQueryHelper
) {

    fun stories(filterText: String, sortOrder: SortOrder, cueId: Int): LiveData<PagedList<Story>> {
        val factory =  storyDao.stories(wshQueryHelper.stories(filterText, sortOrder, cueId))
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
