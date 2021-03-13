package com.irvan.movieku.mvvm.repositories

import android.app.Application
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.irvan.movieku.apis.UtilsApi
import com.irvan.movieku.apis.apiresponses.ApiResponse
import com.irvan.movieku.apis.apiresponses.GenreListResponse
import com.irvan.movieku.apis.apiresponses.MovieListResponse
import com.irvan.movieku.database.AppDatabase
import com.irvan.movieku.database.dao.GenreDao
import com.irvan.movieku.mvvm.models.GenreModel
import com.irvan.movieku.mvvm.models.MovieModel
import com.irvan.movieku.utils.AppExecutors
import com.irvan.movieku.utils.NetworkBoundResource
import com.irvan.movieku.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class MovieRepository(private val application: Application) : CoroutineScope {
    private val TAG = "MovieRepository"

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    private var genreDao: GenreDao
    private var isListMovieLoaded: MutableLiveData<String> = MediatorLiveData()
    private var listMovieUpcomingModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()
    private var listMovieTopRatedModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()
    private var listMovieNowPlayingModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()
    private var listMoviePopularModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()
    private var listMovieSortedModels: MutableLiveData<MutableList<MovieModel>> = MediatorLiveData()


    init {
        val db = AppDatabase.getInstance(application)
        genreDao = db!!.genreDao()
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

    fun getMovieGenre() = genreDao.getGenre()

    fun getMovieGenreApi(){
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

    fun isListMovieLoaded() = isListMovieLoaded
    fun getListMovieUpcomingModels() = listMovieUpcomingModels
    fun getListMoviePopularModels() = listMoviePopularModels
    fun getListMovieTopRatedModels() = listMovieTopRatedModels
    fun getListMovieNowPlayingModels() = listMovieNowPlayingModels
    fun getListMovieSortedModels() = listMovieSortedModels

}