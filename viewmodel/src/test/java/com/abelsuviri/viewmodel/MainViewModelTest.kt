package com.abelsuviri.viewmodel

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abelsuviri.data.Result
import com.abelsuviri.data.model.Tweet
import com.abelsuviri.data.repository.TweetRepository
import com.abelsuviri.viewmodel.mock.MockJson
import com.abelsuviri.viewmodel.rule.CoroutinesTestRule
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * @author Abel Suviri
 */

class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var tweetRepository: TweetRepository

    @Mock
    lateinit var preferences: SharedPreferences

    private lateinit var mainViewModel: MainViewModel

    private lateinit var gson: Gson

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel(tweetRepository, preferences)
        gson = Gson()
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun test_get_tweets_stream_successfully() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val firstItemResponseModel = Result.Success(gson.fromJson(MockJson.mockJsonFirst, Tweet::class.java))
        val secondItemResponseModel = Result.Success(gson.fromJson(MockJson.mockJsonSecond, Tweet::class.java))

        val response = flow {
            emit(firstItemResponseModel)
            delay(100)
            emit(secondItemResponseModel)
        }

        `when`(tweetRepository.getTweets("")).thenReturn(response)

        mainViewModel.getTweets("")

        mainViewModel.tweetLiveData.observeForever{}

        Assert.assertEquals(mainViewModel.tweetLiveData.value, firstItemResponseModel.data)
        delay(101)
        Assert.assertEquals(mainViewModel.tweetLiveData.value, secondItemResponseModel.data)
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun test_get_tweets_stream_successfully_with_errors() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val firstErrorResponseModel = Result.Error(Throwable())
        val firstItemResponseModel = Result.Success(gson.fromJson(MockJson.mockJsonFirst, Tweet::class.java))
        val secondErrorResponseModel = Result.Error(Throwable())
        val secondItemResponseModel = Result.Success(gson.fromJson(MockJson.mockJsonSecond, Tweet::class.java))

        val response = flow {
            emit(firstErrorResponseModel)
            delay(100)
            emit(firstItemResponseModel)
            delay(100)
            emit(secondErrorResponseModel)
            delay(100)
            emit(secondItemResponseModel)
        }

        `when`(tweetRepository.getTweets("")).thenReturn(response)

        mainViewModel.getTweets("")

        mainViewModel.tweetLiveData.observeForever{}

        Assert.assertEquals(mainViewModel.tweetLiveData.value, null)
        delay(101)
        Assert.assertEquals(mainViewModel.tweetLiveData.value, firstItemResponseModel.data)
        delay(101)
        Assert.assertEquals(mainViewModel.tweetLiveData.value, firstItemResponseModel.data)
        delay(101)
        Assert.assertEquals(mainViewModel.tweetLiveData.value, secondItemResponseModel.data)
    }
}