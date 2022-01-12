package com.devmon.crcp.ui.consent

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentStudyConsentDetailBinding
import com.devmon.crcp.utils.EventObserver
import com.devmon.crcp.utils.ViewExt.toast
import com.devmon.crcp.utils.onThrottleClick
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * 동의서 상세
 * PDF 뷰어로 보여준다
 */
@AndroidEntryPoint
class StudyConsentDetailFragment :
    BaseFragment<FragmentStudyConsentDetailBinding, StudyConsentDetailViewModel>(R.layout.fragment_study_consent_detail) {

    override val viewModel: StudyConsentDetailViewModel by viewModels()
    private lateinit var downloadManager: DownloadManager

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent?.action) {
                if (downloadId == id) {
                    viewModel.savePdfFile(pdfFile)
                }
            } else {
                viewModel.onDownloadFailed()
            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST = 1000
    }

    var downloadId: Long = -1
    var pageNumber = 0
    lateinit var pdfFile: File

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        requireContext().registerReceiver(broadcastReceiver, intentFilter)

        downloadManager =
            requireContext().getSystemService(Service.DOWNLOAD_SERVICE) as DownloadManager

        viewModel.nextEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                StudyConsentDetailFragmentDirections.actionStudyConsentDetailFragmentToStudyConsentCheckFragment(
                    it
                )
            )
        })

        viewModel.pdfFile.observe(viewLifecycleOwner, EventObserver { file ->
            Timber.e(file.name)
            Timber.e(file.path)
            Timber.e(file.absolutePath)
            Timber.e("${file.absoluteFile}")
            toast("다운로드 완료되었습니다.")
        })

        viewModel.failed.observe(viewLifecycleOwner, EventObserver {
            // binding.tvError.visibility = View.VISIBLE
            toast("다운로드 실패하였습니다.")
        })

        binding.btnDownload.onThrottleClick {
            requestPermissionAndDownloadPdf()
        }

        val linkUrl = viewModel.getLinkUrl()
        Timber.e(linkUrl)
        loadPdf(linkUrl)
    }

    private fun requestPermissionAndDownloadPdf() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            activity?.let {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), PERMISSION_REQUEST
                )
            }
        } else {
            downloadPdf(viewModel.getLinkUrl().split("/").last(), viewModel.getLinkUrl())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermissionAndDownloadPdf()
            } else {
                toast("권한 요청을 수락해주세요.")
            }
        }
    }

    private fun loadPdf(url: String) {
        if (url.isEmpty()) {
            toast("잠시 후 다시 시도해주세요")
            return
        }

        binding.pdfView.isVisible = true
        OkHttpClient.Builder()
            .apply {
                val x509TrustManager = object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }
                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }

                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, arrayOf(x509TrustManager), SecureRandom())

                if (sslContext.socketFactory != null) {
                    sslSocketFactory(sslContext.socketFactory, x509TrustManager)
                }
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })
            .build()
            .newCall(
                Request.Builder()
                    .url(url)
                    .build()
            )
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    binding.pdfView.isVisible = false
                    Timber.e("onFailure $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    val pdfData = response.body?.bytes()

                    if (pdfData != null) {
                        binding.pdfView
                            .fromBytes(pdfData)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .onError { t ->
                                // 에러처리
                                Timber.tag("pdf onError").e(t)
                                binding.tvError.visibility = View.VISIBLE
                            }
                            .onPageError { page, t ->
                                Timber.tag("pdf onPageError").e(t)
                                binding.tvError.visibility = View.VISIBLE
                            }
                            .onLoad {
                                binding.tvError.visibility = View.INVISIBLE
                            }
                            .load()
                    }
                }
            })
    }

    private fun downloadPdf(name: String, url: String) {
        val file = File(requireContext().getExternalFilesDir(null), name)
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(file))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        try {
            downloadId = downloadManager.enqueue(request)
        } catch (e: Exception) {
            Timber.tag("pdf error").e(e.printStackTrace().toString())
        }
        pdfFile = file
    }

    override fun onDestroyView() {
        requireContext().unregisterReceiver(broadcastReceiver)
        super.onDestroyView()
    }
}