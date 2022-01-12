package com.devmon.crcp.ui.consent

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.devmon.crcp.BR
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.base.BaseRecyclerView
import com.devmon.crcp.databinding.FragmentStudyConsentBinding
import com.devmon.crcp.databinding.ItemConsentBinding
import com.devmon.crcp.ui.model.Consent
import com.devmon.crcp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

/**
 * 동의서 목록 화면
 */
@FlowPreview
@AndroidEntryPoint
class StudyConsentFragment :
    BaseFragment<FragmentStudyConsentBinding, StudyConsentViewModel>(R.layout.fragment_study_consent) {

    override val viewModel: StudyConsentViewModel by viewModels()

    private val adapter: BaseRecyclerView.BaseAdapter<Consent, ItemConsentBinding> by lazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        binding.rvConsents.adapter = adapter

        viewModel.consents.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.state.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is StudyConsentViewModel.State.Detail -> {
                    findNavController().navigate(
                        StudyConsentFragmentDirections.actionStudyConsentFragmentToStudyConsentDetailFragment(
                            it.consent
                        )
                    )
                }
            }
        })
    }

    private fun createAdapter(): BaseRecyclerView.BaseAdapter<Consent, ItemConsentBinding> {
        return object : BaseRecyclerView.BaseAdapter<Consent, ItemConsentBinding>(
            layoutId = R.layout.item_consent,
            bindingVariableId = BR.item,
            diffUtil = object : DiffUtil.ItemCallback<Consent>() {
                override fun areItemsTheSame(oldItem: Consent, newItem: Consent): Boolean {
                    return oldItem.consentId == newItem.consentId
                }

                override fun areContentsTheSame(oldItem: Consent, newItem: Consent): Boolean {
                    return oldItem == newItem
                }
            }
        ) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): BaseRecyclerView.BaseViewHolder<Consent, ItemConsentBinding> {
                return super.onCreateViewHolder(parent, viewType).apply {
                    itemView.setOnClickListener {
                        val consent = getItem(adapterPosition)
                        viewModel.navigateConsentDetail(consent)
                    }
                }
            }
        }
    }
}