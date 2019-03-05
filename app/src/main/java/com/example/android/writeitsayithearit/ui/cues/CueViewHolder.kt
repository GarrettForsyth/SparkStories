package com.example.android.writeitsayithearit.ui.cues

import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.databinding.CueListItemBinding
import com.example.android.writeitsayithearit.model.cue.Cue

class CueViewHolder(
        private val binding : CueListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cue: Cue) {
        binding.cue = cue
        // It's important to call executePendingBindings() here
        // Otherwise the bindings don't get executed until the next frame
        // and onCreateViewHolder will improperly measure the viewholder
        // This can lead to some very tricky debugging when you, for
        // example, use the RecyclerViewActions to scroll during testing.
        binding.executePendingBindings()
    }

}