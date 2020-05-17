package com.abelsuviri.viewmodel.di.modules

import androidx.preference.PreferenceManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * @author Abel Suviri
 */

val androidModule = module {
    single { PreferenceManager.getDefaultSharedPreferences(androidApplication()) }
}