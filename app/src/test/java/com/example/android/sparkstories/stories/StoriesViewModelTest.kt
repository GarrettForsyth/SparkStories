package com.example.android.sparkstories.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.repos.story.StoryRepository
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.ui.stories.StoriesViewModel
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.ui.util.QueryParameters
import com.example.android.sparkstories.util.MockUtils.mockObserverFor
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*


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
        val expectedParameters = QueryParameters()
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterCue() {
        val id = UUID.randomUUID().toString()
        storiesViewModel.queryParameters.filterCueId = id
        val expectedParameters = QueryParameters(_filterCueId = id)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQuery() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.filterString = filterString

        val expectedParameters = QueryParameters(_filterString = filterString)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndCue() {
        val filterString = "dogs"
        val id = UUID.randomUUID().toString()
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            filterCueId = id
        }
        val expectedParameters = QueryParameters(_filterString = filterString, _filterCueId = id)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByNew() {
        storiesViewModel.queryParameters.sortOrder = SortOrder.NEW
        val expectedParameters = QueryParameters()
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByNewAndCue() {
        val id = UUID.randomUUID().toString()
        storiesViewModel.queryParameters.apply {
            filterCueId = id
            sortOrder = SortOrder.NEW
        }
        val expectedParameters = QueryParameters(_filterCueId = id)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByTop() {
        storiesViewModel.queryParameters.sortOrder = SortOrder.TOP
        val expectedParameters = QueryParameters(_sortOrder = SortOrder.TOP)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByTopAndCue() {
        val id = UUID.randomUUID().toString()
        storiesViewModel.queryParameters.apply {
            filterCueId = id
            sortOrder = SortOrder.TOP
        }
        val expectedParameters = QueryParameters(_filterCueId = id, _sortOrder = SortOrder.TOP)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByHot() {
        storiesViewModel.queryParameters.sortOrder = SortOrder.HOT
        val expectedParameters = QueryParameters(_sortOrder = SortOrder.HOT)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun sortByHotAndCue() {
        val id = UUID.randomUUID().toString()
        storiesViewModel.queryParameters.apply {
            sortOrder = SortOrder.HOT
            filterCueId = id
        }
        val expectedParameters = QueryParameters(_filterCueId = id, _sortOrder = SortOrder.HOT)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByHot() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.HOT
        }
        val expectedParameters = QueryParameters(_filterString = "dogs", _sortOrder = SortOrder.HOT)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByHotAndCue() {
        val filterString = "dogs"
        val id = UUID.randomUUID().toString()
        storiesViewModel.queryParameters.apply {
            storiesViewModel.queryParameters.apply {
                this.filterString = filterString
                filterCueId = id
                sortOrder = SortOrder.HOT
            }
            val expectedParameters =
                QueryParameters(_filterCueId = id, _filterString = filterString, _sortOrder = SortOrder.HOT)
            verify { storyRepository.stories(expectedParameters) }
        }
    }

    @Test
    fun filterQueryAndSortByNew() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            sortOrder = SortOrder.NEW
            this.filterString = filterString
        }
        val expectedParameters = QueryParameters(_filterString = filterString, _sortOrder = SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByNewAndCue() {
        val filterString = "dogs"
        val id = UUID.randomUUID().toString()
        storiesViewModel.queryParameters.apply {
            sortOrder = SortOrder.NEW
            this.filterString = filterString
            filterCueId = id
        }
        val expectedParameters =
            QueryParameters(_filterCueId = id, _filterString = filterString, _sortOrder = SortOrder.NEW)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByTop() {
        val filterString = "dogs"
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.TOP
        }
        val expectedParameters = QueryParameters(_filterString = "dogs", _sortOrder = SortOrder.TOP)
        verify { storyRepository.stories(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByTopAndCue() {
        val filterString = "dogs"
        val id = UUID.randomUUID().toString()
        storiesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.TOP
            filterCueId = id
        }
        val expectedParameters =
            QueryParameters(_filterCueId = id, _filterString = filterString, _sortOrder = SortOrder.TOP)
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
    fun onClickStory() {
        val storyId = UUID.randomUUID().toString()
        storiesViewModel.onClickStory(storyId)
        assertEquals(storyId, storiesViewModel.storyClicked.getValueBlocking().peekContent())
    }

}
