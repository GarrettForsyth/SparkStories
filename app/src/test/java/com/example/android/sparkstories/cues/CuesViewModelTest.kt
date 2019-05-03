package com.example.android.sparkstories.cues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.ui.cues.CuesViewModel
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
class CuesViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val cueRepository: CueRepository = mockk(relaxed = true)

    private lateinit var cuesViewModel: CuesViewModel

    @Before
    fun init() {
        cuesViewModel = CuesViewModel(cueRepository)

        mockObserverFor(cuesViewModel.cues)
        mockObserverFor(cuesViewModel.hasResultsStatus)
        mockObserverFor(cuesViewModel.cueClicked)
        mockObserverFor(cuesViewModel.newCueFabClick)
    }

    @Test
    fun intializationQueriesCuesOnce() {
        verify(exactly = 1) { cueRepository.cues(any()) }
    }


    @Test
    fun getCues() {
        cuesViewModel.queryParameters.filterString = ""
        val expectedParameters = QueryParameters()
        verify { cueRepository.cues(expectedParameters) }
    }


    @Test
    fun filterQuery() {
        val filterString = "dogs"
        cuesViewModel.queryParameters.filterString = filterString
        val expectedParameters = QueryParameters(_filterString =  filterString)
        verify { cueRepository.cues(expectedParameters) }
    }

    @Test
    fun sortByNew() {
        cuesViewModel.queryParameters.sortOrder = SortOrder.NEW
        val expectedParameters = QueryParameters(_sortOrder =  SortOrder.NEW)
        verify { cueRepository.cues(expectedParameters) }
    }

    @Test
    fun sortByTop() {
        cuesViewModel.queryParameters.sortOrder = SortOrder.TOP
        val expectedParameters = QueryParameters(_sortOrder = SortOrder.TOP)
        verify { cueRepository.cues(expectedParameters) }
    }

    @Test
    fun sortByHot() {
        cuesViewModel.queryParameters.sortOrder = SortOrder.HOT
        val expectedParameters = QueryParameters(_sortOrder = SortOrder.HOT)
        verify { cueRepository.cues(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByHot() {
        val filterString = "dogs"
        cuesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.HOT
        }
        val expectedParameters = QueryParameters(_filterString = filterString, _sortOrder =  SortOrder.HOT)
        verify { cueRepository.cues(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByNew() {
        val filterString = "dogs"
        cuesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.NEW
        }
        val expectedParameters = QueryParameters(_filterString = filterString, _sortOrder = SortOrder.NEW)
        verify { cueRepository.cues(expectedParameters) }
    }

    @Test
    fun filterQueryAndSortByTop() {
        val filterString = "dogs"
        cuesViewModel.queryParameters.apply {
            this.filterString = filterString
            sortOrder = SortOrder.TOP
        }
        val expectedParameters = QueryParameters(_filterString = filterString, _sortOrder = SortOrder.TOP)
        verify { cueRepository.cues(expectedParameters) }
    }

    @Test
    fun setHasResults() {
        cuesViewModel.setHasResults(true)
        assertTrue(cuesViewModel.hasResultsStatus.getValueBlocking().peekContent())

        cuesViewModel.setHasResults(false)
        assertFalse(cuesViewModel.hasResultsStatus.getValueBlocking().peekContent())
    }

    @Test
    fun onClickCue(){
        val id = UUID.randomUUID().toString()
        cuesViewModel.onClickCue(id)
        assertEquals(id, cuesViewModel.cueClicked.getValueBlocking().peekContent())
    }

    @Test
    fun onClickNewCue(){
        cuesViewModel.onClickNewCue()
        assertTrue(cuesViewModel.newCueFabClick.getValueBlocking().peekContent())
    }
}