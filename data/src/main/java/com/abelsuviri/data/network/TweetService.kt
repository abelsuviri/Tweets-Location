package com.abelsuviri.data.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming

/**
 * @author Abel Suviri
 */

interface TweetService {
    @GET("1.1/statuses/filter.json")
    @Streaming
    suspend fun getTweets(
        @Query("track") keyword: String
    ): ResponseBody
}