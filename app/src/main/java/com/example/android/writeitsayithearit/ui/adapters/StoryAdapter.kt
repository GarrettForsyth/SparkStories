package com.example.android.writeitsayithearit.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.StoryListItemBinding
import com.example.android.writeitsayithearit.ui.adapters.vh.StoryViewHolder
import com.example.android.writeitsayithearit.vo.Story

class StoryAdapter() : RecyclerView.Adapter<StoryViewHolder>() {

    private var stories: List<Story>?  = null
    private lateinit var clickListener: ClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {

        val binding : StoryListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.story_list_item,
                parent,
                false
        )
        return StoryViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories?.get(position)!!
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        if (stories == null) {
            return 0
        }
        else {
            return stories?.size!!
        }
    }

    fun setList(stories: List<Story>) {
        this.stories = stories
    }

    fun getStoryAtPosition(position: Int): Story {
        return stories?.get(position)!!
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

}
