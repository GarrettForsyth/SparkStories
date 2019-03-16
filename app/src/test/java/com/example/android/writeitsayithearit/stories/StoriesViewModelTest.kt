package com.example.android.writeitsayithearit.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.test.TestUtils.createTestStoryList
import com.example.android.writeitsayithearit.test.asLiveData
import com.example.android.writeitsayithearit.ui.util.QueryParameters
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
        storiesViewModel.queryParameters.filterString = ""
        val expectedParameters = QueryParameters(-1, "", SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterCue() {
        storiesViewModel.queryParameters.filterCueId = 1
        val expectedParameters = QueryParameters(1, "", SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQuery() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.filterString = filterString

        val expectedParameters = QueryParameters(-1, filterString, SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndCue() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            filterCueId = 1
        }
        val expectedParameters = QueryParameters(1, filterString, SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByNew() {
        storiesViewModel.queryParameters.sortOrder = SortOrder.NEW
        val expectedParameters = QueryParameters(-1, "", SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByNewAndCue() {
        storiesViewModel.queryParameters.apply {
            filterCueId = 1
            sortOrder = SortOrder.NEW
        }
        val expectedParameters = QueryParameters(1, "", SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByTop() {
        storiesViewModel.queryParameters.sortOrder = SortOrder.TOP
        val expectedParameters = QueryParameters(-1, "", SortOrder.TOP)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByTopAndCue() {
        storiesViewModel.queryParameters.apply {
            filterCueId = 1
            sortOrder = SortOrder.TOP
        }
        val expectedParameters = QueryParameters(1, "", SortOrder.TOP)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByHot() {
        storiesViewModel.queryParameters.sortOrder = SortOrder.HOT
        val expectedParameters = QueryParameters(-1, "", SortOrder.HOT)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByHotAndCue() {
        storiesViewModel.queryParameters.apply {
            sortOrder = SortOrder.HOT
            filterCueId = 1
        }
        val expectedParameters = QueryParameters(1, "", SortOrder.HOT)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByHot() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.HOT
        }
        val expectedParameters = QueryParameters(-1, "dogs", SortOrder.HOT)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByHotAndCue() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            filterCueId = 1
            sortOrder = SortOrder.HOT
        }
        val expectedParameters = QueryParameters(1, "dogs", SortOrder.HOT)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByNew() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            sortOrder = SortOrder.NEW
            this.filterString = filterString
        }
        val expectedParameters = QueryParameters(-1, "dogs", SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByNewAndCue() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            sortOrder = SortOrder.NEW
            this.filterString = filterString
            filterCueId = 1
        }
        val expectedParameters = QueryParameters(1, "dogs", SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByTop() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.TOP
        }
        val expectedParameters = QueryParameters(-1, "dogs", SortOrder.TOP)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByTopAndCue() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.TOP
            filterCueId = 1
        }
        val expectedParameters = QueryParameters(1, "dogs", SortOrder.TOP)
        verify { storyRepository.stories(expectedParameters) }
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
