package com.example.android.writeitsayithearit.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.TestUtils.STARTING_STORIES
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.vo.SortOrder
import com.example.android.writeitsayithearit.vo.Story
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

        // observe stories
        val mockObserver: Observer<List<Story>> = mockk(relaxed = true)
        storiesViewModel.stories.observeForever(mockObserver)
    }

    @Test
    fun getStories() {
        // mock response
        val liveStories = MutableLiveData<List<Story>>()
        liveStories.value = STARTING_STORIES
        every { storyRepository.stories("", SortOrder.NEW) } returns liveStories

        // set filter to be ""
        storiesViewModel.filterQuery = ""
        verify { storyRepository.stories("", SortOrder.NEW) }

        val observedStories = storiesViewModel.stories.getValueBlocking()
        STARTING_STORIES.forEachIndexed { index, story ->
            assertEquals(STARTING_STORIES[index], observedStories[index])
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
        // observe live data
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        storiesViewModel.hasResultsStatus.observeForever(mockObserver)

        storiesViewModel.setHasResults(true)
        assertTrue(storiesViewModel.hasResultsStatus.getValueBlocking().peekContent())

        storiesViewModel.setHasResults(false)
        assertFalse(storiesViewModel.hasResultsStatus.getValueBlocking().peekContent())
    }

    @Test
    fun onClickStory(){
        // observe live data
        val mockObserver: Observer<Event<Int>> = mockk(relaxed = true)
        storiesViewModel.storyClicked.observeForever(mockObserver)

        storiesViewModel.onClickStory(0)
        assertEquals(0, storiesViewModel.storyClicked.getValueBlocking().peekContent())
    }

}
