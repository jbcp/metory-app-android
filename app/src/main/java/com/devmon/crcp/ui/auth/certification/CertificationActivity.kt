package com.devmon.crcp.ui.auth.certification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import com.devmon.crcp.BuildConfig
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseActivity
import com.devmon.crcp.data.datasource.MemberDataSource
import com.devmon.crcp.databinding.ActivityCertificationBinding
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.ui.alert.AlertFactory
import com.google.gson.Gson
import com.google.gson.JsonIOException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class CertificationActivity : BaseActivity<ActivityCertificationBinding>(
    layoutId = R.layout.activity_certification
) {

    @Inject
    lateinit var alertFactory: AlertFactory

    @Inject
    lateinit var memberDataSource: MemberDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.wbPass.apply {
            webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?,
                ): Boolean {
                    Timber.e("onJsAlert : $message")
//                    Toast.makeText(this@WebViewActivity, "onJsAlert : $message", Toast.LENGTH_SHORT)
//                        .show()
                    result?.confirm()
                    return true
                }

                override fun onJsConfirm(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    Timber.e("onJsConfirm : $message")
                    result?.confirm()
                    return true
                }
            }

            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    Timber.e("shouldOverrideUrlLoading : ${request?.url}")
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    Timber.e("onPageFinished : $url")
                    super.onPageFinished(view, url)
                }
            }

            settings.apply {
                javaScriptEnabled = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            val onSuccess = { result: PassResult ->
                // 종료
                Toast.makeText(this@CertificationActivity, "본인인증되었습니다.", Toast.LENGTH_SHORT).show()

                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_PASS_RESULT, result)
                })
                finish()
            }

            val onFail = { error: String ->
                alertFactory.openConfirmAlert(
                    supportFragmentManager,
                    Alert(
                        "결과",
                        error
                    )
                )
            }

            addJavascriptInterface(WebAppInterface(onSuccess, onFail), "Android")
            loadUrl(memberDataSource.getSubjectIp() + BuildConfig.PASS_URL)
        }
    }

    class WebAppInterface(
        private val onSuccess: (result: PassResult) -> Unit,
        private val onFail: (fail: String) -> Unit,
    ) {

        @JavascriptInterface
        fun setResult(result: String) {
            // 로그 결과 확인
            Timber.e("setResult = $result")

            var jsonString = result.drop(1)
            jsonString = jsonString.dropLast(1)
            Timber.e("setResult = $jsonString")

            try {
                val pass = Gson().fromJson(result, PassResult::class.java)
                onSuccess(pass)
            } catch (e: JsonIOException) {
                onFail("잘못된 형식의 응답입니다. 잠시 후 다시 시도해주세요.")
            } catch (e: Exception) {
                Timber.tag("setResult error").e(e.printStackTrace().toString())
                onFail("서버 요청에 실패했습니다. 잠시 후 다시 시도해주세요.")
            }
        }
    }

    companion object {
        const val EXTRA_PASS_RESULT = "EXTRA_PASS_RESULT"
    }
}