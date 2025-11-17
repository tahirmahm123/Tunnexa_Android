package com.tunnexa.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Tunnexa
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
@HiltAndroidApp
class TunnexaApplication : Application()

