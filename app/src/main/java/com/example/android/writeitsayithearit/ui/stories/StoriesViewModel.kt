package com.example.android.writeitsayithearit.ui.stories

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.story.Story
import com.example.android.writeitsayithearit.ui.util.ObservedMutableLiveData
import com.example.android.writeitsayithearit.ui.util.QueryParameters
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _queryParameters = ObservedMutableLiveData<QueryParameters>()
    val queryParameters = QueryParameters()

    private val _stories = Transformations.switchMap(_queryParameters) { parameters ->
        stories(parameters)
    }
    val stories: LiveData<PagedList<Story>> = _stories

    init {
        _queryParameters.postValue(queryParameters)
    }

    private val _hasResultsStatus = MutableLiveData<Event<Boolean>>()
    val hasResultsStatus: LiveData<Event<Boolean>>
        get() = _hasResultsStatus

    private val _storyClicked = MutableLiveData<Event<Int>>()
    val storyClicked: LiveData<Event<Int>>
        get() = _storyClicked

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    fun onClickStory(cueId: Int) {
        _storyClicked.value = Event(cueId)
    }

    private fun stories(parameters: QueryParameters): LiveData<PagedList<Story>> {
        return storyRepository.stories(parameters)
    }

}