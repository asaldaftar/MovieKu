package com.irvan.movieku.mvvm.view.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.databinding.FragmentHomeBinding
import com.irvan.movieku.mvvm.models.MovieModel
import com.irvan.movieku.mvvm.view.adapters.MovieAdapter
import com.irvan.movieku.mvvm.viewmodels.MovieViewModel
import com.irvan.movieku.sessions.SessionManager

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var headerTitle: TextView
    private lateinit var headerToolbar: View
    private lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
        subscribeObservers()
        movieViewModel.getListMovieUpcoming(1)
        subscribeListeners()
    }

    private fun initComponents(view: View) {
        sessionManager = SessionManager(requireContext())
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setItemViewCacheSize(20)
        adapter = MovieAdapter()
        binding.recyclerView.adapter = adapter
    }


    private fun subscribeObservers() {
        movieViewModel.isListMovieLoaded().removeObservers(viewLifecycleOwner)
        movieViewModel.isListMovieLoaded().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.equals("loading")) {
                    if (!movieViewModel.isNextPage) {
                        adapter.displayOnlyLoading()
                    }
                } else if (it.equals("success")) {
                    adapter.hideLoading()
                    if (movieViewModel.isNextPage) {
                        movieViewModel.isNextPage = false
                    }
                    if (movieViewModel.isRequest) {
                        movieViewModel.isRequest = false
                    }
                    if (binding.swipeRefresh.isRefreshing) {
                        binding.swipeRefresh.isRefreshing = false
                    }
                    movieViewModel.isListMovieLoaded().value = null
                } else if (it.equals("error")) {
                    adapter.hideLoading()
                    if (movieViewModel.isNextPage) {
                        movieViewModel.isNextPage = false
                    }
                    if (movieViewModel.isRequest) {
                        movieViewModel.isRequest = false
                    }
                    if (binding.swipeRefresh.isRefreshing) {
                        binding.swipeRefresh.isRefreshing = false
                    }
                    movieViewModel.isListMovieLoaded().value = null
                }
            }
        })

        movieViewModel.getListMovieModels().removeObservers(viewLifecycleOwner)
        movieViewModel.getListMovieModels().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (binding.swipeRefresh.isRefreshing) {
                    binding.swipeRefresh.isRefreshing = false
                }
                if (adapter.getModels().size == 0) {

                    if (!(it.size > 0 && it.size % BuildConfig.LIMIT_PAGINATION.toInt() == 0)) {
                        if (movieViewModel.isQueryExhausted) {
                            movieViewModel.isQueryExhausted = true
                        }
                    }

                    adapter.setModels(it)
                } else {
                    val temporary = adapter.getModels()
                    temporary.addAll(it)
                    if (!(temporary.size > 0 && temporary.size % BuildConfig.LIMIT_PAGINATION.toInt() == 0)) {
                        if (movieViewModel.isQueryExhausted) {
                            movieViewModel.isQueryExhausted = true
                        }
                    }
                    adapter.setModels(temporary)
                }
                movieViewModel.getListMovieModels().value = null
            }
        })


    }

    private fun subscribeListeners() {
        adapter.setOnClickListener(object : MovieAdapter.OnClickListener {
            override fun onClick(model: MovieModel, position: Int) {
               //detail
            }

            override fun onClickFav(model: MovieModel, position: Int) {
                //fav
            }

        })
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    val layoutManager = (recyclerView.getLayoutManager() as LinearLayoutManager)
                    val position = layoutManager.findLastVisibleItemPosition()
                    if (!recyclerView.canScrollVertically(1) && position >= 0) {
                        val type = adapter.getItemViewType(position)
                        if (adapter.itemCount > 0) {
                            if (type == 0) {
                                movieViewModel.nextPageListMovieUpcoming()
                            }
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        binding.headerMain.imgFav.setOnClickListener {
            //list fav
        }

        binding.swipeRefresh.setOnRefreshListener {
            movieViewModel.getListMovieUpcoming(1)
        }

        binding.sortMovie.setOnClickListener {

        }
    }

}
