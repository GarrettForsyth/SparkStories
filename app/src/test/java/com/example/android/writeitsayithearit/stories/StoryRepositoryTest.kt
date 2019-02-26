package com.example.android.writeitsayithearit.stories

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.util.InstantAppExecutors
import com.example.android.writeitsayithearit.model.SortOrder
import io.mockk.every
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
    private val wshQueryHelper: WSHQueryHelper = mockk(relaxed = true)

    private val storyRepository = StoryRepository(InstantAppExecutors(), dao, service, wshQueryHelper)

    @Test
    fun loadStoriesLocally() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.stories("", SortOrder.NEW) } returns mockedQuery

        storyRepository.stories("", SortOrder.NEW)
        verify(exactly = 1) { dao.stories(mockedQuery) }
    }

    @Test
    fun loadStoriesLocallyFilterByText() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.stories("Dogs", SortOrder.NEW) } returns mockedQuery

        storyRepository.stories("Dogs",SortOrder.NEW)
        verify(exactly = 1) { dao.stories(mockedQuery) }
    }

    @Test
    fun loadStoriesLocallyOrderByNew() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.stories("", SortOrder.NEW) } returns mockedQuery

        storyRepository.stories("", SortOrder.NEW)
        verify(exactly = 1) { dao.stories(mockedQuery) }

    }

    @Test
    fun loadStoriesLocallyOrderByTop() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.stories("", SortOrder.TOP) } returns mockedQuery

        storyRepository.stories("", SortOrder.TOP)
        verify(exactly = 1) { dao.stories(mockedQuery) }
    }

    @Test
    fun loadStoriesLocallyOrderByHot() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.stories("", SortOrder.HOT) } returns mockedQuery

        storyRepository.stories("", SortOrder.HOT)
        verify(exactly = 1) { dao.stories(mockedQuery) }
    }

    @Test
    fun submitStory() {
        val story = TestUtils.createTestStory()
        storyRepository.submitStory(story)
        verify(exactly = 1) { dao.insert(story) }
    }

}
