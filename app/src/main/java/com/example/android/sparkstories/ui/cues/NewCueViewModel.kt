package com.example.android.sparkstories.ui.cues

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.model.cue.CueTextField
import com.example.android.sparkstories.ui.util.events.Event
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.ui.stories.NewStoryViewModel.Companion.DEFAULT_AUTHOR
import com.example.android.sparkstories.ui.stories.NewStoryViewModel.Companion.PREFERENCE_AUTHOR
import com.facebook.internal.Mutable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class NewCueViewModel @Inject constructor(
    private val cueRepository: CueRepository
): ViewModel() {

    @Inject lateinit var sharedPreferences: SharedPreferences
    var cueTextField: CueTextField =
        CueTextField()

    private val _shouldNavigateToCues= MutableLiveData<Event<Boolean>>()
    val shouldNavigateToCues: LiveData<Event<Boolean>>
        get() = _shouldNavigateToCues

    private val _invalidCueSnackBar = MutableLiveData<Event<Boolean>>()
    val invalidCueSnackBar: LiveData<Event<Boolean>>
        get() = _invalidCueSnackBar

    private val _newCueEditTextFocusStatus = MutableLiveData<Event<Boolean>>()
    val newCueEditTextFocusStatus: LiveData<Event<Boolean>>
        get() = _newCueEditTextFocusStatus

    private val submitCue = MutableLiveData<Cue>()
    private val _submitCueResponse = Transformations.switchMap(submitCue) {
        println(it)
        cueRepository.submitCue(it)
    }
    val submitCueResponse: LiveData<Resource<Boolean>> = _submitCueResponse

    init {
        _newCueEditTextFocusStatus.value = Event(false)
    }

    fun newCueEditTextFocusChangeListener(): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { v, hasFocus ->
            _newCueEditTextFocusStatus.value = Event(hasFocus)
        }
    }

    fun onClickSubmitCue() {
        if (cueTextField.isValid()) {
            val author = sharedPreferences.getString(PREFERENCE_AUTHOR, DEFAULT_AUTHOR)!!
            val cue = Cue(cueTextField.text, author)
            cue.id = UUID.randomUUID().toString()
            Timber.d("--> $cue")
            submitCue.postValue(cue)
        }else {
            _invalidCueSnackBar.postValue(Event(true))
        }
    }

    companion object {
        const val PREFERENCE_AUTHOR = "preference_author"
        const val DEFAULT_AUTHOR = "Unknown"
    }

}