package com.example.android.writeitsayithearit.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.writeitsayithearit.vo.Cue

object TestUtils {

    private val _cues = MutableLiveData<List<Cue>>()
    val cues: LiveData<List<Cue>> = _cues

    val listOfStartingCues = listOf(
                Cue(1, "You find a rock, and realize anyone holding it must tell the truth."),
                Cue(2, "One day you look up at the sky and notice the moon is gone."),
                Cue(3, "A new submarine design reaches depths never before explored."),
                Cue(4, "A mad scientist threatens the world with his death ray."),
                Cue(5, "The largest hurricane in history is approaching."),
                Cue(6, "The allies lost the first world war."),
                Cue(7, "Dogs suddenly gain the ability to speak."),
                Cue(8, "A man drives his motorcycle across the country."),
                Cue(9, "A criminal tries to reintegrate with society."),
                Cue(10,"A writer solos a night on the town for inspiration.")
        )

    fun postCueResults() {
        _cues.value = listOfStartingCues
    }
}