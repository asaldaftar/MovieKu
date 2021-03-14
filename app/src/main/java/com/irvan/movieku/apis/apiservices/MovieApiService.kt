package com.irvan.movieku.apis.apiservices

import androidx.lifecycle.LiveData
import com.irvan.movieku.apis.apiresponses.*
import com.irvan.movieku.mvvm.models.MovieModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface MovieApiService {

    @GET("movie/now_playing")
    fun listMovieNowPlaying(
        @QueryMap map: Map<String, String>
    ): Call<MovieListResponse>

    @GET("movie/popular")
    fun listMoviePopular(
        @QueryMap map: Map<String, String>
    ): Call<MovieListResponse>

    @GET("movie/top_rated")
    fun listMovieTopRated(
        @QueryMap map: Map<String, String>
    ): Call<MovieListResponse>

    @GET("movie/upcoming")
    fun listMovieUpcoming(
        @QueryMap map: Map<String, String>
    ): Call<MovieListResponse>

    @GET("discover/movie")
    fun listMovieSort(
        @QueryMap map: Map<String, String>
    ): Call<MovieListResponse>

    @GET("movie/latest")
    fun movieLatest(): Call<MovieModel>

    @GET("movie/{movie_id}")
    fun movieDetail(
        @Path("movie_id") movie_id: String
    ): Call<MovieModel>

    @GET("movie/{movie_id}/videos")
    fun movieVideo(
        @Path("movie_id") movie_id: String,
        @Query("apprend_to_response") id: String = "video"
    ): Call<VideoResponse>

   @GET("movie/{movie_id}/reviews")
    fun getListReview(
        @Path("movie_id") movie_id: String,
        @QueryMap map: Map<String, String>
    ): Call<ReviewListResponse>

    @GET("genre/movie/list")
    fun listMovieGenre(): Call<GenreListResponse>

}