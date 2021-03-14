package com.irvan.movieku.mvvm.repositories

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.irvan.movieku.apis.UtilsApi
import com.irvan.movieku.apis.apiresponses.GenreListResponse
import com.irvan.movieku.apis.apiresponses.MovieListResponse
import com.irvan.movieku.apis.apiresponses.ReviewListResponse
import com.irvan.movieku.apis.apiresponses.VideoResponse
import com.irvan.movieku.database.AppDatabase
import com.irvan.movieku.database.dao.FavoriteDao
import com.irvan.movieku.database.dao.GenreDao
import com.irvan.movieku.mvvm.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class MovieRepository(private val application: Application) : CoroutineScope {
    private val TAG = "MovieRepository"

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    private var genreDao: GenreDao
    private var favoriteDao: FavoriteDao
    private var isListMovieLoaded: MutableLiveData<String> = MediatorLiveData()
    private var isMovieLoaded: MutableLiveData<String> = MediatorLiveData()
    private var isVideoLoaded: MutableLiveData<String> = MediatorLiveData()
    private var isReviewLoaded: MutableLiveData<String> = MediatorLiveData()
    private var isFav: MutableLiveData<Boolean> = MediatorLiveData()
    private var listMovieUpcomingModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()
    private var listMovieTopRatedModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()
    private var listMovieNowPlayingModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()
    private var listMoviePopularModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()
    private var listMovieSortedModels: MutableLiveData<MutableList<MovieModel>> = MediatorLiveData()
    private var listFavoriteModels: MutableLiveData<MutableList<FavoriteModel>> = MediatorLiveData()
    private var listReviewModels: MutableLiveData<MutableList<ReviewModel>> = MediatorLiveData()
    private var listVideoModel: MutableLiveData<MutableList<VideoModel>> = MediatorLiveData()
    private var movieDetailModel: MutableLiveData<MovieModel> = MediatorLiveData()


    init {
        val db = AppDatabase.getInstance(application)
        genreDao = db!!.genreDao()
        favoriteDao = db!!.favoriteDao()
    }

    companion object {
        private var instance: MovieRepository? = null

        fun getInstance(application: Application): MovieRepository? {
            if (instance == null) {
                instance = MovieRepository(application)
            }
            return instance
        }
    }

    fun getListMovieUpcoming(page: Int) {
        var map: HashMap<String, String> = HashMap()
        isListMovieLoaded.postValue("loading")
        if (page > 1) {
            map.put("page", page.toString())
        }
        UtilsApi().getMovieApiService().listMovieUpcoming(map)
            .enqueue(object :
                Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        isListMovieLoaded.postValue("success")
                        Handler().postDelayed({
                            listMovieUpcomingModels.postValue(data.results)
                        }, 100)
                    } else {
                        isListMovieLoaded.postValue("error")
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isListMovieLoaded.postValue("error")
                }
            })
    }

    fun getListMovieNowPlaying(page: Int) {
        var map: HashMap<String, String> = HashMap()
        isListMovieLoaded.postValue("loading")
        if (page > 1) {
            map.put("page", page.toString())
        }
        UtilsApi().getMovieApiService().listMovieNowPlaying(map)
            .enqueue(object :
                Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        isListMovieLoaded.postValue("success")
                        Handler().postDelayed({
                            listMovieNowPlayingModels.postValue(data.results)
                        }, 100)
                    } else {
                        isListMovieLoaded.postValue("error")
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isListMovieLoaded.postValue("error")
                }
            })
    }

    fun getListMovieTopRated(page: Int) {
        var map: HashMap<String, String> = HashMap()
        isListMovieLoaded.postValue("loading")
        if (page > 1) {
            map.put("page", page.toString())
        }
        UtilsApi().getMovieApiService().listMovieTopRated(map)
            .enqueue(object :
                Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        isListMovieLoaded.postValue("success")
                        Handler().postDelayed({
                            listMovieTopRatedModels.postValue(data.results)
                        }, 100)
                    } else {
                        isListMovieLoaded.postValue("error")
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isListMovieLoaded.postValue("error")
                }
            })
    }

    fun getListMoviePopular(page: Int) {
        var map: HashMap<String, String> = HashMap()
        isListMovieLoaded.postValue("loading")
        if (page > 1) {
            map.put("page", page.toString())
        }
        UtilsApi().getMovieApiService().listMoviePopular(map)
            .enqueue(object :
                Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        isListMovieLoaded.postValue("success")
                        Handler().postDelayed({
                            listMoviePopularModels.postValue(data.results)
                        }, 100)
                    } else {
                        isListMovieLoaded.postValue("error")
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isListMovieLoaded.postValue("error")
                }
            })
    }

    fun getListMovieSorted(page: Int, map: HashMap<String, String>) {
        isListMovieLoaded.postValue("loading")
        if (page > 1) {
            map.put("page", page.toString())
        }
        UtilsApi().getMovieApiService().listMovieSort(map)
            .enqueue(object :
                Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        isListMovieLoaded.postValue("success")
                        Handler().postDelayed({
                            listMovieSortedModels.postValue(data.results)
                        }, 100)
                    } else {
                        isListMovieLoaded.postValue("error")
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isListMovieLoaded.postValue("error")
                }
            })
    }

    fun getListReview(movie_id: String, page: Int, map: HashMap<String, String>) {
        isReviewLoaded.postValue("loading")
        if (page > 1) {
            map.put("page", page.toString())
        }
        UtilsApi().getMovieApiService().getListReview(movie_id, map)
            .enqueue(object :
                Callback<ReviewListResponse> {
                override fun onResponse(
                    call: Call<ReviewListResponse>,
                    response: Response<ReviewListResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        isReviewLoaded.postValue("success")
                        Handler().postDelayed({
                            listReviewModels.postValue(data.results)
                        }, 100)
                    } else {
                        isReviewLoaded.postValue("error")
                    }
                }

                override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isReviewLoaded.postValue("error")
                }
            })
    }

    fun getMovieGenre() = genreDao.getGenre()

    fun getGenreById(id: String) = genreDao.getGenreById(id)

    fun getMovieGenreApi() {
        UtilsApi().getMovieApiService().listMovieGenre()
            .enqueue(object : Callback<GenreListResponse> {
                override fun onResponse(
                    call: Call<GenreListResponse>,
                    response: Response<GenreListResponse>
                ) {
                    if (response.isSuccessful) {
                        launch { response.body()?.genres?.let { inputGenre(it) } }
                    }
                }

                override fun onFailure(call: Call<GenreListResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
    }

    private suspend fun inputGenre(genreModels: MutableList<GenreModel>) {
        withContext(Dispatchers.IO) {
            try {
                genreModels.forEach {
                    genreDao.insertGenre(it)
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    fun addToFavorite(favModels: FavoriteModel) {
        launch {
            inputFav(favModels)
            checkFav(favModels.sessionId, favModels.movieId)
        }
    }

    private suspend fun inputFav(favModels: FavoriteModel) {
        withContext(Dispatchers.IO) {
            try {
                if (favoriteDao.checkFav(favModels.sessionId, favModels.movieId) > 0) {
                    favoriteDao.updateFav(
                        favModels.movieId,
                        favModels.isFavorite,
                        favModels.sessionId
                    )
                } else {
                    favoriteDao.insertFavorit(favModels)
                }

            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    fun getFavorite(session_id: String): LiveData<MutableList<FavoriteModel>> = favoriteDao.getFavorit(session_id)

    fun checkIfFavorite(session_id: String, movie_id: String) =
        launch { checkFav(session_id, movie_id) }

    private suspend fun checkFav(session_id: String, movie_id: String) {
        withContext(Dispatchers.IO) {
            isFav.postValue(favoriteDao.checkIfFavorite(session_id, movie_id) == 1)
        }
    }

    fun getMovieDetail(movie_id: String) {
        isMovieLoaded.postValue("loading")
        UtilsApi().getMovieApiService().movieDetail(movie_id)
            .enqueue(object :
                Callback<MovieModel> {
                override fun onResponse(
                    call: Call<MovieModel>,
                    response: Response<MovieModel>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        isMovieLoaded.postValue("success")
                        Handler().postDelayed({
                            movieDetailModel.postValue(data)
                        }, 100)
                    } else {
                        isMovieLoaded.postValue("error")
                    }
                }

                override fun onFailure(call: Call<MovieModel>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isMovieLoaded.postValue("error")
                }
            })
    }

    fun getVideo(movie_id: String) {
        isVideoLoaded.postValue("loading")
        UtilsApi().getMovieApiService().movieVideo(movie_id)
            .enqueue(object :
                Callback<VideoResponse> {
                override fun onResponse(
                    call: Call<VideoResponse>,
                    response: Response<VideoResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        isVideoLoaded.postValue("success")
                        Handler(Looper.getMainLooper()).postDelayed({
                            listVideoModel.postValue(data.results)
                        }, 100)
                    } else {
                        isVideoLoaded.postValue("error")
                    }
                }

                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            application.applicationContext,
                            "request timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isVideoLoaded.postValue("error")
                }
            })
    }

    fun isListMovieLoaded() = isListMovieLoaded
    fun isMovieLoaded() = isMovieLoaded
    fun isVideoLoaded() = isVideoLoaded
    fun isReviewLoaded() = isReviewLoaded
    fun isFav() = isFav
    fun getListMovieUpcomingModels() = listMovieUpcomingModels
    fun getListMoviePopularModels() = listMoviePopularModels
    fun getListMovieTopRatedModels() = listMovieTopRatedModels
    fun getListMovieNowPlayingModels() = listMovieNowPlayingModels
    fun getListMovieSortedModels() = listMovieSortedModels
    fun getMovieDetailModel() = movieDetailModel
    fun getListVideoModels() = listVideoModel
    fun getListReviewModels() = listReviewModels

}