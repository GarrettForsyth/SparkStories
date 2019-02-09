package com.example.android.writeitsayithearit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.CueListItemBinding
import com.example.android.writeitsayithearit.ui.adapters.vh.CueViewHolder
import com.example.android.writeitsayithearit.vo.Cue

class CueAdapter() : RecyclerView.Adapter<CueViewHolder>() {

    private var cues: List<Cue>?  = null
    private lateinit var clickListener: ClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CueViewHolder {

        val binding : CueListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.cue_list_item,
                parent,
                false
        )
        return CueViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: CueViewHolder, position: Int) {
        val cue = cues?.get(position)!!
        holder.bind(cue)
    }

    override fun getItemCount(): Int {
        if (cues == null) {
            return 0
        }
        else {
            return cues?.size!!
        }
    }

    fun setList(cues: List<Cue>) {
        this.cues = cues
    }

    fun getCueAtPosition(position: Int): Cue {
        return cues?.get(position)!!
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

}