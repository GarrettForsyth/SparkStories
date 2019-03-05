package com.example.android.writeitsayithearit.cues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.cues.CuesViewModel
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.test.TestUtils.createTestCueList
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
    fun getCues() {
        // mock response
        val cues = createTestCueList(5)
        every { cueRepository.cues("", SortOrder.NEW) } returns cues.asLiveData()

        // set filter to be ""
        cuesViewModel.filterQuery = ""
        verify { cueRepository.cues("", SortOrder.NEW) }

        val observedCues = cuesViewModel.cues.getValueBlocking()
        cues.forEachIndexed { index, cue ->
            assertEquals(cues[index], observedCues[index])
        }
    }

    @Test
    fun filterQuery() {
        val filterString = "dogs"
        cuesViewModel.filterQuery = filterString

        verify(exactly = 1) { cueRepository.cues(filterString, SortOrder.NEW) }
    }

    @Test
    fun sortByNew() {
        cuesViewModel.sortOrder(SortOrder.NEW)
        verify(exactly = 1) { cueRepository.cues("", SortOrder.NEW) }
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
        cuesViewModel.filterQuery = filterString
        cuesViewModel.sortOrder(SortOrder.HOT)
        verify(exactly = 1) { cueRepository.cues("dogs", SortOrder.HOT) }
    }

    @Test
    fun filterQueryAndSortByNew() {
        val filterString = "dogs"
        cuesViewModel.sortOrder(SortOrder.NEW)
        cuesViewModel.filterQuery = filterString
        verify(exactly = 1) { cueRepository.cues("dogs", SortOrder.NEW) }
    }

    @Test
    fun filterQueryAndSortByTop() {
        val filterString = "dogs"
        cuesViewModel.filterQuery = filterString
        cuesViewModel.sortOrder(SortOrder.TOP)
        verify(exactly = 1) { cueRepository.cues("dogs", SortOrder.TOP) }
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
        cuesViewModel.onClickCue(0)
        assertEquals(0, cuesViewModel.cueClicked.getValueBlocking().peekContent())
    }

    @Test
    fun onClickNewCue(){
        cuesViewModel.onClickNewCue()
        assertTrue(cuesViewModel.newCueFabClick.getValueBlocking().peekContent())
    }
}