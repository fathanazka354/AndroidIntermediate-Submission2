package com.nadikarim.submission2.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.nadikarim.submission2.data.local.database.StoryDatabase
import com.nadikarim.submission2.data.model.UserSession
import com.nadikarim.submission2.data.model.login.LoginResponse
import com.nadikarim.submission2.data.model.login.LoginResult
import com.nadikarim.submission2.data.model.login.RegisterResponse
import com.nadikarim.submission2.data.model.stories.AddResponse
import com.nadikarim.submission2.data.model.stories.StoriesResponse
import com.nadikarim.submission2.data.model.stories.Story
import com.nadikarim.submission2.data.remote.ApiService
import com.nadikarim.submission2.utils.RETROFIT_TAG
import com.uk.tastytoasty.TastyToasty
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(
    private val quoteDatabase: StoryDatabase,
    private val apiService: ApiService,
    //private val preference: UserPreference
    ) {

    private val _userLogin = MutableLiveData<LoginResult>()
    val userLogin: LiveData<LoginResult> = _userLogin

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _listStory = MutableLiveData<ArrayList<Story>>()
    val listStory: LiveData<ArrayList<Story>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getStory(token: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(token, apiService)
            }
        ).liveData
    }



    /*
    fun getStory(auth: String) {
        _isLoading.value = true
        apiService.getListStory("Bearer $auth").enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStory.postValue(response.body()?.listStory)
                    Log.d(RETROFIT_TAG, response.body()?.listStory.toString())
                }

            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(RETROFIT_TAG, t.message.toString())
            }

        })
    }

     */

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        apiService.loginUser(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _toastMessage.value = response.body()?.message
                        _userLogin.value = response.body()?.loginResult
                        Log.d(RETROFIT_TAG, response.body()?.message.toString())
                        Log.d(RETROFIT_TAG, response.body()?.loginResult?.token.toString())
                        Log.d(RETROFIT_TAG, response.body()?.loginResult?.name ?: "name")
                        Log.d(RETROFIT_TAG, response.body()?.loginResult?.userId ?: "userId")
                    }
                    if (!response.isSuccessful) {
                        _toastMessage.value = response.message()
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _toastMessage.value = t.message
                    _isLoading.value = false
                    Log.d(RETROFIT_TAG, t.message.toString())
                }

            })
    }

    fun registerUser(name: String, email: String,password: String) {
        _isLoading.value = true
        apiService.registerUser(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        Log.d(RETROFIT_TAG, response.body()?.message.toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.d(RETROFIT_TAG, t.message.toString())
                }

            })
    }

    fun addStory(token: String, imageMultipart: MultipartBody.Part, description: RequestBody){
        val service = apiService.uploadImage(token, imageMultipart, description)
        service.enqueue(object : Callback<AddResponse> {
            override fun onResponse(call: Call<AddResponse>, response: Response<AddResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _toastMessage.value = responseBody.message
                    } else {
                        _toastMessage.value =  response.message()
                    }
                }
            }

            override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                _toastMessage.value = "Gagal instance retrofit"
            }

        })
    }

    /*
    fun getUserSession() = preference.getUser()

    suspend fun setUser(user: UserSession) {
        preference.setUser(user)
    }

    suspend fun logout()  {
        preference.logout()
    }

 */
}