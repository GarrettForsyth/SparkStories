package com.example.android.writeitsayithearit.ui.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.databinding.CueListItemBinding
import com.example.android.writeitsayithearit.vo.Cue
import timber.log.Timber

class CueViewHolder(val binding : CueListItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(cue: Cue) {
        binding.cue = cue
    }

}