package com.example.android.writeitsayithearit.ui.comments

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.comment.Comment
import com.example.android.writeitsayithearit.repos.CommentRepository
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper.stories
import com.example.android.writeitsayithearit.ui.util.events.Event
import org.junit.internal.requests.SortingRequest
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val commentRepository: CommentRepository
): ViewModel() {

    private val _storyId = MutableLiveData<Int>()
    val storyId = _storyId

    private val _sortOrder = MutableLiveData<SortOrder>()

//    private val _comments = MediatorLiveData<List<Comment>>()
    val comments = Transformations.switchMap(_storyId) { id -> comments(id)}



    private val _hasResultsStatus = MutableLiveData<Event<Boolean>>()
    val hasResultsStatus: LiveData<Event<Boolean>>
        get() = _hasResultsStatus

    fun storyId(id: Int) = _storyId.postValue(id)

    fun sortOrder(sortOrder: SortOrder) = _sortOrder.postValue(sortOrder)

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    private fun comments(
        storyId :Int
    ): LiveData<PagedList<Comment>> {
        return commentRepository.comments(storyId)!!
    }
}
