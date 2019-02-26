package com.example.android.writeitsayithearit.ui.stories

import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.databinding.StoryListItemBinding
import com.example.android.writeitsayithearit.model.story.Story

class StoryViewHolder (
        private val binding : StoryListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(story: Story) {
        binding.story = story
    }

}
