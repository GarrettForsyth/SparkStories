package com.example.android.writeitsayithearit.ui

import androidx.lifecycle.ViewModel
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.vo.Cue
import javax.inject.Inject

class NewCueViewModel @Inject constructor(val cueRepository: CueRepository): ViewModel() {

    fun submitCue(cue: Cue) { cueRepository.submitCue(cue) }
}