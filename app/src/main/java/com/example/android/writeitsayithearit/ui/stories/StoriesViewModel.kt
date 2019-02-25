package com.example.android.writeitsayithearit.ui.stories

import androidx.lifecycle.*
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.viewmodel.HasSortOrderSpinner
import com.example.android.writeitsayithearit.vo.SortOrder
import com.example.android.writeitsayithearit.vo.Story
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() , HasSortOrderSpinner {

    var filterQuery
        get() = _filterQuery.value ?: ""
        set(value) {
            _filterQuery.value = value
        }

    private val _stories = MediatorLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    private val _filterQuery = MutableLiveData<String>()
    private val _filteredStories = Transformations.switchMap(_filterQuery) { query ->
        if (_sortOrder.value != null) {
            stories(query, _sortOrder.value!!)
        } else {
            stories(filterText = query, sortOrder = SortOrder.NEW)
        }
    }

    private val _hasResultsStatus = MutableLiveData<Event<Boolean>>()
    val hasResultsStatus: LiveData<Event<Boolean>>
        get() = _hasResultsStatus

    private val _storyClicked = MutableLiveData<Event<Int>>()
    val storyClicked: LiveData<Event<Int>>
        get() = _storyClicked

    private val _sortOrder = MutableLiveData<SortOrder>()
    private val _sortedStories = Transformations.switchMap(_sortOrder) { sortOrder ->
        stories(_filterQuery.value ?: "", _sortOrder.value!!)
    }

    init {
        _stories.addSource(_filteredStories, { _stories.value = _filteredStories.value!! })
        _stories.addSource(_sortedStories, { _stories.value = _sortedStories.value!! })
        _hasResultsStatus.value = Event(false)
    }

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    fun onClickStory(cueId: Int) {
        _storyClicked.value = Event(cueId)
    }

    override fun sortOrder(sortOrder: SortOrder) = _sortOrder.postValue(sortOrder)

    private fun stories(filterText: String, sortOrder: SortOrder): LiveData<List<Story>> {
        return storyRepository.stories(filterText, sortOrder)
    }

}