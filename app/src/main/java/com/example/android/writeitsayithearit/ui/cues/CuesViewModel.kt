package com.example.android.writeitsayithearit.ui.cues

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.ui.util.ObservedMutableLiveData
import com.example.android.writeitsayithearit.ui.util.QueryParameters
import javax.inject.Inject

class CuesViewModel @Inject constructor(private val cueRepository: CueRepository) : ViewModel() {

    private val _queryParameters = ObservedMutableLiveData<QueryParameters>()
    val queryParameters = QueryParameters(-1, "", SortOrder.NEW)

    private val _cues = Transformations.switchMap(_queryParameters) { parameters ->
        cues(parameters)
    }
    val cues: LiveData<PagedList<Cue>> = _cues

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
        _queryParameters.postValue(queryParameters)
        _hasResultsStatus.value = Event(false)
    }

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    fun onClickCue(cueId: Int) {
        _cueClicked.value = Event(cueId)
    }

    fun onClickNewCue() {
        _newCueFabClick.value = Event(true)
    }

    private fun cues(queryParameters: QueryParameters): LiveData<PagedList<Cue>> {
        return cueRepository.cues(queryParameters)
    }

}