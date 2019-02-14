package com.example.android.writeitsayithearit.ui.stories

import androidx.lifecycle.*
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.vo.SortOrder
import com.example.android.writeitsayithearit.vo.Story
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
        private val storyRepository: StoryRepository
): ViewModel() {

    private val _stories = MediatorLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    private val _filterQuery = MutableLiveData<String>()
    private val _filteredStories = Transformations.switchMap(_filterQuery) { query ->
        if (_sortOrder.value != null) {
            stories(query, _sortOrder.value!!)
        }else {
            stories(filterText = query, sortOrder = SortOrder.NEW)
        }
    }

    private val _sortOrder = MutableLiveData<SortOrder>()
    private val _sortedStories = Transformations.switchMap(_sortOrder) { sortOrder ->
        if (_filterQuery.value != null) {
            stories(_filterQuery.value!!, _sortOrder.value!!)
        }else {
            stories(filterText = "", sortOrder = sortOrder)
        }
    }

    init {
        filterQuery("")
        _stories.addSource(_filteredStories, { _stories.value = _filteredStories.value!! })
        _stories.addSource(_sortedStories, { _stories.value = _sortedStories.value!! })
    }

    fun filterQuery(filterText: String) = _filterQuery.postValue(filterText)

    fun sortOrder(sortOrder: SortOrder) = _sortOrder.postValue(sortOrder)

    private fun stories(filterText: String, sortOrder: SortOrder): LiveData<List<Story>> {
        return storyRepository.stories(filterText, sortOrder)
    }

}