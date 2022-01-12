package com.devmon.crcp.data.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import timber.log.Timber

/**
 * crcp api 의 output 을 성공(data), 실패(error) 에 따라 매핑한다.
 * 응답 json 의 rows 필드의 값이 1 이상일 때 성공, 1 미만 일때 에러로 판단한다.
 */
class CRCPResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val appJson = JSONObject()

        val response = chain.proceed(chain.request())
        val text = response.body?.string()  ?: ""
        try {
            val responseJson = JSONObject(text)
            val code = responseJson.getString("code")
            appJson.put("code", code)
            val rows = responseJson.getInt("rows")
            appJson.put("rows", rows)

            if (rows > 0) {
                val data = responseJson.get("output") ?: null
                appJson.put("data", data)
            } else {
                val error = responseJson.get("output") ?: null
                appJson.put("error", error.toString())
            }
        } catch (e: Exception) {
            // 파싱 중 에러가 발생하면 기존 응답을 반환함
            Timber.w(e)
            return response.newBuilder()
                .body(text.toResponseBody("application/json".toMediaType()))
                .build()
        }

        // 성공시 앱에서 사용하는 응답 형태의 json 으로 반환
        return response.newBuilder()
            .body(appJson.toString().toResponseBody("application/json".toMediaType()))
            .build()
    }
}