package com.example.android.writeitsayithearit.repos

import androidx.lifecycle.LiveData
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.vo.Cue
import javax.inject.Inject

class CueRepository @Inject constructor(
        val appExecutors: AppExecutors,
        val cueDao: CueDao,
        val service: WriteItSayItHearItService
) {

    fun cues(): LiveData<List<Cue>> = cueDao.cues()

    fun submitCue(cue: Cue) {
        appExecutors.diskIO().execute { cueDao.insert(cue) }
    }

    fun cue(id: Int) = cueDao.cue(id)

}