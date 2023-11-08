package io.tklabs.testintv.remote

data class UsersResponse(
    val page: String,
    val data: List<UserDto>,
)
