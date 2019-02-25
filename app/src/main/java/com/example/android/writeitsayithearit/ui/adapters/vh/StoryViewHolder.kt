package com.example.android.writeitsayithearit.ui.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.databinding.StoryListItemBinding
import com.example.android.writeitsayithearit.ui.adapters.ClickListener
import com.example.android.writeitsayithearit.vo.Story

class StoryViewHolder (
        private val binding : StoryListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(story: Story) {
        binding.story = story
    }

}
