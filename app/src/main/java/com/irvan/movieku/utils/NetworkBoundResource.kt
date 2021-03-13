package com.irvan.movieku.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.apis.apiresponses.*

abstract class NetworkBoundResource<CacheObject, RequestObject> @MainThread constructor(appExecutors: AppExecutors) {

    private val TAG = "NetworkBoundResource"

    private var appExecutors: AppExecutors
    private var results: MediatorLiveData<Resource<CacheObject?>> = MediatorLiveData()

    init {
        this.appExecutors = appExecutors
        init()
    }

    private fun init() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "init: second")
        }
        results.setValue(Resource.loading(null) as Resource<CacheObject?>)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "init: third")
        }
        val dbSource: LiveData<CacheObject> = loadFromDb()
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "init: fourth")
        }

        results.addSource(dbSource) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "init: enterSource")
            }

            results.removeSource(dbSource)
            if (it != null) {
                Log.d(TAG, "init: not null")
                if (shouldFetch(it)) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "init: shouldFetch")
                    }

                    fetchFromNetwork(dbSource)
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "init: else shouldFetch")
                    }

                    results.addSource(dbSource) {
                        setValue(Resource.success(it))
                    }
                }
            } else {
                Log.d(TAG, "init: null")
                fetchFromNetwork(dbSource)
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<CacheObject>) {
        results.addSource(dbSource) { cacheObject: CacheObject ->
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "fetchFromNetwork: 1")
            }
            setValue(Resource.loading(cacheObject))
        }

        val apiResponse: LiveData<ApiResponse<RequestObject>> = createCall()
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "fetchFromNetwork: 2 ${apiResponse.value}")
        }

        results.addSource(apiResponse) {
            results.removeSource(dbSource)
            results.removeSource(apiResponse)
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "fetchFromNetwork: 3")
            }

            if (it is ApiSuccessResponse) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "fetcFronNetwork: ApiSuccessResponse")
                }
                appExecutors.diskIO().execute {
                    saveCallResult(processResponse(it as ApiSuccessResponse<CacheObject>) as RequestObject)
                    appExecutors.mainThread().execute {
                        results.addSource(loadFromDb()) { cacheObject: CacheObject ->
                            setValue(Resource.success(cacheObject))
                        }
                    }
                }
            } else if (it is ApiEmptyResponse) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "fetchFronNetwork: ApiEmptyResponse")
                }
                appExecutors.mainThread().execute {
                    results.addSource(loadFromDb()) {
                        setValue(Resource.success(it))
                    }
                }
            } else if (it is ApiErrorResponse) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "fetchFronNetwork: ApiErrorResponse")
                }
                results.addSource(dbSource) { cacheObject: CacheObject ->
                    setValue(Resource.error(cacheObject, it.errorMessage, it.code))
                }
            } else if (it is ApiTimeoutResponse) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "fetchFronNetwork: ApiTimeoutResponse")
                }
                results.addSource(dbSource) { cacheObject: CacheObject ->
                    setValue(Resource.timeout(cacheObject, it.errorMessage))
                }
            } else if (it is ApiUnAuthorizedResponse) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "fetchFronNetwork: ApiUnAuthorizedResponse")
                }
                results.addSource(dbSource) { cacheObject: CacheObject ->
                    setValue(Resource.unauthorized(cacheObject, it.errorMessage, it.code))
                }
            }
        }
    }

    @WorkerThread
    private fun processResponse(response: ApiSuccessResponse<CacheObject>): CacheObject {
        return response.body
    }

    @MainThread
    private fun setValue(newValue: Resource<CacheObject?>) {
        if (results.value != newValue) {
            results.value = newValue
        }
    }

    @WorkerThread
    protected abstract fun saveCallResult(@NonNull item: RequestObject)

    @MainThread
    protected abstract fun shouldFetch(@Nullable data: CacheObject): Boolean

    @NonNull
    @MainThread
    protected abstract fun loadFromDb(): LiveData<CacheObject>

    @NonNull
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestObject>>

    fun getAsLiveData(): LiveData<Resource<CacheObject?>> = results
}