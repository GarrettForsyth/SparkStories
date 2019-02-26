package com.example.android.writeitsayithearit.ui.cues

import androidx.lifecycle.*
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.SortOrder
import javax.inject.Inject

class CuesViewModel @Inject constructor(private val cueRepository: CueRepository) : ViewModel() {

    var filterQuery
        get() = _filterQuery.value ?: ""
        set(value) {
            _filterQuery.value = value
        }

    private val _cues = MediatorLiveData<List<Cue>>()
    val cues: LiveData<List<Cue>> = _cues

    private val _filterQuery = MutableLiveData<String>()
    private val _filteredCues = Transformations.switchMap(_filterQuery) { query ->
        if (_sortOrder.value != null) {
            cues(query, _sortOrder.value!!)
        } else {
            cues(filterText = query, sortOrder = SortOrder.NEW)
        }
    }


    private val _sortOrder = MutableLiveData<SortOrder>()
    private val _sortedCues = Transformations.switchMap(_sortOrder) { sortOrder ->
        cues(_filterQuery.value ?: "", _sortOrder.value!!)
    }

    private val _hasResultsStatus = MutableLiveData<Event<Boolean>>()
    val hasResultsStatus: LiveData<Event<Boolean>>
        get() = _hasResultsStatus

    private val _cueClicked = MutableLiveData<Event<Int>>()
    val cueClicked: LiveData<Event<Int>>
        get() = _cueClicked

    private val _newCueFabClick = MutableLiveData<Event<Boolean>>()
    val newCueFabClick: LiveData<Event<Boolean>>
        get() = _newCueFabClick


    init {
        _cues.addSource(_filteredCues, { _cues.value = _filteredCues.value!! })
        _cues.addSource(_sortedCues, { _cues.value = _sortedCues.value!! })
        _hasResultsStatus.value = Event(false)
    }

    fun sortOrder(sortOrder: SortOrder) = _sortOrder.postValue(sortOrder)

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    fun onClickCue(cueId: Int) {
        _cueClicked.value = Event(cueId)
    }

    fun onClickNewCue() {
        _newCueFabClick.value = Event(true)
    }

    private fun cues(filterText: String, sortOrder: SortOrder): LiveData<List<Cue>> {
        return cueRepository.cues(filterText, sortOrder)
    }

}