package com.example.android.sparkstories.stories

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.api.WriteItSayItHearItService
import com.example.android.sparkstories.data.local.StoryDao
import com.example.android.sparkstories.repos.story.StoryRepository
import com.example.android.sparkstories.repos.utils.SparkStoriesQueryHelper
import com.example.android.sparkstories.util.InstantAppExecutors
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.test.TestUtils.createTestStory
import com.example.android.sparkstories.ui.util.QueryParameters
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@SmallTest
@RunWith(JUnit4::class)
@Ignore
class StoryRepositoryTest {

    private val dao: StoryDao = mockk(relaxed = true)
    private val service: SparkStoriesService = mockk(relaxed = true)
    private val wshQueryHelper: SparkStoriesQueryHelper = mockk(relaxed = true)

    private val storyRepository = StoryRepository(
        InstantAppExecutors(),
        dao,
        service,
        wshQueryHelper
    )

    @Test
    fun loadStoriesLocally() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.stories(QueryParameters()) } returns mockedQuery

        storyRepository.stories(QueryParameters())
        verify(exactly = 1) { dao.stories(mockedQuery) }
    }

    @Test
    fun loadStoriesLocallyFilterByText() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        val queryParameters = QueryParameters(_filterString = "Dogs")
        every { wshQueryHelper.stories(queryParameters) } returns mockedQuery

        storyRepository.stories(queryParameters)
        verify(exactly = 1) { dao.stories(mockedQuery) }
    }

    @Test
    fun loadStoriesLocallyOrderByNew() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.stories(QueryParameters()) } returns mockedQuery

        storyRepository.stories(QueryParameters())
        verify(exactly = 1) { dao.stories(mockedQuery) }

    }

    @Test
    fun loadStoriesLocallyOrderByTop() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.stories(QueryParameters(_sortOrder = SortOrder.TOP)) } returns mockedQuery

        storyRepository.stories(QueryParameters(_sortOrder = SortOrder.TOP))
        verify(exactly = 1) { dao.stories(mockedQuery) }
    }

    @Test
    fun loadStoriesLocallyOrderByHot() {
        val mockedQuery: SupportSQLiteQuery = mockk()

        every { wshQueryHelper.stories(QueryParameters(_sortOrder = SortOrder.HOT)) } returns mockedQuery

        storyRepository.stories(QueryParameters(_sortOrder = SortOrder.HOT))

        verify(exactly = 1) { dao.stories(mockedQuery) }
    }

    @Test
    fun loadStory() {
        val storyId = UUID.randomUUID().toString()
        storyRepository.story(storyId)
        verify(exactly = 1) { dao.story(storyId) }
    }

    @Test
    fun submitStory() {
        val story = createTestStory()
        storyRepository.submitStory(story)
        verify(exactly = 1) { dao.insert(story) }
    }

    @Test
    fun updateStory() {
        val story = createTestStory()
        storyRepository.update(story)
        verify(exactly = 1) { dao.update(story)}
    }

}
