package com.example.android.writeitsayithearit.ui.cues

import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.databinding.CueListItemBinding
import com.example.android.writeitsayithearit.model.cue.Cue

class CueViewHolder(
        private val binding : CueListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cue: Cue) {
        binding.cue = cue
    }

}