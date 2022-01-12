package com.devmon.crcp.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.data.network.response.QnaResponse
import com.devmon.crcp.databinding.FragmentChatDetailBinding
import com.devmon.crcp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class ChatDetailFragment : BaseFragment<FragmentChatDetailBinding, ChatDetailViewModel>(
    layoutId = R.layout.fragment_chat_detail
) {

    override val viewModel by viewModels<ChatDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatDetailAdapter = ChatDetailListAdapter()

        binding.rvChatDetail.apply {
            adapter = chatDetailAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        //viewModel.getApplid()

        viewModel.qnaList.observe(viewLifecycleOwner, { qnaList ->
            chatDetailAdapter.setChatList(qnaList)
            if (chatDetailAdapter.itemCount != 0) {
                binding.rvChatDetail.scrollToPosition(chatDetailAdapter.itemCount - 1)
            }
        })

        viewModel.sendMessage.observe(viewLifecycleOwner, EventObserver {
            chatDetailAdapter.addChatList(
                QnaResponse(
                    qnacontent = it,
                    subjflag = 1,
                    said = null,
                    invid = null,
                    qnadtc = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date()).toString()
                )
            )
            binding.rvChatDetail.scrollToPosition(chatDetailAdapter.itemCount - 1)
        })

        binding.tvSend.setOnClickListener {
            viewModel.sendMessage(binding.etChat.text.toString())
            binding.etChat.text.clear()
        }
    }
}