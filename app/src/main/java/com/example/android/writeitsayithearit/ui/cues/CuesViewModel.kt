package com.example.android.writeitsayithearit.ui.cues

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.vo.Cue
import javax.inject.Inject

class CuesViewModel @Inject constructor(val cueRepository: CueRepository) : ViewModel() {

    val cues: LiveData<List<Cue>> = cueRepository.cues()

}