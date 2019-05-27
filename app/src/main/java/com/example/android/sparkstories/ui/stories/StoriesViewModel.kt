package com.example.android.sparkstories.ui.stories

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.repos.story.StoryRepository
import com.example.android.sparkstories.ui.util.events.Event
import com.example.android.sparkstories.model.story.Story
import com.example.android.sparkstories.ui.util.ObservedMutableLiveData
import com.example.android.sparkstories.ui.util.QueryParameters
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _queryParameters = ObservedMutableLiveData<QueryParameters>()
    val queryParameters = QueryParameters()

    private val _stories = Transformations.switchMap(_queryParameters) { parameters ->
        stories(parameters)
    }
    val stories: LiveData<Resource<PagedList<Story>>> = _stories

    init {
        _queryParameters.postValue(queryParameters)
    }

    private val _hasResultsStatus = MutableLiveData<Event<Boolean>>()
    val hasResultsStatus: LiveData<Event<Boolean>>
        get() = _hasResultsStatus

    private val _storyClicked = MutableLiveData<Event<String>>()
    val storyClicked: LiveData<Event<String>>
        get() = _storyClicked

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    fun onClickStory(storyId: String) {
        _storyClicked.value = Event(storyId)
    }

    private fun stories(parameters: QueryParameters): LiveData<Resource<PagedList<Story>>> {
        return storyRepository.stories(parameters)
    }

}