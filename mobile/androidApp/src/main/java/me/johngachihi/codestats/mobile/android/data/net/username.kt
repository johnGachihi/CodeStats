package me.johngachihi.codestats.mobile.android.data.net

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

// TODO: Feels funny. Should this be `checkUsernameExists` instead?
//       [Later] It probably feels like this because the function is doing two things:
//       (1) communicating with the remote service and (2) interpreting
//       the remote service's response.
//       I like it the way it is. [Probation]
suspend fun isUsernameAvailable(username: String, httpClient: HttpClient = client): Boolean {
    try {
        httpClient.get("${Constants.BaseUrl}/username/$username/exists")
    } catch (e: ResponseException) {
        if (e.response.status == HttpStatusCode.NotFound) {
            return true
        }
        throw e
    }
    return false
}