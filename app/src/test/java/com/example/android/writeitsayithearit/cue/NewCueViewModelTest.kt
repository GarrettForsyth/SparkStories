package com.example.android.writeitsayithearit.cue

import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.CuesViewModel
import com.example.android.writeitsayithearit.ui.NewCueViewModel
import com.example.android.writeitsayithearit.vo.Cue
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class NewCueViewModelTest {

    private val cueRepository: CueRepository = mockk(relaxed = true)
    private val newCueViewModel = NewCueViewModel(cueRepository)



    @Test
    fun submitCueCallsRepository() {
        val cue = TestUtils.createTestCue()
        newCueViewModel.submitCue(cue)
        verify(exactly = 1) { cueRepository.submitCue(cue) }
    }
}