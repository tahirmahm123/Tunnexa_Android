package com.tunnexa.android.di

import android.content.Context
import com.tunnexa.android.data.storage.SecurePreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing storage-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    
    /**
     * Provides SecurePreferencesManager instance
     */
    @Provides
    @Singleton
    fun provideSecurePreferencesManager(
        @ApplicationContext context: Context
    ): SecurePreferencesManager {
        return SecurePreferencesManager.getInstance(context)
    }
}

