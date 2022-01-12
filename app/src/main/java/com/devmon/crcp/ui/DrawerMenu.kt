package com.devmon.crcp.ui

import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.devmon.crcp.R

enum class DrawerMenu(@IdRes val actionId: Int) {
    MY_INFO(R.id.action_to_my_info),
    STUDY_DETAIL(R.id.action_to_study_detail),
    CONSENT(R.id.action_to_study_consent),
    SCREENING(R.id.action_to_selfScreeningFragment),
    QNA(R.id.action_to_chatDetailFragment),
    LOGOUT(R.id.action_to_login),
    ;

    fun navigate(navController: NavController) {
        navController.navigate(actionId)
    }
}