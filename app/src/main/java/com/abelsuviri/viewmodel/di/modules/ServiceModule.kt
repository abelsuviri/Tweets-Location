package com.abelsuviri.viewmodel.di.modules

import com.abelsuviri.data.network.TweetService
import com.abelsuviri.data.repository.TweetRepository
import com.abelsuviri.viewmodel.BuildConfig
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor

/**
 * @author Abel Suviri
 */

val serviceModule = module {
    single { TweetRepository(tweetService()) }
}

private fun setupClient(): OkHttpClient {
    val consumer = OkHttpOAuthConsumer(BuildConfig.API_K, BuildConfig.API_S)
    consumer.setTokenWithSecret(BuildConfig.API_T, BuildConfig.API_TS)

    val client = OkHttpClient.Builder()
        .addInterceptor(SigningInterceptor(consumer))
    return client.build()
}

private fun tweetService(): TweetService {
    val retrofit = Retrofit.Builder()
        .baseUrl(streamUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(setupClient())
        .build()
    return retrofit.create(TweetService::class.java)
}

private const val streamUrl = "https://stream.twitter.com"