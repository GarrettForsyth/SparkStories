package com.example.android.writeitsayithearit.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.vo.SortOrder
import com.example.android.writeitsayithearit.vo.Story
import io.mockk.mockk
import io.mockk.verify
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

        // Observe stories so live data will propagate changes
        val mockObserver: Observer<List<Story>> = mockk(relaxed = true)
        storiesViewModel.stories.observeForever(mockObserver)
    }

    @Test
    fun callsStoriesWithNoFilterWhenCreated() {
        verify(exactly = 1) { storyRepository.stories("", SortOrder.NEW) }
    }

    @Test
    fun filterQueryCallsRepository() {
        val filterString = "dogs"
        storiesViewModel.filterQuery(filterString)
        verify(exactly = 1) { storyRepository.stories(filterString, SortOrder.NEW) }
    }

    @Test
    fun sortByNew() {
        storiesViewModel.sortOrder(SortOrder.NEW)
        // called twice since it's the default value when called during initiaization
        verify(exactly = 2) { storyRepository.stories("", SortOrder.NEW) }
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
    fun filterQueryThen() {
        val filterString = "dogs"
        storiesViewModel.filterQuery(filterString)
        storiesViewModel.sortOrder(SortOrder.HOT)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.HOT) }
    }

    @Test
    fun sortByHotThenAddAFilterQuery() {
        storiesViewModel.sortOrder(SortOrder.HOT)
        val filterString = "dogs"
        storiesViewModel.filterQuery(filterString)
        verify(exactly = 1) { storyRepository.stories("dogs", SortOrder.HOT) }
    }
}
