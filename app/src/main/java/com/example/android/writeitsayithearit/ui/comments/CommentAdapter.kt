package com.example.android.writeitsayithearit.ui.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.CommentListItemBinding
import com.example.android.writeitsayithearit.model.comment.Comment
import com.example.android.writeitsayithearit.ui.common.DataBoundListAdapter

class CommentAdapter(
    private val viewModel: CommentsViewModel,
    appExecutors: AppExecutors
) : DataBoundListAdapter<Comment, com.example.android.writeitsayithearit.databinding.CommentListItemBinding>(
    appExecutors = appExecutors,
    diffCallback = Comment.commentDiffCallback
) {
    override fun createBinding(parent: ViewGroup): CommentListItemBinding {
        val binding :CommentListItemBinding = DataBindingUtil.inflate(
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
    }
}
