package com.example.android.writeitsayithearit.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.StoryListItemBinding
import com.example.android.writeitsayithearit.ui.adapters.vh.StoryViewHolder
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.vo.Story

class StoryAdapter(private val viewModel: StoriesViewModel) : RecyclerView.Adapter<StoryViewHolder>() {

    private var stories: List<Story>?  = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {

        val binding : StoryListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.story_list_item,
                parent,
                false
        )
        binding.viewmodel = viewModel
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories!!.get(position)
        holder.bind(story)
    }

    override fun getItemCount() = stories?.size ?: 0

    fun setList(stories: List<Story>) {
        this.stories = stories
    }

}
