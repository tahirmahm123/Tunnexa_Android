package com.tunnexa.android.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tunnexa.android.data.cache.ServerCacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing cache-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    
    /**
     * Provides Gson instance for JSON serialization/deserialization
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
    
    /**
     * Provides ServerCacheManager instance
     */
    @Provides
    @Singleton
    fun provideServerCacheManager(
        @ApplicationContext context: Context,
        gson: Gson
    ): ServerCacheManager {
        return ServerCacheManager(context, gson)
    }
}

