package com.example.android.writeitsayithearit.cues

import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.util.InstantAppExecutors
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
    private val cueRepository = CueRepository(InstantAppExecutors(), dao, service)


    @Test
    fun loadCuesLocally() {
        cueRepository.cues()
        verify(exactly = 1) { dao.cues() }
    }

    @Test
    fun submitCue() {
        val cue = TestUtils.createTestCue()
        cueRepository.submitCue(cue)
        verify(exactly = 1) { dao.insert(cue) }
    }

}