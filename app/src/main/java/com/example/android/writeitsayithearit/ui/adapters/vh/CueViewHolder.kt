package com.example.android.writeitsayithearit.ui.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.databinding.CueListItemBinding
import com.example.android.writeitsayithearit.ui.adapters.ClickListener
import com.example.android.writeitsayithearit.vo.Cue
import timber.log.Timber

class CueViewHolder(
        private val binding : CueListItemBinding,
        private val clickListener: ClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener { view ->
            clickListener.onItemClick(view, adapterPosition)
        }
    }

    fun bind(cue: Cue) {
        binding.cue = cue
    }

}