package com.example.android.writeitsayithearit.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.asLiveData
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.stories.StoryViewModel
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.util.MockUtils.mockObserversFor
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
class StoryViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val storyRepository: StoryRepository = mockk(relaxed = true)
    private val cueRepository: CueRepository = mockk(relaxed = true)

    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun init() {
        storyViewModel = StoryViewModel(storyRepository, cueRepository)

        mockObserversFor(storyViewModel.story)
        mockObserversFor(storyViewModel.cue)
        mockObserversFor(storyViewModel.topMenuStatus)
    }

    @Test()
    fun getStory(){
        // Mock the story's cue
        val cueId = 12
        val cue = Cue("This is some cue", 0, 0,  cueId)
        every { cueRepository.cue(cueId) } returns cue.asLiveData()

        // Mock the story
        val storyId = 0
        val story = Story("This is the story about something.", cueId)
        every { storyRepository.story(storyId) } returns story.asLiveData()

        // Call get story
        storyViewModel.getStory(storyId)

        verify { storyRepository.story(storyId)}
        verify { cueRepository.cue(cueId) }
        assertEquals(storyViewModel.story.getValueBlocking(), story)
        assertEquals(storyViewModel.cue.getValueBlocking(), cue)
    }

    @Test
    fun toggleMenu() {
        // initial state should be true
        assertTrue(storyViewModel.topMenuStatus.getValueBlocking().peekContent())

        storyViewModel.onToggleMenu()
        assertFalse(storyViewModel.topMenuStatus.getValueBlocking().peekContent())

        storyViewModel.onToggleMenu()
        assertTrue(storyViewModel.topMenuStatus.getValueBlocking().peekContent())
    }

    @Test
    fun likeStory() {
        val story = Story("test", 14, 0, 52, 0)
        every { storyRepository.story(0) } returns story.asLiveData()
        storyViewModel.getStory(0)

        storyViewModel.onLikeStoryClick()
        val updatedStory = Story("test", 14, 0, 53, 0)
        verify { storyRepository.update(updatedStory) }
    }

}