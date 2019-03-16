package com.example.android.writeitsayithearit.repos

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
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val commentDao: CommentDao
) {

    fun comments(storyId: Int): LiveData<PagedList<Comment>>{
        val factory =  commentDao.comments(storyId)
        return LivePagedListBuilder<Int, Comment>(factory, getCommentPagedListConfig()).build()
    }

    companion object {
        private const val PAGE_SIZE = 45
        private const val PREFETCH_DISTANCE = 90
        private const val INITIAL_LOAD_HINT = 90

        private fun getCommentPagedListConfig(): PagedList.Config {
            return PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setInitialLoadSizeHint(INITIAL_LOAD_HINT)
                .build()
        }
    }
}

