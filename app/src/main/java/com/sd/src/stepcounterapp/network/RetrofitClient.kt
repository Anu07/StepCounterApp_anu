package com.sd.src.stepcounterapp.network

import com.sd.src.stepcounterapp.AppApplication
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitClient {

    //http://54.71.18.74:4575/api/auth/login

    val BASE_URL = "http://3.15.112.60:3000/"
    //    val BASE_URL = "http://54.71.18.74:4575/"
//    val IMG_URL = "http://54.71.18.74:4575"
    val IMG_URL = "http://3.15.112.60:3000"
    private val CACHE_CONTROL = "Cache-Control"
    private var retrofit: Retrofit? = null
    private var retrofitGoogle: Retrofit? = null
    private var retrofitEtaGoogle: Retrofit? = null
    private var apiInterface: ApiInterface? = null

    val instance: ApiInterface?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            if (apiInterface == null) {
                apiInterface = retrofit!!.create(ApiInterface::class.java)
            }
            return apiInterface
        }

    val baseClient: Retrofit?
        get() {
            if (retrofitEtaGoogle == null) {
                retrofitEtaGoogle = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofitEtaGoogle
        }

    //Creating OKHttpClient
    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(provideOfflineCacheInterceptor())
            .addNetworkInterceptor(provideCacheInterceptor())
            .cache(provideCache())
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    //Creating Cache
    private fun provideCache(): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(
                File(AppApplication.instance!!.cacheDir, "http-cache"),
                (10 * 1024 * 1024).toLong()
            ) // 10 MB
        } catch (ignored: Exception) {

        }

        return cache
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                .maxAge(2, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .header(CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    //Provides offline cache
    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            if (AppApplication.hasNetwork()) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }

            chain.proceed(request)
        }
    }
}