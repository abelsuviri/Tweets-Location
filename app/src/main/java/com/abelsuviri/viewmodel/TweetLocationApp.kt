package com.abelsuviri.viewmodel

import android.app.Application
import com.abelsuviri.viewmodel.di.modules.androidModule
import com.abelsuviri.viewmodel.di.modules.serviceModule
import com.abelsuviri.viewmodel.di.modules.viewModelModule
import org.conscrypt.Conscrypt
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.security.Security

/**
 * @author Abel Suviri
 */

class TweetLocationApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        startKoin {
            androidContext(this@TweetLocationApp)
            modules(listOf(androidModule, serviceModule, viewModelModule))
        }
    }
}