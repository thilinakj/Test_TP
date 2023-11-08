package io.tklabs.testintv.network

import io.tklabs.testintv.remote.UserResponse
import io.tklabs.testintv.remote.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("users")
    fun getUsers(@Query("page") page: String): Call<UsersResponse>

    @GET("users")
    fun getUsersCallbackWay(@Query("page") page: String): Call<UsersResponse>

    @GET("users/{id}")
    fun getUser(@Path("id") id: String): Call<UserResponse>

    @GET("users/{id}")
    fun getUserCallbackWay(@Path("id") id: String): Call<UserResponse>
}
