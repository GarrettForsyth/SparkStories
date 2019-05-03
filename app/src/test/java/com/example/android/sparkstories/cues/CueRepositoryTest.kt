package com.example.android.sparkstories.cues

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.data.local.CueDao
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.repos.utils.SparkStoriesQueryHelper
import com.example.android.sparkstories.util.InstantAppExecutors
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.repos.cue.CueBoundaryCallback
import com.example.android.sparkstories.test.TestUtils.createTestCue
import com.example.android.sparkstories.ui.util.QueryParameters
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
@Ignore
class CueRepositoryTest {

    private val dao: CueDao = mockk(relaxed = true)
    private val sparkStoriesService: SparkStoriesService = mockk(relaxed = true)
    private val sparkStoriesQueryHelper: SparkStoriesQueryHelper = mockk(relaxed = true)
    private val cueBoundaryCallback: CueBoundaryCallback = mockk(relaxed = true)

    private val cueRepository =
        CueRepository(InstantAppExecutors(), dao, sparkStoriesService, sparkStoriesQueryHelper)

    @Test
    fun loadCuesLocally() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { sparkStoriesQueryHelper.cues(QueryParameters()) } returns mockedQuery

        cueRepository.cues(QueryParameters())
//        verify(exactly = 1) { cueBoundaryCallback.queryParameters = QueryParameters()}
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyFilterByText() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        val queryParameters = QueryParameters(_filterString = "Dogs")
        every { sparkStoriesQueryHelper.cues(queryParameters) } returns mockedQuery

        cueRepository.cues(queryParameters)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyOrderByNew() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { sparkStoriesQueryHelper.cues(QueryParameters())} returns mockedQuery

        cueRepository.cues(QueryParameters())
        verify(exactly = 1) { dao.cues(mockedQuery) }

    }

    @Test
    fun loadCuesLocallyOrderByTop() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        val queryParameters = QueryParameters(_sortOrder = SortOrder.TOP)
        every { sparkStoriesQueryHelper.cues(queryParameters) } returns mockedQuery

        cueRepository.cues(queryParameters)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyOrderByHot() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        val queryParameters = QueryParameters(_sortOrder = SortOrder.HOT)
        every { sparkStoriesQueryHelper.cues(queryParameters) } returns mockedQuery

        cueRepository.cues(queryParameters)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun submitCue() {
        val cue = createTestCue()
        cueRepository.submitCue(cue)
        verify(exactly = 1) { sparkStoriesService.submitCue(cue) }
    }

    @Test
    fun updateCue() {
        val cue = createTestCue()
        cueRepository.updateCue(cue)
        verify(exactly = 1) { dao.update(cue)}
    }

}