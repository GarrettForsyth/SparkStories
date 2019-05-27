package com.example.android.sparkstories.repos.story

import androidx.paging.PagedList
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.story.Story
import com.example.android.sparkstories.ui.util.QueryParameters
import timber.log.Timber

class StoryBoundaryCallback (
    var queryParameters: QueryParameters,
    val storyRepository: StoryRepository,
    val sparkStoriesService: SparkStoriesService
) : PagedList.BoundaryCallback<Story>() {

    private var isLoading = false

    override fun onZeroItemsLoaded() {
        Timber.d("PaginationTest onzeroitemsloaded called")
    }

    /**
     * TODO:
     *  1) Possibly isLoading should be made atomic and this block should be synchronized
     *  2) Also, maybe this is a memory leak since the response object returned will be
     *  observed and therefor maybe not garbage collected even though it is replaced by
     *  the response of the next query.
     */
    override fun onItemAtEndLoaded(itemAtEnd: Story) {
        Timber.d("PaginationTest onItemAtEndLoaded called end item: $itemAtEnd")
        if (!isLoading) {
            Timber.d("PaginationTest onItemAtEndLoaded called. network free --> creating request")
            isLoading = true
//            val response = sparkStoriesService.getCues(queryParameters)
            val response = storyRepository.stories(queryParameters)
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

    override fun onItemAtFrontLoaded(itemAtFront: Story) {
        Timber.d("PaginationTest onItemAtFrontLoaded front item: $itemAtFront")
        // Ignored since we only ever append to what's in the DB
    }
}
