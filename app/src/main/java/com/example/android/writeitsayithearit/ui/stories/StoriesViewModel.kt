package com.example.android.writeitsayithearit.ui.stories

import androidx.lifecycle.*
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.story.Story
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    var filterQuery
        get() = _filterQuery.value ?: ""
        set(value) {
            _filterQuery.value = value
        }

    private val _stories = MediatorLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    private val _sortOrder = MutableLiveData<SortOrder>()
    private val _sortedStories = Transformations.switchMap(_sortOrder) { sortOrder ->
        stories(_filterQuery.value ?: "",
            _sortOrder.value!!,
            _filterCue.value ?: -1
        )
    }

    private val _filterQuery = MutableLiveData<String>()
    private val _filteredStories = Transformations.switchMap(_filterQuery) { query ->
        stories(query,
            _sortOrder.value ?: SortOrder.NEW,
            _filterCue.value ?: -1
        )
    }

    private val _filterCue = MutableLiveData<Int>()
    private val _filteredStoriesByCue = Transformations.switchMap(_filterCue) { cueId ->
        stories(_filterQuery.value ?: "",
            _sortOrder.value ?: SortOrder.NEW,
            cueId
        )
    }

    private val _hasResultsStatus = MutableLiveData<Event<Boolean>>()
    val hasResultsStatus: LiveData<Event<Boolean>>
        get() = _hasResultsStatus

    private val _storyClicked = MutableLiveData<Event<Int>>()
    val storyClicked: LiveData<Event<Int>>
        get() = _storyClicked


    init {
        _stories.addSource(_filteredStories) { _stories.value = _filteredStories.value!! }
        _stories.addSource(_sortedStories) { _stories.value = _sortedStories.value!! }
        _stories.addSource(_filteredStoriesByCue) { _stories.value = _filteredStoriesByCue.value!! }
        _hasResultsStatus.value = Event(false)
    }

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    fun onClickStory(cueId: Int) {
        _storyClicked.value = Event(cueId)
    }

    fun sortOrder(sortOrder: SortOrder) = _sortOrder.postValue(sortOrder)

    fun filterCue(cueId: Int) = _filterCue.postValue(cueId)

    private fun stories(
        filterText: String,
        sortOrder: SortOrder,
        cueId: Int
    ): LiveData<List<Story>> {
        return storyRepository.stories(filterText, sortOrder, cueId)
    }

}