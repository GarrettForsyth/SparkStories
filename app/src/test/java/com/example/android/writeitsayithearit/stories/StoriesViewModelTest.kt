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
        // set filter to be ""
        storiesViewModel.filterQuery = ""
        verify { storyRepository.stories("", SortOrder.NEW, -1) }
    }

    @Test
    fun filterCue() {
        storiesViewModel.filterCue(1)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.NEW, 1) }
    }

    @Test
    fun filterQuery() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString

        verify(exactly = 1) { storyRepository.stories(filterString, SortOrder.NEW, -1) }
    }

    @Test
    fun filterQueryAndCue() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString
        storiesViewModel.filterCue(1)

        verify(exactly = 1) { storyRepository.stories(filterString, SortOrder.NEW, 1) }
    }

    @Test
    fun sortByNew() {
        storiesViewModel.sortOrder(SortOrder.NEW)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.NEW, -1) }
    }

    @Test
    fun sortByNewAndCue() {
        storiesViewModel.sortOrder(SortOrder.NEW)
        storiesViewModel.filterCue(1)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.NEW, 1) }
    }

    @Test
    fun sortByTop() {
        storiesViewModel.sortOrder(SortOrder.TOP)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.TOP, -1) }
    }

    @Test
    fun sortByTopAndCue() {
        storiesViewModel.sortOrder(SortOrder.TOP)
        storiesViewModel.filterCue(1)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.TOP, 1) }
    }

    @Test
    fun sortByHot() {
        storiesViewModel.sortOrder(SortOrder.HOT)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.HOT, -1) }
    }

    @Test
    fun sortByHotAndCue() {
        storiesViewModel.sortOrder(SortOrder.HOT)
        storiesViewModel.filterCue(1)
        verify(exactly = 1) { storyRepository.stories("", SortOrder.HOT, 1) }
    }

    @Test
    fun filterQueryAndSortByHot() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString
        storiesViewModel.sortOrder(SortOrder.HOT)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.HOT, -1) }
    }

    @Test
    fun filterQueryAndSortByHotAndCue() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString
        storiesViewModel.sortOrder(SortOrder.HOT)
        storiesViewModel.filterCue(1)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.HOT, 1) }
    }

    @Test
    fun filterQueryAndSortByNew() {
        val filterString = "dogs"
        storiesViewModel.sortOrder(SortOrder.NEW)
        storiesViewModel.filterQuery = filterString
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.NEW, -1) }
    }

    @Test
    fun filterQueryAndSortByNewAndCue() {
        val filterString = "dogs"
        storiesViewModel.sortOrder(SortOrder.NEW)
        storiesViewModel.filterQuery = filterString
        storiesViewModel.filterCue(1)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.NEW, 1) }
    }

    @Test
    fun filterQueryAndSortByTop() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString
        storiesViewModel.sortOrder(SortOrder.TOP)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.TOP, -1) }
    }

    @Test
    fun filterQueryAndSortByTopAndCue() {
        val filterString = "dogs"
        storiesViewModel.filterQuery = filterString
        storiesViewModel.sortOrder(SortOrder.TOP)
        storiesViewModel.filterCue(1)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.TOP, 1) }
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
