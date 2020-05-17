package com.abelsuviri.viewmodel.di.modules

import com.abelsuviri.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Abel Suviri
 */

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
}