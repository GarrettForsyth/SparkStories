package com.example.android.writeitsayithearit.ui.stories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.StoryListItemBinding
import com.example.android.writeitsayithearit.model.story.Story
import com.example.android.writeitsayithearit.ui.common.DataBoundListAdapter

class StoryAdapter(
    private val viewModel: StoriesViewModel,
    appExecutors: AppExecutors
) : DataBoundListAdapter<Story, StoryListItemBinding>(
    appExecutors = appExecutors,
    diffCallback = Story.storyDiffCallback
) {
    override fun createBinding(parent: ViewGroup): StoryListItemBinding {
        val binding : StoryListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.story_list_item,
                parent,
                false
        )
        binding.viewmodel = viewModel
        return binding
    }

    override fun bind(binding: StoryListItemBinding, item: Story) {
        binding.story = item
        binding.viewmodel = viewModel
    }

//    private var stories: List<Story>?  = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
//
//        val binding : StoryListItemBinding = DataBindingUtil.inflate(
//                LayoutInflater.from(parent.context),
//                R.layout.story_list_item,
//                parent,
//                false
//        )
//        binding.viewmodel = viewModel
//        return StoryViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
//        val story = stories!!.get(position)
//        holder.bind(story)
//    }
//
//    override fun getItemCount() = stories?.size ?: 0
//
//    fun setList(stories: List<Story>) {
//        this.stories = stories
//    }

}
