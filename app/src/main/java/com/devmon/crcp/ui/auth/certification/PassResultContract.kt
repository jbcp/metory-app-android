package com.devmon.crcp.ui.auth.certification

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class PassResultContract : ActivityResultContract<Unit, PassResult?>() {
    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(context, CertificationActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): PassResult? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getParcelableExtra(CertificationActivity.EXTRA_PASS_RESULT)
    }
}