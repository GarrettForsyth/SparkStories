package com.example.android.writeitsayithearit.ui.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.Story
import javax.inject.Inject

class NewStoryViewModel @Inject constructor(
        private val cueRepository: CueRepository,
        private val storyRepository: StoryRepository
) : ViewModel() {

    fun submitStory(story: Story) = storyRepository.submitStory(story)

    fun cue(id: Int) = cueRepository.cue(id)
}