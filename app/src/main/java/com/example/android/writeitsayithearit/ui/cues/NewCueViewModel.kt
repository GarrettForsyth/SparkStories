package com.example.android.writeitsayithearit.ui.cues

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.model.cue.CueTextField
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.cue.Cue
import javax.inject.Inject

class NewCueViewModel @Inject constructor(
    private val cueRepository: CueRepository
): ViewModel() {

    var cueTextField: CueTextField =
        CueTextField()

    private val _shouldNavigateToCues= MutableLiveData<Event<Boolean>>()
    val shouldNavigateToCues: LiveData<Event<Boolean>>
        get() = _shouldNavigateToCues

    private val _invalidCueSnackBar = MutableLiveData<Event<Boolean>>()
    val invalidCueSnackBar: LiveData<Event<Boolean>>
        get() = _invalidCueSnackBar

    fun onClickSubmitCue() {
        if (cueTextField.isValid()) {
            val cue = Cue(cueTextField.text)
            submitCue(cue)
            _shouldNavigateToCues.value = Event(true)
        } else {
            _invalidCueSnackBar.value = Event(true)
        }
    }

    private fun submitCue(cue: Cue) { cueRepository.submitCue(cue) }
}