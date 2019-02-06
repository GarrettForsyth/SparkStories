package com.example.android.writeitsayithearit.cue

import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.repos.CueRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CueRepositoryTest {


    private val dao: CueDao = mockk(relaxed = true)
    private val service: WriteItSayItHearItService = mockk(relaxed = true)

    private val cueRepository = CueRepository(dao, service)

    @Test
    fun loadCuesFromNetwork() {
        cueRepository.cues()
        verify(exactly = 1) { dao.cues() }
    }

}