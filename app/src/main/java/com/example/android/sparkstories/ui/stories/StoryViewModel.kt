package com.example.android.sparkstories.ui.stories

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.lifecycle.*
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.story.Story
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.repos.story.StoryRepository
import com.example.android.sparkstories.ui.util.events.Event
import timber.log.Timber
import javax.inject.Inject

class StoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val cueRepository: CueRepository
): ViewModel() {


    private val storyId = MutableLiveData<String>()
    private val _story = Transformations.switchMap(storyId) { id -> storyRepository.story(id) }
    val story: LiveData<Story>
        get() =  _story

    private val _cue = Transformations.switchMap(_story ) { story -> cueRepository.cue(story.cueId) }
    val cue: LiveData<Resource<Cue>>
        get() =  _cue

    private val _topMenuStatus = MutableLiveData<Boolean>()
    val topMenuStatus: LiveData<Boolean>
        get() =  _topMenuStatus

    private val _cueDialog = MutableLiveData<Event<Boolean>>()
    val cueDialog: LiveData<Event<Boolean>>
        get() = _cueDialog

    private val _viewCommentsEvent = MutableLiveData<Event<Boolean>>()
    val viewCommentsEvent: LiveData<Event<Boolean>>
        get() = _viewCommentsEvent


    init {
        _topMenuStatus.value = true
    }


    fun getStory(id: String) {
        storyId.value = id
    }

    fun toggleTopMenuDoubleClickListener(): GestureDetector.SimpleOnGestureListener {
        Timber.d("mytest creating doubleclick listener")
        return object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?) = true

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                Timber.d("mytest double clicked!")
                onToggleMenu()
                return super.onDoubleTap(e)
            }
        }
    }

    fun onToggleMenu() {
        _topMenuStatus.value = !_topMenuStatus.value!!
    }

    fun onLikeStoryClick() {
        val updatedStory = story.value!!.copy().apply{ rating++}
        storyRepository.update(updatedStory)
    }

    fun onShowCueClick() {
        _cueDialog.value = Event(true)
    }

    fun onViewCommentsClick() {
        _viewCommentsEvent.value = Event(true)
    }

}