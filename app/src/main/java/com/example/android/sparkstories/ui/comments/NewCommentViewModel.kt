package com.example.android.sparkstories.ui.comments

import android.content.SharedPreferences
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.sparkstories.model.comment.Comment
import com.example.android.sparkstories.model.comment.CommentTextField
import com.example.android.sparkstories.repos.comment.CommentRepository
import com.example.android.sparkstories.ui.stories.NewStoryViewModel.Companion.DEFAULT_AUTHOR
import com.example.android.sparkstories.ui.stories.NewStoryViewModel.Companion.PREFERENCE_AUTHOR
import com.example.android.sparkstories.ui.util.events.Event
import java.util.*
import javax.inject.Inject

class NewCommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository
) : ViewModel() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    var commentTextField = CommentTextField()

    private val _storyId = MutableLiveData<Int>()
    val storyId: LiveData<Int>
        get() = _storyId


    private val parentCommentId = MutableLiveData<Int>()
    private val _parentComment = Transformations.switchMap(parentCommentId) { id -> commentRepository.comment(id) }
    val parentComment: LiveData<Comment>
        get() = _parentComment

    private val _topMenuStatus = MutableLiveData<Event<Boolean>>()
    val topMenuStatus: LiveData<Event<Boolean>>
        get() = _topMenuStatus

    private val _invalidCommentSnackbar = MutableLiveData<Event<Boolean>>()
    val invalidCommentSnackbar: LiveData<Event<Boolean>>
        get() = _invalidCommentSnackbar

    private val _shouldNavigateToComments = MutableLiveData<Event<Boolean>>()
    val shouldNavigateToComments: LiveData<Event<Boolean>>
        get() = _shouldNavigateToComments

    private val _inPreviewMode = MutableLiveData<Event<Boolean>>()
    val inPreviewMode: LiveData<Event<Boolean>>
        get() = _inPreviewMode

    private val _showParentComment = MutableLiveData<Event<Boolean>>()
    val showParentComment: LiveData<Event<Boolean>>
        get() = _showParentComment

    init {
        _topMenuStatus.value = Event(true)
        _inPreviewMode.value = Event(false)
    }


    fun toggleTopMenuDoubleClickListener(): GestureDetector.SimpleOnGestureListener {
        return object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                onToggleMenu()
                return super.onDoubleTap(e)
            }
        }
    }

    fun setStoryId(storyId: Int) { _storyId.value = storyId }

    fun getParentComment(parentCommentId: Int) {
        this.parentCommentId.value = parentCommentId
    }

    fun onClickShowParentComment() {
        _showParentComment.value = Event(true)
    }

    fun onTogglePreviewMode() {
        _inPreviewMode.value = Event(!_inPreviewMode.value!!.peekContent())
    }

    fun onToggleMenu() {
        _topMenuStatus.value = Event(!_topMenuStatus.value!!.peekContent())
    }

    fun onClickSubmit() {
        if (commentTextField.isValid()) {
            val author = sharedPreferences.getString(PREFERENCE_AUTHOR, DEFAULT_AUTHOR)!!
            val comment = Comment(
                text = commentTextField.text,
                author = author,
                storyId = storyId.value!!,
                parentId = parentComment.value?.id ?: -1,
                depth = (parentComment.value?.depth ?: -1) + 1,
                creationDate = Calendar.getInstance().timeInMillis,
                rating = 0
            )
            println("--> $comment")
            commentRepository.submitComment(comment)
            _shouldNavigateToComments.value = Event(true)
        } else {
            _invalidCommentSnackbar.value = Event(true)
        }
    }
}
