package com.devmon.crcp.ui.consent

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.devmon.crcp.BR
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseRecyclerView
import com.devmon.crcp.data.network.response.Check
import com.devmon.crcp.databinding.ItemConsentCheckBinding

class ConsentCheckAdapter : BaseRecyclerView.BaseAdapter<Check, ItemConsentCheckBinding>(
    layoutId = R.layout.item_consent_check,
    bindingVariableId = BR.check,
    diffUtil = object : DiffUtil.ItemCallback<Check>() {
        override fun areItemsTheSame(oldItem: Check, newItem: Check): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: Check, newItem: Check): Boolean {
            return oldItem.content == newItem.content
        }
    }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerView.BaseViewHolder<Check, ItemConsentCheckBinding> {
        return super.onCreateViewHolder(parent, viewType).apply {
            this.binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.rb_agree -> getItem(adapterPosition).accept = true
                    R.id.rb_disagree -> getItem(adapterPosition).accept = false
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseRecyclerView.BaseViewHolder<Check, ItemConsentCheckBinding>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position).apply {
            if (position == itemCount - 1) {
                holder.binding.layout.setBackgroundResource(R.drawable.bg_shape_stroke_1dp_black)
            }
        }
    }

    fun isCheck(): Boolean {
        for (i in 0 until itemCount) {
            if (getItem(i).accept != true)
                return false
        }
        return true
    }
}