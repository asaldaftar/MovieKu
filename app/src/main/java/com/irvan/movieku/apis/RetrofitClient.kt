package com.irvan.movieku.apis

import com.irvan.movieku.BuildConfig
import com.irvan.movieku.BuildConfig.API_LIVE
import com.irvan.movieku.BuildConfig.DEBUG
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class RetrofitClient {
    private var retrofit: Retrofit? = null
    fun getClient(isLong: Boolean): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        var BASE_URL: String = if (DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            API_LIVE
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
            API_LIVE
        }
        try {
            val timeout: Int
            timeout = if (isLong) {
                10
            } else {
                1
            }
            val client = OkHttpClient.Builder()
                .connectTimeout(timeout.toLong(), TimeUnit.MINUTES)
                .readTimeout(timeout.toLong(), TimeUnit.MINUTES)
                .writeTimeout(timeout.toLong(), TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(AddHeaderInterceptor()).build()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return retrofit
    }


    private class AddHeaderInterceptor() : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val url = request.url.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
            request = request.newBuilder().url(url).build()
            return chain.proceed(request)
        }
    }
}