package com.irvan.movieku.apis.apiresponses

import android.util.Log
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.services.AppService
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException


sealed class ApiResponse<T> {

    companion object {
        private val TAG: String = "AppDebug"


        fun <T> create(error: Throwable): ApiResponse<T> {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "create: error", error)
            }
            return if (error is SocketTimeoutException) {
                ApiTimeoutResponse("ConnectionTimeout")
            } else {
                ApiErrorResponse(
                    error.message ?: "unknown error",
                    402
                )
            }
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "ApiResponse: response: ${response}")
                    Log.d(TAG, "ApiResponse: raw: ${response.raw()}")
                Log.d(TAG, "ApiResponse: headers: ${response.headers()}")
                Log.d(TAG, "ApiResponse: message: ${response.message()}")
            }

            if (response.isSuccessful) {
                AppService.isTimeout = true
                val body = response.body()
                val code = response.code()
                return if (body == null) {
                    ApiEmptyResponse()
                } else if (code == 401) {
                    ApiUnAuthorizedResponse(
                        "401 Invalid API key: You must be granted a valid key.",
                        code
                    )
                } else if (code == 403) {
                    ApiErrorResponse(
                        "403 The resource you requested could not be found.",
                        code
                    )
                } else {
                    ApiSuccessResponse(body)
                }
            } else {
                val jsonObject = JSONObject(response.errorBody()!!.string())
                val msg: String
                val code: Int
                if (jsonObject.isNull("status_message")) {
                    code = response.code()
                    msg = response.message()
                } else {
                    code = response.code()
                    msg = jsonObject.getString("status_message")
                }
                return if (code == 401) {
                    ApiUnAuthorizedResponse(
                        msg,
                        code
                    )
                } else {
                    ApiErrorResponse(
                        msg,
                        code
                    )
                }
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>() {}

data class ApiErrorResponse<T>(val errorMessage: String, val code: Int) : ApiResponse<T>()

data class ApiTimeoutResponse<T>(val errorMessage: String) : ApiResponse<T>()

data class ApiUnAuthorizedResponse<T>(val errorMessage: String, val code: Int) : ApiResponse<T>()