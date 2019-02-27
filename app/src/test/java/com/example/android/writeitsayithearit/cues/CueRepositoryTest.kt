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
        every { wshQueryHelper.cues("", SortOrder.NEW) } returns mockedQuery

        cueRepository.cues("", SortOrder.NEW)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyFilterByText() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.cues("Dogs", SortOrder.NEW) } returns mockedQuery

        cueRepository.cues("Dogs",SortOrder.NEW)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyOrderByNew() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.cues("", SortOrder.NEW) } returns mockedQuery

        cueRepository.cues("", SortOrder.NEW)
        verify(exactly = 1) { dao.cues(mockedQuery) }

    }

    @Test
    fun loadCuesLocallyOrderByTop() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.cues("", SortOrder.TOP) } returns mockedQuery

        cueRepository.cues("", SortOrder.TOP)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun loadCuesLocallyOrderByHot() {
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.cues("", SortOrder.HOT) } returns mockedQuery

        cueRepository.cues("", SortOrder.HOT)
        verify(exactly = 1) { dao.cues(mockedQuery) }
    }

    @Test
    fun submitCue() {
        val cue = TestUtils.createTestCue()
        cueRepository.submitCue(cue)
        verify(exactly = 1) { dao.insert(cue) }
    }

    @Test
    fun updateCue() {
        val cue = TestUtils.createTestCue()
        cueRepository.updateCue(cue)
        verify(exactly = 1) { dao.update(cue)}
    }

}