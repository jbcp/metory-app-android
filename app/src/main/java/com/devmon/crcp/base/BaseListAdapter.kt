package com.devmon.crcp.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView {

    abstract class BaseAdapter<T : Any?, B : ViewDataBinding>(
        private val layoutId: Int,
        private val bindingVariableId: Int,
        @NonNull diffUtil: DiffUtil.ItemCallback<T>
    ) : ListAdapter<T, BaseViewHolder<T, B>>(diffUtil) {

        lateinit var binding: B

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, B> {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
            return object : BaseViewHolder<T, B>(binding, bindingVariableId) {}
        }

        override fun onBindViewHolder(holder: BaseViewHolder<T, B>, position: Int) {
            holder.bind(getItem(position))
        }

    }

    abstract class BaseViewHolder<T : Any?, B : ViewDataBinding>(
        val binding: B,
        private val bindingVariableId: Int
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            item?.let {
                binding.setVariable(bindingVariableId, it)
                binding.executePendingBindings()
            }
        }
    }
}