package com.example.android.writeitsayithearit.repos

import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.vo.Story
import javax.inject.Inject

class StoryRepository @Inject constructor(
        val appExecutors: AppExecutors,
        val storyDao: StoryDao,
        val service: WriteItSayItHearItService
) {

    fun stories() = storyDao.stories()

    fun submitStory(story: Story) {
        appExecutors.diskIO().execute { storyDao.insert(story) }
    }


}
