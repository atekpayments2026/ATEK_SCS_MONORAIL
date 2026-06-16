package com.atek.scs.feature.common.network

import com.atek.scs.feature.common.system.Environment
import com.atek.scs.feature.common.system.env
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.tinylog.Logger
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiManager {

    private val ATEK_CCS_BASE_URL = env(Environment.ATEK_CCS_BASE_URL)
    private val ATEK_QR_BASE_URL = env(Environment.ATEK_QR_BASE_URL)

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .apply {
            if (Logger.isTraceEnabled()) {
                addInterceptor(
                    HttpLoggingInterceptor { message ->
                        Logger.trace { "[API_CALL] $message" }
                    }.setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }
        .build()


    private fun createRetrofitService(baseUrl: String, client: OkHttpClient = okHttpClient): ApiService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(/*gson*/))
            .build()
            .create(ApiService::class.java)

    fun getCCService(): ApiService = createRetrofitService(ATEK_CCS_BASE_URL)
    fun getQRService(): ApiService = createRetrofitService(ATEK_QR_BASE_URL)

    fun equipmentApiService(ip: String): ApiService = createRetrofitService("http://$ip:3030/api/")

}