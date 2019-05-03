package com.example.android.sparkstories.ui.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.example.android.sparkstories.AppExecutors

/**
 * A generic RecyclerView adapter that uses Data Binding and DiffUtil with the Paging Library
 *
 * @param <T> Type of the items int he list
 * @param <V> The type of the ViewDataBinding
 */
abstract class DataBoundPagedListAdapter<T, V : ViewDataBinding>(
    appExecutors: AppExecutors,
    diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, DataBoundViewHolder<V>>(
    AsyncDifferConfig.Builder<T>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        val item = getItem(position)
        item?.let {
            bind(holder.binding, item)
            holder.binding.executePendingBindings()
        }
    }

    protected abstract fun bind(binding: V, item: T)
}

