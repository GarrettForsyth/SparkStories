package com.example.android.sparkstories.ui.comments

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.R
import com.example.android.sparkstories.databinding.CommentListItemBinding
import com.example.android.sparkstories.model.comment.Comment
import com.example.android.sparkstories.ui.common.DataBoundListAdapter
import timber.log.Timber

class CommentAdapter(
    private val owner: LifecycleOwner,
    private val viewModel: CommentsViewModel,
    private val appExecutors: AppExecutors
) : DataBoundListAdapter<Comment, com.example.android.sparkstories.databinding.CommentListItemBinding>(
    appExecutors = appExecutors,
    diffCallback = Comment.commentDiffCallback
) {
    override fun createBinding(parent: ViewGroup): CommentListItemBinding {
        val binding: CommentListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.comment_list_item,
            parent,
            false
        )
        binding.viewmodel = viewModel
        return binding
    }

    override fun bind(binding: CommentListItemBinding, item: Comment) {
        binding.comment = item
        binding.viewmodel = viewModel


        Timber.d("mytest onbind with $item")

        val adapter = CommentAdapter(owner, viewModel, appExecutors)
        binding.listAdapter = adapter
        viewModel.childComments(item.id).observe(owner, Observer { childComments ->
            childComments?.let {
                adapter.submitList(childComments)
            }
        })

    }
}
