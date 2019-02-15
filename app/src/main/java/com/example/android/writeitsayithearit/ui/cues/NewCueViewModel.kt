package com.example.android.writeitsayithearit.ui.cues

import androidx.lifecycle.ViewModel
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.vo.Cue
import javax.inject.Inject

class NewCueViewModel @Inject constructor(
    private val cueRepository: CueRepository
): ViewModel() {

    fun submitCue(cue: Cue) { cueRepository.submitCue(cue) }
}