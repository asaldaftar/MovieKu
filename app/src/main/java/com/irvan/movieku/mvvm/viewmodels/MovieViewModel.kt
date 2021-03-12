package com.irvan.movieku.mvvm.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.irvan.movieku.mvvm.repositories.MovieRepository


class MovieViewModel(application: Application) : AndroidViewModel(application) {
    var isRequest = false
    var isQueryExhausted = false
    var isNextPage = false
    var page = 1
    private var repository = MovieRepository.getInstance(application)!!

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

    fun isListMovieLoaded() = repository.isListMovieLoaded()
    fun getListMovieModels() = repository.getListMovieModels()

}