package com.example.android.writeitsayithearit.stories

import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.ui.cues.CuesViewModel
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class StoriesViewModelTest {

    private val storyRepository: StoryRepository = mockk(relaxed = true)
    private val storiesViewModel = StoriesViewModel(storyRepository)

    @Test
    fun storiesCallsRepository() {
        verify(exactly = 1) { storyRepository.stories() }
    }
}
