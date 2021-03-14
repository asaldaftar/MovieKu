package com.irvan.movieku.mvvm.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.irvan.movieku.mvvm.models.FavoriteModel
import com.irvan.movieku.mvvm.repositories.MovieRepository


class MovieViewModel(application: Application) : AndroidViewModel(application) {
    var isRequest = false
    var isQueryExhausted = false
    var isNextPage = false
    var page = 1
    private var cancelRequest = false
    private var repository = MovieRepository.getInstance(application)!!

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

    fun getListMovieReview(movie_id: String, page: Int, map: HashMap<String, String>) {
        if (!isRequest) {
            this.isRequest = true
            this.page = page
            this.isQueryExhausted = false
            repository.getListReview(movie_id, page, map)
        }
    }

    fun nextPageListMovieReview(moview_id: String, map: HashMap<String, String>) {
        if (!isRequest && !isQueryExhausted && !isNextPage) {
            page++
            isNextPage = true
            getListMovieReview(moview_id, page, map)
        }
    }

    fun addToFavorite(favoriteModel: FavoriteModel) = repository.addToFavorite(favoriteModel)
    fun getFavorite(session_id: String): LiveData<MutableList<FavoriteModel>> = repository.getFavorite(session_id)
    fun checkIfFavorite(session_id: String, movie_id: String) =
        repository.checkIfFavorite(session_id, movie_id)

    fun getMovieDetail(movie_id: String) = repository.getMovieDetail(movie_id)
    fun getVideo(movie_id: String) = repository.getVideo(movie_id)
    fun getGenreById(id: String) = repository.getGenreById(id)

    fun isListMovieLoaded() = repository.isListMovieLoaded()
    fun isMovieLoaded() = repository.isMovieLoaded()
    fun isVideoLoaded() = repository.isVideoLoaded()
    fun isReviewLoaded() = repository.isReviewLoaded()
    fun isFav() = repository.isFav()
    fun getListMovieUpcomingModels() = repository.getListMovieUpcomingModels()
    fun getListMoviePopularModels() = repository.getListMoviePopularModels()
    fun getListMovieTopRatedModels() = repository.getListMovieTopRatedModels()
    fun getListMovieNowPlayingModels() = repository.getListMovieNowPlayingModels()
    fun getListMovieSortedModels() = repository.getListMovieSortedModels()
    fun getListMovieGenre() = repository.getMovieGenre()
    fun getMovieDetailModel() = repository.getMovieDetailModel()
    fun getListVideoModels() = repository.getListVideoModels()
    fun getListReviewModels() = repository.getListReviewModels()

}