package com.irvan.movieku.apis.apiservices

import com.irvan.movieku.apis.apiresponses.MovieListResponse
import retrofit2.Call
import retrofit2.http.*


interface MovieApiService {
    @GET("movie/upcoming")
    fun listMovieUpcoming(
        @QueryMap map: Map<String, String>
    ): Call<MovieListResponse>

}