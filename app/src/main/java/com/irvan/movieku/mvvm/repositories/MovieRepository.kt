package com.irvan.movieku.mvvm.repositories

import android.app.Application
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.irvan.movieku.apis.UtilsApi
import com.irvan.movieku.apis.apiresponses.MovieListResponse
import com.irvan.movieku.mvvm.models.MovieModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class MovieRepository(private val application: Application) : CoroutineScope {
    private val TAG = "MovieRepository"

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    private var isListMovieLoaded: MutableLiveData<String> = MediatorLiveData()
    private var listMovieModels: MutableLiveData<MutableList<MovieModel>> =
        MediatorLiveData()

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
                            listMovieModels.postValue(data.results)
                        }, 100)
                    }else {
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
                        Toast.makeText(application.applicationContext, t.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    isListMovieLoaded.postValue("error")
                }
            })
    }

    fun isListMovieLoaded() = isListMovieLoaded
    fun getListMovieModels() = listMovieModels

}