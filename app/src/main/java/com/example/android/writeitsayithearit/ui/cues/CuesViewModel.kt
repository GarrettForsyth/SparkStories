package com.example.android.writeitsayithearit.ui.cues

import androidx.lifecycle.*
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.viewmodel.HasFilterQuery
import com.example.android.writeitsayithearit.viewmodel.HasSortOrderSpinner
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.SortOrder
import javax.inject.Inject

class CuesViewModel @Inject constructor(private val cueRepository: CueRepository) : ViewModel(), HasFilterQuery,
    HasSortOrderSpinner {

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
        cues(_filterQuery.value!!, _sortOrder.value!!)
    }

    init {
        filterQuery("")
        _cues.addSource(_filteredCues, { _cues.value = _filteredCues.value!! })
        _cues.addSource(_sortedCues, { _cues.value = _sortedCues.value!! })
    }

    override fun filterQuery(filterText: String) = _filterQuery.postValue(filterText)

    override fun sortOrder(sortOrder: SortOrder) = _sortOrder.postValue(sortOrder)

    private fun cues(filterText: String, sortOrder: SortOrder): LiveData<List<Cue>> {
        return cueRepository.cues(filterText, sortOrder)
    }

}