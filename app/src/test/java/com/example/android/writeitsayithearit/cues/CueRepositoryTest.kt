package com.example.android.writeitsayithearit.cues

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.util.InstantAppExecutors
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.test.TestUtils.createTestCue
import com.example.android.writeitsayithearit.ui.util.QueryParameters
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class CueRepositoryTest {

    private val dao: CueDao = mockk(relaxed = true)
    private val service: WriteItSayItHearItService = mockk(relaxed = true)
    private val wshQueryHelper: WSHQueryHelper = mockk(relaxed = true)

    private val cueRepository = CueRepository(InstantAppExecutors(), dao, service, wshQueryHelper)

    @Test
    fun loadCuesLocally() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.cues(QueryParameters()) } returns mockedQuery

        cueRepository.cues(QueryParameters())
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyFilterByText() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        val queryParameters = QueryParameters(_filterString = "Dogs")
        every { wshQueryHelper.cues(queryParameters) } returns mockedQuery

        cueRepository.cues(queryParameters)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyOrderByNew() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.cues(QueryParameters())} returns mockedQuery

        cueRepository.cues(QueryParameters())
        verify(exactly = 1) { dao.cues(mockedQuery) }

    }

    @Test
    fun loadCuesLocallyOrderByTop() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        val queryParameters = QueryParameters(_sortOrder = SortOrder.TOP)
        every { wshQueryHelper.cues(queryParameters) } returns mockedQuery

        cueRepository.cues(queryParameters)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyOrderByHot() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        val queryParameters = QueryParameters(_sortOrder = SortOrder.HOT)
        every { wshQueryHelper.cues(queryParameters) } returns mockedQuery

        cueRepository.cues(queryParameters)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun submitCue() {
        val cue = createTestCue()
        cueRepository.submitCue(cue)
        verify(exactly = 1) { dao.insert(cue) }
    }

    @Test
    fun updateCue() {
        val cue = createTestCue()
        cueRepository.updateCue(cue)
        verify(exactly = 1) { dao.update(cue)}
    }

}