package com.irvan.movieku.mvvm.models

import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("poster_path")
    var poster_path: String? = null,
    @SerializedName("adult")
    var adult: Boolean = false,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("release_date")
    var release_date: String,
    @SerializedName("genre_ids")
    var genre_ids: IntArray? = null,
    @SerializedName("genres")
    var genres: IntArray? = null,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("original_title")
    var original_title: String,
    @SerializedName("original_language")
    var original_language: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("backdrop_path")
    var backdrop_path: String? = null,
    @SerializedName("popularity")
    var popularity: Number,
    @SerializedName("vote_count")
    var vote_count: Int = 0,
    @SerializedName("video")
    var video: Boolean = false,
    @SerializedName("vote_average")
    var vote_average: Number,
    @SerializedName("name")
    var name: String,
    @SerializedName("budget")
    var budget: String? = null,
    @SerializedName("homepage")
    var homepage: String? = null,
    @SerializedName("imdb_id")
    var imdb_id: String? = null,
    @SerializedName("production_companies")
    var production_companies: String? = null,
    @SerializedName("production_countries")
    var production_countries: String? = null,
    @SerializedName("revenue")
    var revenue: Int? = 0,
    @SerializedName("runtime")
    var runtime: Int? = 0,
    @SerializedName("spoken_languages")
    var spoken_languages: IntArray? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("tagline")
    var tagline: String? = null
)