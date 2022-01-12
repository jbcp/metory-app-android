package com.devmon.crcp.ui.study

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import com.devmon.crcp.BR
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseRecyclerView
import com.devmon.crcp.databinding.ItemRecruitingBinding
import com.devmon.crcp.ui.model.Recruiting
import com.devmon.crcp.ui.studydetail.fetchStudySex
import com.devmon.crcp.utils.dp2px

class StudyAdapter : BaseRecyclerView.BaseAdapter<Recruiting, ItemRecruitingBinding>(
    diffUtil = object : DiffUtil.ItemCallback<Recruiting>() {
        override fun areItemsTheSame(
            oldItem: Recruiting,
            newItem: Recruiting,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Recruiting,
            newItem: Recruiting,
        ): Boolean {
            return oldItem == newItem
        }
    },
    layoutId = R.layout.item_recruiting,
    bindingVariableId = BR.data
) {

    private lateinit var onClickListener: OnClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseRecyclerView.BaseViewHolder<Recruiting, ItemRecruitingBinding> {
        return super.onCreateViewHolder(parent, viewType).apply {
            binding.layoutRecruiting.setOnClickListener {
                onClickListener.onClick(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseRecyclerView.BaseViewHolder<Recruiting, ItemRecruitingBinding>,
        position: Int,
    ) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        with(binding.flOption) {
            removeAllViews()
            if (item.starget.isNotEmpty()) {
                addView(createChip(context, item.starget))
            }

            if (item.ssex != null) {
                addView(
                    createChip(context, "").apply {
                        fetchStudySex(item.ssex)
                    })
            }

            item.sappl?.let {
                addView(createChip(context, item.sappl))
            }
        }
    }

    private fun createChip(context: Context, content: String): TextView {
        return TextView(context).apply {
            text = content
            textSize = 12f
            setTextColor(ContextCompat.getColor(context, R.color.color_grey_300))
            setBackgroundResource(R.drawable.bg_study_info)

            setPadding(
                context.dp2px(8).toInt(),
                context.dp2px(2).toInt(),
                context.dp2px(8).toInt(),
                context.dp2px(4).toInt(),
            )

            this.doOnLayout {
                updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    marginStart = context.dp2px(8).toInt()
                    marginEnd = context.dp2px(8).toInt()
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick(recruitingData: Recruiting)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}