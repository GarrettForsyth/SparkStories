package com.example.android.writeitsayithearit.repos

import androidx.lifecycle.LiveData
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.AuthorDao
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.story.Story
import javax.inject.Inject

class StoryRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val storyDao: StoryDao,
        private val service: WriteItSayItHearItService,
        private val wshQueryHelper: WSHQueryHelper
) {

    fun stories(filterText: String, sortOrder: SortOrder, cueId: Int): LiveData<List<Story>> {
        return storyDao.stories(wshQueryHelper.stories(filterText, sortOrder, cueId))
    }

    fun story(id: Int) = storyDao.story(id)

    fun update(story: Story) {
        appExecutors.diskIO().execute { storyDao.update(story) }
    }

    fun submitStory(story: Story) {
        appExecutors.diskIO().execute { storyDao.insert(story) }
    }

}
