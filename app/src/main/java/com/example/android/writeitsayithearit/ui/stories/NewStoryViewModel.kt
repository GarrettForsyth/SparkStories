package com.example.android.writeitsayithearit.ui.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.model.story.StoryTextField
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story
import javax.inject.Inject

@OpenForTesting
class NewStoryViewModel @Inject constructor(
    private val cueRepository: CueRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    var storyTextField: StoryTextField =
        StoryTextField()

    private val _invalidStorySnackBar = MutableLiveData<Event<Boolean>>()
    val invalidStorySnackBar: LiveData<Event<Boolean>>
        get() = _invalidStorySnackBar

    private val _newStoryInfoDialog = MutableLiveData<Event<Boolean>>()
    val newStoryInfoDialog: LiveData<Event<Boolean>>
        get() = _newStoryInfoDialog

    private val _confirmSubmissionDialog = MutableLiveData<Event<Boolean>>()
    val confirmSubmissionDialog: LiveData<Event<Boolean>>
        get() = _confirmSubmissionDialog

    private val _navigateToStoriesFragment = MutableLiveData<Event<Boolean>>()
    val shouldNavigateToStories: LiveData<Event<Boolean>>
        get() = _navigateToStoriesFragment

    private val cueId = MutableLiveData<Int>()
    private val _cue = Transformations.switchMap(cueId) { id -> cueRepository.cue(id) }
    val cue: LiveData<Cue>
        get() = _cue

    private val _topMenuStatus = MutableLiveData<Event<Boolean>>()
    val topMenuStatus: LiveData<Event<Boolean>>
        get() = _topMenuStatus

    private val _inPreviewMode = MutableLiveData<Event<Boolean>>()
    val inPreviewMode: LiveData<Event<Boolean>>
        get() = _inPreviewMode

    init {
        _topMenuStatus.value = Event(true)
        _inPreviewMode.value = Event(false)
    }

    fun getCue(id: Int) = cueId.postValue(id)

    fun onToggleMenu() {
       _topMenuStatus.value = Event(!_topMenuStatus.value!!.peekContent())
    }

    fun onTogglePreviewMode() {
        _inPreviewMode.value = Event(!_inPreviewMode.value!!.peekContent())
    }

    fun onClickInfo(){
        _newStoryInfoDialog.value = Event(true)
    }

    fun onClickSubmit() {
        _confirmSubmissionDialog.value = Event(true)
    }

    fun onConfirmSubmission() {
        if (storyTextField.isValid()) {
            val story = Story(storyTextField.text, cueId.value!!)
            submitStory(story)
            _navigateToStoriesFragment.value = Event(true)
        } else {
            _invalidStorySnackBar.value = Event(true)
        }
    }

    fun submitStory(story: Story) = storyRepository.submitStory(story)
}