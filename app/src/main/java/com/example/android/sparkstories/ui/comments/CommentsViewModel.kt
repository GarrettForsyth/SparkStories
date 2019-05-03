package com.example.android.sparkstories.ui.comments

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.android.sparkstories.model.comment.Comment
import com.example.android.sparkstories.repos.comment.CommentRepository
import com.example.android.sparkstories.ui.util.ObservedMutableLiveData
import com.example.android.sparkstories.ui.util.QueryParameters
import com.example.android.sparkstories.ui.util.events.Event
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val commentRepository: CommentRepository
): ViewModel() {

    private val _queryParameters = ObservedMutableLiveData<QueryParameters>()
    val queryParameters = QueryParameters()

    private val _comments = Transformations.switchMap(_queryParameters) { parameters ->
        comments(parameters)
    }
    val comments: LiveData<PagedList<Comment>> = _comments

    init {
        _queryParameters.postValue(queryParameters)
    }

    private val _hasResultsStatus = MutableLiveData<Event<Boolean>>()
    val hasResultsStatus: LiveData<Event<Boolean>>
        get() = _hasResultsStatus

    private val _shouldNavigateToNewComment = MutableLiveData<Event<Int>>()
    val shouldNavigateToNewComment: LiveData<Event<Int>>
        get() = _shouldNavigateToNewComment

    fun setHasResults(hasResults: Boolean) {
        _hasResultsStatus.value = Event(hasResults)
    }

    fun onClickNewComment(parentId: Int) {
        _shouldNavigateToNewComment.value = Event(parentId)
    }

    fun onClickLikeComment(comment: Comment) {
        val updatedComment = comment.copy().apply { rating ++}
        commentRepository.update(updatedComment)
    }

    fun childComments(id: Int): LiveData<PagedList<Comment>>  {
        return commentRepository.childComments(id)
    }

    private fun comments(queryParameters: QueryParameters): LiveData<PagedList<Comment>> {
        return commentRepository.comments(queryParameters)
    }
}
