package com.example.android.writeitsayithearit.repos

import androidx.lifecycle.LiveData
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.SortOrder
import javax.inject.Inject

class CueRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val cueDao: CueDao,
        private val service: WriteItSayItHearItService,
        private val wshQueryHelper: WSHQueryHelper
) {

    fun cues(filterText: String, sortOrder: SortOrder): LiveData<List<Cue>> {
        return cueDao.cues(wshQueryHelper.cues(filterText, sortOrder))
    }

    fun submitCue(cue: Cue) {
        appExecutors.diskIO().execute {
            cueDao.insert(cue)
        }
    }

    fun cue(id: Int) = cueDao.cue(id)

}