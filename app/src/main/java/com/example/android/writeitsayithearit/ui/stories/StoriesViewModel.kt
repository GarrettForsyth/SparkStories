package com.example.android.writeitsayithearit.ui.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.vo.Story
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
        private val storyRepository: StoryRepository
): ViewModel() {

    val stories: LiveData<List<Story>> = storyRepository.stories()

}