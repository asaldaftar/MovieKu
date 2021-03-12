package com.irvan.movieku.apis

import com.irvan.movieku.apis.apiservices.MovieApiService


class UtilsApi {
    fun getMovieApiService(): MovieApiService {
        val retrofitClient = RetrofitClient()
        return retrofitClient.getClient(false)!!.create(MovieApiService::class.java)
    }
}