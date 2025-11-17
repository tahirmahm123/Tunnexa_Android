package com.tunnexa.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tunnexa.data.api.TunnexaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network configuration object for setting up Retrofit and OkHttp
 */
object NetworkConfig {
    
    // Base URL - Update this to match your production API
    private const val BASE_URL = "https://tunnexa.cashbookbd.com/"
    
    // Timeout values
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L
    
    /**
     * Gson instance with custom configuration
     */
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .setPrettyPrinting()
        .create()
    
    /**
     * OkHttpClient with logging interceptor
     */
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Enable logging in debug builds
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Retrofit instance
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * API service instance
     */
    val apiService: TunnexaApiService by lazy {
        retrofit.create(TunnexaApiService::class.java)
    }
    
    /**
     * Update base URL (useful for testing or different environments)
     */
    fun updateBaseUrl(newBaseUrl: String): TunnexaApiService {
        val newRetrofit = Retrofit.Builder()
            .baseUrl(newBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        
        return newRetrofit.create(TunnexaApiService::class.java)
    }
}

