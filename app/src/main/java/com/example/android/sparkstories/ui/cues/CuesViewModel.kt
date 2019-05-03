package com.example.android.sparkstories.ui.cues

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.android.sparkstories.di.AppInjector.init
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.ui.util.events.Event
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.ui.util.ObservedMutableLiveData
import com.example.android.sparkstories.ui.util.QueryParameters
import timber.log.Timber
import javax.inject.Inject

class CuesViewModel @Inject constructor(private val cueRepository: CueRepository) : ViewModel() {

    private val _hasResultsStatus = MutableLiveData<Event<Boolean>>()
    val hasResultsStatus: LiveData<Event<Boolean>>
        get() = _hasResultsStatus

    private val _cueClicked = MutableLiveData<Event<String>>()
    val cueClicked: LiveData<Event<String>>
        get() = _cueClicked

    private val _newCueFabClick = MutableLiveData<Event<Boolean>>()
    val newCueFabClick: LiveData<Event<Boolean>>
        get() = _newCueFabClick

    private val _queryParameters = ObservedMutableLiveData<QueryParameters>()
    val queryParameters = QueryParameters()

    private val _cues = Transformations.switchMap(_queryParameters) { parameters ->
        Timber.d("mytest query parameters have changed: ${queryParameters.sortOrder}, ${queryParameters.filterString}")
        cues(parameters)
    }
    val cues: LiveData<Resource<PagedList<Cue>>> = _cues

    init {
        Timber.d("mytest INTIALIZING CUES VIEWMODEL..")
        _queryParameters.postValue(queryParameters)
        _hasResultsStatus.value = Event(false)

    }

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    fun onClickCue(cueId: String) {
        _cueClicked.value = Event(cueId)
    }

    fun onClickNewCue() {
        _newCueFabClick.value = Event(true)
    }

    private fun cues(queryParameters: QueryParameters): LiveData<Resource<PagedList<Cue>>> {
        return cueRepository.cues(queryParameters)
    }

}