package com.example.android.writeitsayithearit.cue

import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.ui.CuesViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CuesViewModelTest {

    private val cueRepository: CueRepository = mockk(relaxed = true)
    private lateinit var cuesViewModel: CuesViewModel

    init {
        // init after the instant executor rule is established
        cuesViewModel = CuesViewModel(cueRepository)
    }

    @Test
    fun cuesCallsRepository() {
        verify(exactly = 1) { cueRepository.cues() }
    }
}