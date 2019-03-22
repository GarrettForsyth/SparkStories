package com.example.android.sparkstories.repos

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.api.WriteItSayItHearItService
import com.example.android.sparkstories.data.CueDao
import com.example.android.sparkstories.repos.utils.WSHQueryHelper
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.ui.util.QueryParameters
import javax.inject.Inject

class CueRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val cueDao: CueDao,
        private val service: WriteItSayItHearItService,
        private val wshQueryHelper: WSHQueryHelper
) {

    fun cues(queryParameters: QueryParameters): LiveData<PagedList<Cue>> {
        val factory = cueDao.cues(wshQueryHelper.cues(
            queryParameters
        ))
        return LivePagedListBuilder<Int, Cue>(factory, getCuePagedListConfig()).build()
    }

    fun submitCue(cue: Cue) {
        appExecutors.diskIO().execute {
            cueDao.insert(cue)
        }
    }

    fun updateCue(cue: Cue) {
        appExecutors.diskIO().execute {
            cueDao.update(cue)
        }
    }

    fun cue(id: Int) = cueDao.cue(id)

    companion object {
        private const val PAGE_SIZE = 45
        private const val PREFETCH_DISTANCE = 90
        private const val INITIAL_LOAD_HINT = 90

        private fun getCuePagedListConfig(): PagedList.Config {
            return PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setInitialLoadSizeHint(INITIAL_LOAD_HINT)
                .build()
        }
    }

}