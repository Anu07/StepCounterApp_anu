package com.sd.src.stepcounterapp.network

import android.provider.Telephony.Carriers.BEARER
import android.util.Log
import com.sd.src.stepcounterapp.AppConstants.*
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_ELCTOKEN
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
//    http://172.24.8.177:3000/api/dashboard
//    val BASE_URL = "http://3.15.112.60:3000/"
//    val ELCIES_BASE_URL = "https://api.elcies.com/"
    //        val BASE_URL = "http://54.71.18.74:4575/"
//    val IMG_URL = "http://54.71.18.74:4575"
//    val IMG_URL = "http://3.15.112.60:3000"
    private val CACHE_CONTROL = "Cache-Control"
    private var retrofit: Retrofit? = null
    private var retrofitElcies: Retrofit? = null
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
//            if (apiInterface == null) {
                apiInterface = retrofit!!.create(ApiInterface::class.java)
//            }
            return apiInterface
        }

    val baseClient: ApiInterface?
        get() {
            Log.e("base", "url$ELCIES_BASE_URL")
            if (retrofitElcies == null) {
                retrofitElcies = Retrofit.Builder()
                    .baseUrl(ELCIES_BASE_URL)
                    .client(elciesHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            apiInterface = retrofitElcies!!.create(ApiInterface::class.java)
            return apiInterface
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
                File(HayaTechApplication.instance!!.cacheDir, "http-cache"),
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

            if (HayaTechApplication.hasNetwork()) {
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


    //Creating OKHttpClient for Elcies
    private fun elciesHeader(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(AUTHORIZATION, "Bearer ${SharedPreferencesManager.getString(HayaTechApplication.applicationContext(),VAR_ELCTOKEN)}")
                    .addHeader(CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(provideOfflineCacheInterceptor())
            .addNetworkInterceptor(provideCacheInterceptor())
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()
    }


}