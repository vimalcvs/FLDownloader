package com.vimalcvs.sample.internal.network

import com.vimalcvs.sample.internal.utils.DownloadConst
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

internal object RetrofitInstance {

    @Volatile
    private var downloadService: DownloadService? = null

    fun getDownloadService(
        connectTimeOutInMs: Long,
        readTimeOutInMs: Long
    ): DownloadService {
        if (downloadService == null) {
            synchronized(this) {
                if (downloadService == null) {
                    downloadService = Retrofit
                        .Builder()
                        .baseUrl(DownloadConst.BASE_URL)
                        .client(
                            OkHttpClient
                                .Builder()
                                .connectTimeout(connectTimeOutInMs, TimeUnit.MILLISECONDS)
                                .readTimeout(readTimeOutInMs, TimeUnit.MILLISECONDS)
                                .build()
                        )
                        .build()
                        .create(DownloadService::class.java)
                }
            }
        }
        return downloadService!!
    }
}