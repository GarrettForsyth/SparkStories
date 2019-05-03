package com.example.android.sparkstories.repos.cue

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.api.ApiSuccessResponse
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.model.Status
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.ui.util.QueryParameters
import timber.log.Timber
import javax.inject.Inject

class CueBoundaryCallback (
    var queryParameters: QueryParameters,
    val cueRepository: CueRepository,
    val sparkStoriesService: SparkStoriesService
) : PagedList.BoundaryCallback<Cue>() {

    private var isLoading = false

    override fun onZeroItemsLoaded() {
        Timber.d("PaginationTest onzeroitemsloaded called")
//        val cues = sparkStoriesService.getCues(queryParameters)
//        cues.observeForever {
//            if (it.status == Status.SUCCESS) {
//                Timber.d("mytest successful cue update called")
//                cuesRepository.submitCues(it.data!!)
//            }else {
//                Timber.d("mytest loading or error update")
//            }
//        }
    }

    /**
     * TODO:
     *  1) Possibly isLoading should be made atomic and this block should be synchronized
     *  2) Also, maybe this is a memory leak since the response object returned will be
     *  observed and therefor maybe not garbage collected even though it is replaced by
     *  the response of the next query.
     */
    override fun onItemAtEndLoaded(itemAtEnd: Cue) {
        Timber.d("PaginationTest onItemAtEndLoaded called end item: $itemAtEnd")
        if (!isLoading) {
            Timber.d("PaginationTest onItemAtEndLoaded called. network free --> creating request")
            isLoading = true
//            val response = sparkStoriesService.getCues(queryParameters)
            val response = cueRepository.cues(queryParameters)
            response.observeForever { response ->
                response?.let {
                    Timber.d("PaginationTest onItemAtEndLoaded called. request received")
                    isLoading = false
                }

            }
        }else {
            Timber.d("PaginationTest boundarycallback is already sending a request")
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Cue) {
        Timber.d("PaginationTest onItemAtFrontLoaded front item: $itemAtFront")
        // Ignored since we only ever append to what's in the DB
    }
}