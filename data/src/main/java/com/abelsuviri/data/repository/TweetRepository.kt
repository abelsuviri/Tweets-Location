package com.abelsuviri.data.repository

import com.abelsuviri.data.Result
import com.abelsuviri.data.model.Tweet
import com.abelsuviri.data.network.TweetService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


/**
 * @author Abel Suviri
 */

class TweetRepository constructor(private val tweetService: TweetService) {
    @ExperimentalCoroutinesApi
    fun getTweets(keyword: String) = flow {
        try {
            val response = tweetService.getTweets(keyword).source()
            withContext(Dispatchers.IO) {
                //Read the content of the stream and convert it to a Tweet object with Gson. Then send the object to the flow collection
                while (!response.exhausted()) {
                    val tweet = Gson().fromJson(response.readUtf8Line(), Tweet::class.java)
                    emit(Result.Success(tweet))
                }
            }
        } catch (e: Throwable) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
