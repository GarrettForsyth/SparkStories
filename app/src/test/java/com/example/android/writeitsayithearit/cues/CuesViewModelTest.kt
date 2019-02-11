package com.example.android.writeitsayithearit.cues

import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.ui.cues.CuesViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class CuesViewModelTest {

    private val cueRepository: CueRepository = mockk(relaxed = true)
    private val cuesViewModel = CuesViewModel(cueRepository)

    @Test
    fun cuesCallsRepository() {
        verify(exactly = 1) { cueRepository.cues() }
    }
}