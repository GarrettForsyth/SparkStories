package com.example.android.sparkstories.repos.cue

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.api.ApiResponse
import com.example.android.sparkstories.data.local.CueDao
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.Status
import com.example.android.sparkstories.repos.utils.SparkStoriesQueryHelper
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.repos.utils.NetworkBoundResource
import com.example.android.sparkstories.ui.util.QueryParameters
import timber.log.Timber
import javax.inject.Inject

class CueRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val cueDao: CueDao,
    private val sparkStoriesService: SparkStoriesService,
    private val wshQueryHelper: SparkStoriesQueryHelper
) {

    fun cues(queryParameters: QueryParameters): LiveData<Resource<PagedList<Cue>>> {
        return object : NetworkBoundResource<PagedList<Cue>, List<Cue>>(appExecutors) {

            override fun loadFromDb(): LiveData<PagedList<Cue>> {
                Timber.d("mytest loadFromDbCalled")

                val factory = cueDao.cues(
                    wshQueryHelper.cues(
                        queryParameters
                    )
                )

                val cueBoundaryCallback = CueBoundaryCallback(
                    queryParameters,
                    this@CueRepository,
                    sparkStoriesService
                )

                return LivePagedListBuilder<Int, Cue>(
                    factory,
                    getCuePagedListConfig()
                ).setBoundaryCallback(cueBoundaryCallback)
                    .build()
            }

            // Always fetch from the network to get most recent data
            override fun shouldFetch(data: PagedList<Cue>?): Boolean {
                Timber.d("mytest shouldFetchCalled")
                return true
            }

            override fun createCall(): LiveData<ApiResponse<List<Cue>>> {
                Timber.d("mytest createCall called ")
                return sparkStoriesService.getCues(queryParameters)
            }

            override fun saveCallResult(item: List<Cue>) {
                Timber.d("mytest saveCallResult called")
                cueDao.insert(item)
            }

        }.asLiveData()

    }

    fun submitCue(cue: Cue): LiveData<Resource<Boolean>> {
        return sparkStoriesService.submitCue(cue)
    }

    fun submitCues(cues: List<Cue>) {
        appExecutors.diskIO().execute {
            cueDao.insert(cues)
        }
    }

    fun updateCue(cue: Cue) {
        appExecutors.diskIO().execute {
            cueDao.update(cue)
        }
    }

    fun cue(id: String): LiveData<Resource<Cue>> {
        return object : NetworkBoundResource<Cue, Cue>(appExecutors) {

            override fun loadFromDb(): LiveData<Cue> = cueDao.cue(id)

            // Only fetch if not in database alraedy
            override fun shouldFetch(data: Cue?) : Boolean {
                if (data == null) {
                    Timber.d("CueTest no cue in local database with id $id.")
                }else {
                    Timber.d("CueTest local cue with $id fetched: $data")
                }
                return data == null
            }

            override fun createCall() = sparkStoriesService.getCue(id)

            override fun saveCallResult(item: Cue) = cueDao.insert(item)

        }.asLiveData()
    }

    companion object {
        private const val PAGE_SIZE = 30
        private const val PREFETCH_DISTANCE = 60

        private fun getCuePagedListConfig(): PagedList.Config {
            return PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build()
        }
    }

}