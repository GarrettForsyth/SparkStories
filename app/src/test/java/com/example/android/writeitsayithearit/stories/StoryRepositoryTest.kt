package com.example.android.writeitsayithearit.stories

import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.util.InstantAppExecutors
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class StoryRepositoryTest {

    private val dao: StoryDao = mockk(relaxed = true)
    private val service: WriteItSayItHearItService = mockk(relaxed = true)
    private val storyRepository = StoryRepository(InstantAppExecutors(), dao, service)


    @Test
    fun submitStory() {
        val story = TestUtils.createTestStory()
        storyRepository.submitStory(story)
        verify(exactly = 1) { dao.insert(story) }
    }

    @Test
    fun loadStoriesLocally() {
        storyRepository.stories()
        verify(exactly = 1) { dao.stories() }
    }
}