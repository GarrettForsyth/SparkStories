package com.example.android.writeitsayithearit.repos

import androidx.lifecycle.LiveData
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.vo.Cue
import javax.inject.Inject

class CueRepository @Inject constructor(
        val cueDao: CueDao,
        val service: WriteItSayItHearItService
) {

    fun cues(): LiveData<List<Cue>> = cueDao.cues()

}