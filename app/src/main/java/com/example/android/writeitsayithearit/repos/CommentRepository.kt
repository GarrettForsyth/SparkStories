package com.example.android.writeitsayithearit.repos

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CommentDao
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.comment.Comment
import com.example.android.writeitsayithearit.model.story.Story
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.ui.util.QueryParameters
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val commentDao: CommentDao,
    private val wshQueryHelper: WSHQueryHelper
) {

    fun submitComment(comment: Comment) {
        appExecutors.diskIO().execute { commentDao.insert(comment) }
    }

    fun comment(commentId: Int): LiveData<Comment> = commentDao.comment(commentId)

    fun comments(queryParameters: QueryParameters): LiveData<PagedList<Comment>>{
        val factory =  commentDao.comments(
            wshQueryHelper.comments(queryParameters))
        return LivePagedListBuilder<Int, Comment>(factory, getCommentPagedListConfig()).build()
    }

    fun childComments(id: Int): LiveData<PagedList<Comment>> {
        val queryParameters = QueryParameters(_filterParentCommentId = id)
        val factory =  commentDao.comments(wshQueryHelper.comments(queryParameters))
        return LivePagedListBuilder<Int, Comment>(factory, getChildCommentPagedListConfig()).build()
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

