package com.irvan.movieku.mvvm.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.mvvm.models.GenreModel
import com.irvan.movieku.mvvm.repositories.MovieRepository
import com.irvan.movieku.utils.Resource


class MovieViewModel(application: Application) : AndroidViewModel(application) {
    var isRequest = false
    var isQueryExhausted = false
    var isNextPage = false
    var page = 1
    private var cancelRequest = false
    private var repository = MovieRepository.getInstance(application)!!
    private var genreModels: MediatorLiveData<Resource<MutableList<GenreModel>?>> =
        MediatorLiveData()

    fun getMovieGenre() {
        repository.getMovieGenreApi()
    }

    fun getListMovieNowPlaying(page: Int) {
        if (!isRequest) {
            this.isRequest = true
            this.page = page
            this.isQueryExhausted = false
            repository.getListMovieNowPlaying(page)
        }
    }

    fun nextPageListMovieNowPlaying() {
        if (!isRequest && !isQueryExhausted && !isNextPage) {
            page++
            isNextPage = true
            getListMovieNowPlaying(page)
        }
    }

    fun getListMoviePopular(page: Int) {
        if (!isRequest) {
            this.isRequest = true
            this.page = page
            this.isQueryExhausted = false
            repository.getListMoviePopular(page)
        }
    }

    fun nextPageListMoviePopular() {
        if (!isRequest && !isQueryExhausted && !isNextPage) {
            page++
            isNextPage = true
            getListMoviePopular(page)
        }
    }

    fun getListMovieTopRated(page: Int) {
        if (!isRequest) {
            this.isRequest = true
            this.page = page
            this.isQueryExhausted = false
            repository.getListMovieTopRated(page)
        }
    }

    fun nextPageListMovieTopRated() {
        if (!isRequest && !isQueryExhausted && !isNextPage) {
            page++
            isNextPage = true
            getListMovieTopRated(page)
        }
    }

    fun getListMovieUpcoming(page: Int) {
        if (!isRequest) {
            this.isRequest = true
            this.page = page
            this.isQueryExhausted = false
            repository.getListMovieUpcoming(page)
        }
    }

    fun nextPageListMovieUpcoming() {
        if (!isRequest && !isQueryExhausted && !isNextPage) {
            page++
            isNextPage = true
            getListMovieUpcoming(page)
        }
    }

    fun getListMovieSorted(page: Int, map: HashMap<String, String>) {
        if (!isRequest) {
            this.isRequest = true
            this.page = page
            this.isQueryExhausted = false
            repository.getListMovieSorted(page, map)
        }
    }

    fun nextPageListMovieSorted(map: HashMap<String, String>) {
        if (!isRequest && !isQueryExhausted && !isNextPage) {
            page++
            isNextPage = true
            getListMovieSorted(page, map)
        }
    }

    fun isListMovieLoaded() = repository.isListMovieLoaded()
    fun getListMovieUpcomingModels() = repository.getListMovieUpcomingModels()
    fun getListMoviePopularModels() = repository.getListMoviePopularModels()
    fun getListMovieTopRatedModels() = repository.getListMovieTopRatedModels()
    fun getListMovieNowPlayingModels() = repository.getListMovieNowPlayingModels()
    fun getListMovieSortedModels() = repository.getListMovieSortedModels()
    fun getListMovieGenre() = repository.getMovieGenre()

}