package com.example.android.writeitsayithearit.ui.cues

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.model.cue.CueTextField
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.cue.Cue
import timber.log.Timber
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
            Timber.d("--> $cue")
            submitCue(cue)
            _shouldNavigateToCues.value = Event(true)
        } else {
            _invalidCueSnackBar.value = Event(true)
        }
    }

    private fun submitCue(cue: Cue) { cueRepository.submitCue(cue) }

    companion object {
        const val PREFERENCE_AUTHOR = "preference_author"
        const val DEFAULT_AUTHOR = "Unknown"
    }

}