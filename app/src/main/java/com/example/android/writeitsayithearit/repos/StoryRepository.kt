package com.example.android.writeitsayithearit.repos

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.vo.SortOrder
import com.example.android.writeitsayithearit.vo.Story
import javax.inject.Inject

class StoryRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val storyDao: StoryDao,
        private val service: WriteItSayItHearItService,
        private val wshQueryHelper: WSHQueryHelper
) {

    fun stories(filterText: String, sortOrder: SortOrder): LiveData<List<Story>> {
        return storyDao.stories(wshQueryHelper.stories(filterText, sortOrder))
    }

    fun submitStory(story: Story) {
        appExecutors.diskIO().execute { storyDao.insert(story) }
    }

}
