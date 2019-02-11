package com.example.android.writeitsayithearit.ui.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.databinding.StoryListItemBinding
import com.example.android.writeitsayithearit.ui.adapters.ClickListener
import com.example.android.writeitsayithearit.vo.Story

class StoryViewHolder (
        private val binding : StoryListItemBinding,
        private val clickListener: ClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener { view ->
            clickListener.onItemClick(view, adapterPosition)
        }
    }

    fun bind(story: Story) {
        binding.story = story
    }

}
