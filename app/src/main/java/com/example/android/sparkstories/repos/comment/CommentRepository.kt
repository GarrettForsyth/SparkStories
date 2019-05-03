package com.example.android.sparkstories.repos.comment

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.data.local.CommentDao
import com.example.android.sparkstories.model.comment.Comment
import com.example.android.sparkstories.repos.utils.SparkStoriesQueryHelper
import com.example.android.sparkstories.ui.util.QueryParameters
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val commentDao: CommentDao,
    private val wshQueryHelper: SparkStoriesQueryHelper
) {

    fun submitComment(comment: Comment) {
        appExecutors.diskIO().execute { commentDao.insert(comment) }
    }

    fun comment(commentId: Int): LiveData<Comment> = commentDao.comment(commentId)

    fun comments(queryParameters: QueryParameters): LiveData<PagedList<Comment>>{
        val factory =  commentDao.comments(
            wshQueryHelper.comments(queryParameters))
        return LivePagedListBuilder<Int, Comment>(factory,
            getCommentPagedListConfig()
        ).build()
    }

    fun childComments(id: Int): LiveData<PagedList<Comment>> {
        val queryParameters = QueryParameters(_filterParentCommentId = id)
        val factory =  commentDao.comments(wshQueryHelper.comments(queryParameters))
        return LivePagedListBuilder<Int, Comment>(factory,
            getChildCommentPagedListConfig()
        ).build()
    }

    fun update(comment: Comment) {
        appExecutors.diskIO().execute { commentDao.update(comment) }
    }

    companion object {
        private const val PAGE_SIZE = 45
        private const val PREFETCH_DISTANCE = 90
        private const val INITIAL_LOAD_HINT = 90

        private const val CHILD_COMMENT_PAGE_SIZE = 45
        private const val CHILD_COMMENT_PREFETCH_DISTANCE = 90
        private const val CHILD_COMMENT_INITIAL_LOAD_HINT = 90


        private fun getCommentPagedListConfig(): PagedList.Config {
            return PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setInitialLoadSizeHint(INITIAL_LOAD_HINT)
                .build()
        }

        private fun getChildCommentPagedListConfig(): PagedList.Config {
            return PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(CHILD_COMMENT_PAGE_SIZE)
                .setPrefetchDistance(CHILD_COMMENT_PREFETCH_DISTANCE)
                .setInitialLoadSizeHint(CHILD_COMMENT_INITIAL_LOAD_HINT)
                .build()
        }
    }
}

