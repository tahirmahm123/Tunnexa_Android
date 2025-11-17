package com.tunnexa.android.di

import android.content.Context
import com.tunnexa.android.data.api.ApiManager
import com.tunnexa.android.data.api.TunnexaApiService
import com.tunnexa.android.network.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing API-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    
    /**
     * Provides TunnexaApiService instance
     */
    @Provides
    @Singleton
    fun provideTunnexaApiService(): TunnexaApiService {
        return NetworkConfig.apiService
    }
    
    /**
     * Provides ApiManager instance
     */
    @Provides
    @Singleton
    fun provideApiManager(
        @ApplicationContext context: Context
    ): ApiManager {
        return ApiManager.getInstance(context)
    }
}

