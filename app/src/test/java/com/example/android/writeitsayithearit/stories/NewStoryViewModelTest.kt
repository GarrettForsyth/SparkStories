package com.example.android.writeitsayithearit.stories

import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class NewStoryViewModelTest {

    private val cueRepository: CueRepository = mockk(relaxed = true)
    private val storyRepository: StoryRepository = mockk(relaxed = true)

    private val newStoryViewModel = NewStoryViewModel(cueRepository, storyRepository)

    @Test
    fun submitStoryCallsRepository() {
        val story = TestUtils.createTestStory()
        newStoryViewModel.submitStory(story)
        verify(exactly = 1) { storyRepository.submitStory(story) }
    }

    @Test
    fun cueCallsRepository() {
        val id = 0
        newStoryViewModel.cue(id)
        verify(exactly = 1) { cueRepository.cue(id) }
    }


}