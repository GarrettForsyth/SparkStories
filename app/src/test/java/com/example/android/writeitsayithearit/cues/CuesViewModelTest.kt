package com.example.android.writeitsayithearit.cues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.ui.cues.CuesViewModel
import com.example.android.writeitsayithearit.vo.CueContract
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.SortOrder
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class CuesViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val cueRepository: CueRepository = mockk(relaxed = true)

    private lateinit var cuesViewModel: CuesViewModel

    @Before
    fun init() {
        cuesViewModel = CuesViewModel(cueRepository)

        // Observe cues so live data will propagate changes
        val mockObserver: Observer<List<Cue>> = mockk(relaxed = true)
        cuesViewModel.cues.observeForever(mockObserver)
    }

    @Test
    fun callsCuesWithNoFilterWhenCreated() {
        verify(exactly = 1) { cueRepository.cues("", SortOrder.NEW) }
    }

    @Test
    fun filterQueryCallsRepositoryForEachKeyStroke() {
        val filterString = "dogs"
        cuesViewModel.filterQuery(filterString)

        verify(exactly = 1) { cueRepository.cues(filterString, SortOrder.NEW) }
    }

    @Test
    fun sortByNew() {
        cuesViewModel.sortOrder(SortOrder.NEW)
        // called twice since it's the default value when called during initiaization
        verify(exactly = 2) { cueRepository.cues("", SortOrder.NEW) }
    }

    @Test
    fun sortByTop() {
        cuesViewModel.sortOrder(SortOrder.TOP)
        verify(exactly = 1) { cueRepository.cues("", SortOrder.TOP) }
    }

    @Test
    fun sortByHot() {
        cuesViewModel.sortOrder(SortOrder.HOT)
        verify(exactly = 1) { cueRepository.cues("", SortOrder.HOT) }
    }

    @Test
    fun filterQueryAndSortByHot() {
        val filterString = "dogs"
        cuesViewModel.filterQuery(filterString)
        cuesViewModel.sortOrder(SortOrder.HOT)
        verify(exactly = 1) { cueRepository.cues("dogs", SortOrder.HOT) }
    }

    @Test
    fun sortByHotThenAddAFilterQuery() {
        cuesViewModel.sortOrder(SortOrder.HOT)
        val filterString = "dogs"
        cuesViewModel.filterQuery(filterString)
        verify(exactly = 1) { cueRepository.cues("dogs", SortOrder.HOT) }
    }
}