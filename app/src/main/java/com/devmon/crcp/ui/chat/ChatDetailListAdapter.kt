package com.devmon.crcp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.devmon.crcp.R
import com.devmon.crcp.data.network.response.QnaResponse
import com.devmon.crcp.databinding.ItemAdminChatMessageBinding
import com.devmon.crcp.databinding.ItemMyChatMessageBinding

class ChatDetailListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MY_CHAT_VIEW_HOLDER = 1
        const val ADMIN_CHAT_VIEW_HOLDER = 2
    }

    val chatList: MutableList<QnaResponse> = mutableListOf()

    override fun getItemViewType(position: Int): Int = chatList[position].subjflag ?: 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MY_CHAT_VIEW_HOLDER -> MyChattingViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.item_my_chat_message, parent, false
                )
            )
            else -> AdminChattingViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.item_admin_chat_message, parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            MY_CHAT_VIEW_HOLDER -> {
                (holder as MyChattingViewHolder).bind(chatList[position])
            }
            else -> {
                (holder as AdminChattingViewHolder).bind(chatList[position])
            }
        }
    }

    override fun getItemCount(): Int = chatList.size


    fun setChatList(list: List<QnaResponse>) {
        chatList.clear()
        chatList.addAll(list)
        this.notifyDataSetChanged()
    }

    fun addChatList(item:QnaResponse) {
        chatList.add(item)
        this.notifyDataSetChanged()
    }
}

class MyChattingViewHolder(val binding: ItemMyChatMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(qnaItem:QnaResponse) {
        binding.item = qnaItem
    }
}

class AdminChattingViewHolder(val binding: ItemAdminChatMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(qnaItem:QnaResponse) {
        binding.item = qnaItem
    }
}