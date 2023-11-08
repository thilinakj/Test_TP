package io.tklabs.testintv.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.tklabs.testintv.network.ApiClient
import io.tklabs.testintv.remote.UserResponse
import io.tklabs.testintv.remote.UsersResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class MainViewModel : ViewModel() {

    private val userApi = ApiClient.userService

    private val _collectDataAsyncResult = MutableStateFlow("")
    val collectDataAsyncResult: Flow<String> get() = _collectDataAsyncResult

    private val _collectDataCallbackWayResult = MutableStateFlow("")
    val collectDataCallbackWayResult: Flow<String> get() = _collectDataCallbackWayResult

    fun callApiAsyncWay() {
        viewModelScope.launch(Dispatchers.IO) {
            collectDataAsyncWay()
        }
    }

    fun callApiCallbackWay() {
        viewModelScope.launch(Dispatchers.IO) {
            collectDataCallbackWay()
        }
    }

    private suspend fun collectDataAsyncWay() {
        val response1 = userApi.getUser("1")
        val response2 = userApi.getUsers("2")
        val result1 = response1.await()
        val result2 = response2.await()
        _collectDataAsyncResult.value = combineData(result1, result2)
    }

    private suspend fun collectDataCallbackWay() {
        val deferred1 = CompletableDeferred<Response<UsersResponse>>()
        userApi.getUsersCallbackWay("1").enqueue(object : Callback<UsersResponse> {
            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                if (response.isSuccessful) {
                    deferred1.complete(response)
                } else {
                    deferred1.completeExceptionally(Exception("Something went wrong"))
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                deferred1.completeExceptionally(Exception("Something went wrong"))
            }
        })
        val deferred2 = CompletableDeferred<Response<UserResponse>>()
        userApi.getUserCallbackWay("4").enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    deferred2.complete(response)
                } else {
                    deferred2.completeExceptionally(Exception("Something went wrong"))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                deferred2.completeExceptionally(Exception("Something went wrong"))
            }
        })
        _collectDataCallbackWayResult.value = try {
            val result1 = deferred1.await()
            val result2 = deferred2.await()
            combineData(result2.body(), result1.body())
        } catch (e: Exception) {
            println("collectDataCallbackWay: failed ")
            ""
        }
    }

    private fun combineData(userResponse: UserResponse?, usersResponse: UsersResponse?): String {
        return buildString {
            append("\nUser Response: ")
            userResponse?.data?.let {
                append("\n${it.id} ${it.email} ${it.first_name}")
            }
            append("\nUsers Response: ")
            usersResponse?.data?.forEach {
                append("\n${it.id} ${it.email} ${it.first_name}")
            }
        }.also {
            println(it)
        }
    }
}
