package com.example.android.writeitsayithearit.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.test.TestUtils.createTestStoryList
import com.example.android.writeitsayithearit.test.asLiveData
import com.example.android.writeitsayithearit.util.MockUtils.mockObserverFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@SmallTest
@RunWith(JUnit4::class)
class StoriesViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val storyRepository: StoryRepository = mockk(relaxed = true)

    private lateinit var storiesViewModel: StoriesViewModel

    @Before
    fun init() {
        storiesViewModel = StoriesViewModel(storyRepository)

        mockObserverFor(storiesViewModel.stories)
        mockObserverFor(storiesViewModel.hasResultsStatus)
        mockObserverFor(storiesViewModel.storyClicked)
    }

    @Test
    fun getStories() {
        // mock response
        val stories = createTestStoryList(5)
        every { storyRepository.stories("", SortOrder.NEW) } returns stories.asLiveData()

        // set filter to be ""
        storiesViewModel.filterQuery = ""
        verify { storyRepository.stories("", SortOrder.NEW) }

        val observedStories = storiesViewModel.stories.getValueBlocking()
        stories.forEachIndexed { index, story ->
            assertEquals(stories[index], observedStories[index])
        }
    }
    @Test
    fun filterQuery() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString

        verify(exactly = 1) { storyRepository.stories(filterString, SortOrder.NEW) }
    }

    @Test
    fun sortByNew() {
        storiesViewModel.sortOrder(SortOrder.NEW)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.NEW) }
    }

    @Test
    fun sortByTop() {
        storiesViewModel.sortOrder(SortOrder.TOP)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.TOP) }
    }

    @Test
    fun sortByHot() {
        storiesViewModel.sortOrder(SortOrder.HOT)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.HOT) }
    }

    @Test
    fun filterQueryAndSortByHot() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString
        storiesViewModel.sortOrder(SortOrder.HOT)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.HOT) }
    }

    @Test
    fun filterQueryAndSortByNew() {
        val filterString = "dogs"
        storiesViewModel.sortOrder(SortOrder.NEW)
        storiesViewModel.filterQuery = filterString
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.NEW) }
    }

    @Test
    fun filterQueryAndSortByTop() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString
        storiesViewModel.sortOrder(SortOrder.TOP)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.TOP) }
    }

    @Test
    fun setHasResults() {
        storiesViewModel.setHasResults(true)
        assertTrue(storiesViewModel.hasResultsStatus.getValueBlocking().peekContent())

        storiesViewModel.setHasResults(false)
        assertFalse(storiesViewModel.hasResultsStatus.getValueBlocking().peekContent())
    }

    @Test
    fun onClickStory(){
        storiesViewModel.onClickStory(0)
        assertEquals(0, storiesViewModel.storyClicked.getValueBlocking().peekContent())
    }

}
