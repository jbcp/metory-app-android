package com.devmon.crcp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.devmon.crcp.CRCPApplication
import com.devmon.crcp.ui.progress.FullScreenProgressBar
import com.devmon.crcp.utils.EventObserver
import timber.log.Timber

abstract class BaseFragment<B : ViewDataBinding, VM: BaseViewModel>(
    @LayoutRes var layoutId: Int
) : Fragment() {

    lateinit var binding: B

    protected abstract val viewModel: VM

    private val fullScreenProgressBar = FullScreenProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity?.application as? CRCPApplication)?.currentScreen = this::class.java.simpleName
        Timber.e(this::class.java.simpleName)
    }

    override fun onStop() {
        (activity?.application as? CRCPApplication)?.currentScreen = ""
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fullProgress.observe(viewLifecycleOwner, EventObserver {
            showFullProgress(it)
        })
    }

    protected fun showFullProgress(isShow: Boolean) {
        if (isShow) {
            activity?.let { activity ->
                fullScreenProgressBar.show(activity)
            }
        } else {
            fullScreenProgressBar.dismiss()
        }
    }
}