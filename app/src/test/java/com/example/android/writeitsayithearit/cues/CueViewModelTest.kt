package com.example.android.writeitsayithearit.cues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.TestUtils.createTestCue
import com.example.android.writeitsayithearit.test.asLiveData
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.cues.CueViewModel
import com.example.android.writeitsayithearit.ui.stories.StoryViewModel
import com.example.android.writeitsayithearit.util.MockUtils.mockObserverFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class CueViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val storyRepository: StoryRepository = mockk(relaxed = true)
    private val cueRepository: CueRepository = mockk(relaxed = true)

    private lateinit var cueViewModel: CueViewModel

    @Before
    fun init() {
        cueViewModel = CueViewModel(storyRepository, cueRepository)
        mockObserverFor(cueViewModel.cue)
    }

    @Test
    fun getCue() {
        // mock response from repository
        val id = 0
        val cue = createTestCue()
        every { cueRepository.cue(id) } returns cue.asLiveData()

        // call getCue
        cueViewModel.getCue(id)

        // assert repository is called and the value returned is exposed
        verify { cueRepository.cue(id) }
        assertEquals(cueViewModel.cue.getValueBlocking(), cue)
    }

    @Test
    fun onNewStoryButtonClick(){
        cueViewModel.onNewStoryButtonClick()
        assertTrue(cueViewModel.newStoryButtonClick.getValueBlocking().peekContent())
    }

}
