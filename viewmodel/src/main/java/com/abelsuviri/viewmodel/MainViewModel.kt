package com.abelsuviri.viewmodel

import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelsuviri.data.Result
import com.abelsuviri.data.model.Tweet
import com.abelsuviri.data.repository.TweetRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

/**
 * @author Abel Suviri
 */

class MainViewModel constructor(private val tweetRepository: TweetRepository, private val preferences: SharedPreferences) : ViewModel() {

    val tweetLiveData: MutableLiveData<Tweet> = MutableLiveData()
    val tweetRemoveLiveData: MutableLiveData<Tweet> = MutableLiveData()
    private var coroutineJob: Job? = null

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getTweets(keyword: String) {
        viewModelScope.launch {
            //If there is a request already we cancel that coroutine to have a fresh stream
            if (coroutineJob != null) {
                coroutineJob?.cancel()
            }

            coroutineJob = viewModelScope.launch {
                //Collect the values emitted from the flow stream
                tweetRepository.getTweets(keyword).collect { response ->
                    if (response is Result.Success) {
                        if (response.data.geolocation != null || response.data.coordinates != null) {
                            tweetLiveData.postValue(response.data)
                            //This handler will manage the pin removal. After the specified lifetime the Tweet will be notified to be removed from the map
                            Handler().postDelayed(
                                Runnable {
                                    tweetRemoveLiveData.postValue(response.data)
                                }, preferences.getLong(PREFERENCE_TIMEOUT, 5000))
                        }
                    } else if (response is Result.Error) {
                        Log.e("Error", response.exception.message.toString())
                    }
                }
            }
        }
    }
}

const val PREFERENCE_TIMEOUT = "PREFERENCE_TIMEOUT"