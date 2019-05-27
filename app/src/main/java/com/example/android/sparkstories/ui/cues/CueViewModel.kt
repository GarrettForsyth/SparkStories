package com.example.android.sparkstories.ui.cues

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.repos.story.StoryRepository
import com.example.android.sparkstories.ui.util.events.Event
import javax.inject.Inject

class CueViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val cueRepository: CueRepository
): ViewModel()  {

    private val cueId = MutableLiveData<String>()
    private val _cue = Transformations.switchMap(cueId) { id -> cueRepository.cue(id) }
    val cue: LiveData<Resource<Cue>>
        get() = _cue

    private val _newStoryButtonClick = MutableLiveData<Event<Boolean>>()
    val newStoryButtonClick: LiveData<Event<Boolean>>
        get() = _newStoryButtonClick

    fun getCue(id: String) = cueId.postValue(id)

    fun onNewStoryButtonClick() {
        _newStoryButtonClick.value = Event(true)
    }

}
