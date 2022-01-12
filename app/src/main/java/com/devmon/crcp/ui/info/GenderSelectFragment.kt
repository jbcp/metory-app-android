package com.devmon.crcp.ui.info

import android.app.Service
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseDialogFragment
import com.devmon.crcp.databinding.FragmentGenderSelectBinding
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.model.Gender
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.utils.onThrottleClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GenderSelectFragment : BaseDialogFragment<FragmentGenderSelectBinding>(
    layoutId = R.layout.fragment_gender_select
) {

    private var deviceWidth: Int = 0

    private var gender = Gender.NONE

    @Inject
    lateinit var alertFactory: AlertFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceWidth = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            (requireActivity().getSystemService(Service.WINDOW_SERVICE) as WindowManager).currentWindowMetrics.bounds.width()
        } else {
            val point = Point()
            (requireActivity().getSystemService(Service.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(
                point
            )
            point.x
        }

        with(binding) {
            val argGender = arguments?.getInt("gender")
            when (argGender) {
                Gender.MAN.key -> radioGroup3.check(R.id.rb_man)
                Gender.WOMAN.key -> radioGroup3.check(R.id.rb_woman)
            }

            radioGroup3.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb_man -> gender = Gender.MAN
                    R.id.rb_woman -> gender = Gender.WOMAN
                }
            }

            tvSetting.onThrottleClick {
                if (gender == Gender.NONE) {
                    alertFactory.openConfirmAlert(
                        childFragmentManager,
                        Alert("알림", "성별을 선택해주세요.")
                    )
                    return@onThrottleClick
                }

                setFragmentResult("gender", bundleOf("gender" to gender))
                findNavController().popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params
    }
}