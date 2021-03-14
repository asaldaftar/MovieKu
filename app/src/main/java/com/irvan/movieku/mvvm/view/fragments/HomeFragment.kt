package com.irvan.movieku.mvvm.view.fragments

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.R
import com.irvan.movieku.databinding.FragmentHomeBinding
import com.irvan.movieku.mvvm.models.MovieModel
import com.irvan.movieku.mvvm.view.activities.MainActivity
import com.irvan.movieku.mvvm.view.adapters.MovieAdapter
import com.irvan.movieku.mvvm.viewmodels.MovieViewModel
import com.irvan.movieku.sessions.SessionManager
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: MovieAdapter
    private lateinit var genreAdapter: ArrayAdapter<String>
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var headerTitle: TextView
    private lateinit var headerToolbar: View
    private lateinit var sessionManager: SessionManager
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var filter: String = "default"
    private var genre: String = "0"
    private var genreId: MutableList<String> = mutableListOf()
    private var genreName: MutableList<String> = mutableListOf()
    private var query: HashMap<String, String> = HashMap()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
        subscribeObservers()
        subscribeListeners()
        query.clear()
        query.put("sort_by", "release_date.desc")
        query.put("page", "1")
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dateNow = formatter.format(date)
        query.put("release_date.lte", dateNow)
        movieViewModel.getListMovieSorted(1, query)
        movieViewModel.getMovieGenre()
    }

    private fun initComponents(view: View) {
        sessionManager = SessionManager(requireContext())
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setItemViewCacheSize(20)
        adapter = MovieAdapter()
        binding.recyclerView.adapter = adapter

        genreName.clear()
        genreId.clear()
        genreName.add(requireContext().resources.getString(R.string.loading))
        genreId.add("0")
        genreAdapter =
            ArrayAdapter(requireContext(), R.layout.item_spinner_selected, genreName)
        genreAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)

        binding.headerMain.imgCollection.visibility = View.VISIBLE
        binding.headerMain.imgFav.visibility = View.GONE
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

        movieViewModel.getListMovieUpcomingModels().removeObservers(viewLifecycleOwner)
        movieViewModel.getListMovieUpcomingModels().observe(viewLifecycleOwner, Observer {
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
                movieViewModel.getListMovieUpcomingModels().value = null
            }
        })

        movieViewModel.getListMovieNowPlayingModels().removeObservers(viewLifecycleOwner)
        movieViewModel.getListMovieNowPlayingModels().observe(viewLifecycleOwner, Observer {
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
                movieViewModel.getListMovieNowPlayingModels().value = null
            }
        })

        movieViewModel.getListMoviePopularModels().removeObservers(viewLifecycleOwner)
        movieViewModel.getListMoviePopularModels().observe(viewLifecycleOwner, Observer {
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
                movieViewModel.getListMoviePopularModels().value = null
            }
        })

        movieViewModel.getListMovieTopRatedModels().removeObservers(viewLifecycleOwner)
        movieViewModel.getListMovieTopRatedModels().observe(viewLifecycleOwner, Observer {
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
                movieViewModel.getListMovieTopRatedModels().value = null
            }
        })
        movieViewModel.getListMovieSortedModels().removeObservers(viewLifecycleOwner)
        movieViewModel.getListMovieSortedModels().observe(viewLifecycleOwner, Observer {
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
                movieViewModel.getListMovieSortedModels().value = null
            }
        })

        movieViewModel.getListMovieGenre().removeObservers(viewLifecycleOwner)
        movieViewModel.getListMovieGenre().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                genreId.clear()
                genreName.clear()
                genreName.add("Semua Genre")
                genreId.add("0")
                it.forEach {
                    genreName.add(it.name)
                    genreId.add(it.id)
                }
                genreAdapter.notifyDataSetChanged()
            }

        })
    }

    private fun subscribeListeners() {
        adapter.setOnClickListener(object : MovieAdapter.OnClickListener {
            override fun onClick(model: MovieModel, position: Int) {
                if (mainActivity.detailMovieFragment != null) {
                    mainActivity.supportFragmentManager.beginTransaction()
                        .remove(mainActivity.detailMovieFragment)
                        .commitAllowingStateLoss()
                }
                mainActivity.detailMovieFragment = DetailMovieFragment()
                val bundle = Bundle()
                bundle.putString(
                    DetailMovieFragment.MOVIE_ID,
                    model.id.toString()
                )
                mainActivity.detailMovieFragment.arguments = bundle
                mainActivity.fragmentNavigationHelper.setFragmentNavigation(
                    mainActivity.detailMovieFragment,
                    mainActivity.detailMovieFragment::class.java.simpleName,
                    false
                )
            }

            override fun onClickFav(model: MovieModel, position: Int) {
                //fav
            }

        })

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showFloating()
                    if (runnable != null) {
                        handler.removeCallbacks(runnable!!)
                    }
                    runnable = Runnable { hideFloating() }
                    handler.postDelayed(runnable!!, 3000)

                    val layoutManager = (recyclerView.getLayoutManager() as LinearLayoutManager)
                    val position = layoutManager.findLastVisibleItemPosition()
                    if (!recyclerView.canScrollVertically(1) && position >= 0) {
                        val type = adapter.getItemViewType(position)
                        if (adapter.itemCount > 0) {
                            if (type == 0) {
                                if (filter.equals("now_playing")) {
                                    movieViewModel.nextPageListMovieNowPlaying()
                                } else if (filter.equals("popular")) {
                                    movieViewModel.nextPageListMoviePopular()
                                } else if (filter.equals("top_rated")) {
                                    movieViewModel.nextPageListMovieTopRated()
                                } else if (filter.equals("upcoming")) {
                                    movieViewModel.nextPageListMovieUpcoming()
                                } else {
                                    query.put("sort_by", "release_date.desc")
                                    if (!genre.equals("0")) {
                                        query.put("with_genres", genre)
                                    } else {
                                        val date = Calendar.getInstance().time
                                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                                        val dateNow = formatter.format(date)
                                        query.put("release_date.lte", dateNow)
                                    }
                                    movieViewModel.nextPageListMovieSorted(query)
                                }
                            }
                        }
                    }
                } else {
                    if (runnable != null) {
                        handler.removeCallbacks(runnable!!)
                    }
                    hideFloating()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        binding.headerMain.imgCollection.setOnClickListener {
            if (mainActivity.favoriteFragment != null) {
                mainActivity.supportFragmentManager.beginTransaction()
                    .remove(mainActivity.favoriteFragment)
                    .commitAllowingStateLoss()
            }
            mainActivity.favoriteFragment = FavoriteFragment()
            mainActivity.fragmentNavigationHelper.setFragmentNavigation(
                mainActivity.favoriteFragment,
                mainActivity.favoriteFragment::class.java.simpleName,
                false
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            query.clear()
            if (filter.equals("now_playing")) {
                movieViewModel.getListMovieNowPlaying(1)
            } else if (filter.equals("popular")) {
                movieViewModel.getListMoviePopular(1)
            } else if (filter.equals("top_rated")) {
                movieViewModel.getListMovieTopRated(1)
            } else if (filter.equals("upcoming")) {
                movieViewModel.getListMovieUpcoming(1)
            } else {
                query.put("sort_by", "release_date.desc")
                query.put("page", "1")
                if (!genre.equals("0")) {
                    query.put("with_genres", genre)
                } else {
                    val date = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val dateNow = formatter.format(date)
                    query.put("release_date.lte", dateNow)
                }
                movieViewModel.getListMovieSorted(1, query)
            }
        }

        binding.sortMovie.setOnClickListener {
            popupFilter()
        }

    }

    private fun showFloating() {
        binding.sortMovie.animate()
            .translationY(0.0f)
            .alpha(1.0f)
            .setDuration(200)
    }

    private fun hideFloating() {
        binding.sortMovie.animate()
            .translationY(binding.sortMovie.getHeight().toFloat())
            .alpha(0.0f)
            .setDuration(500)
    }

    private fun popupFilter() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_filter_movie)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        dialog.show()
        val spinnerGenre = dialog.findViewById<Spinner>(R.id.spinnerGenre)

        spinnerGenre.adapter = genreAdapter
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroupSortMovie)
        var filterData = filter

        val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
        if (filterData.equals("now_playing")) {
            radioGroup.check(R.id.radioNowPlaying)
        } else if (filterData.equals("popular")) {
            radioGroup.check(R.id.radioPopular)
        } else if (filterData.equals("top_rated")) {
            radioGroup.check(R.id.radioTopRated)
        } else if (filterData.equals("upcoming")) {
            radioGroup.check(R.id.radioUpcoming)
        } else {
            spinnerGenre.setSelection(genreId.indexOf(genre))
        }

        var isReset = false
        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                filterData = when (checkedId) {
                    R.id.radioNowPlaying -> {
                        "now_playing"
                    }
                    R.id.radioPopular -> {
                        "popular"
                    }
                    R.id.radioTopRated -> {
                        "top_rated"
                    }
                    R.id.radioUpcoming -> {
                        "upcoming"
                    }
                    else -> "default"
                }
                if (!filterData.equals("default")) {
                    spinnerGenre.setSelection(0)
                }
            }

        })

        spinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(isReset){
                    radioGroup.clearCheck()
                }else{
                    isReset = !isReset
                }
                genre = genreId[spinnerGenre.selectedItemPosition]
            }
        }

        dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(dialog: DialogInterface?) {
                if (runnable != null) {
                    handler.removeCallbacks(runnable!!)
                }
                runnable = Runnable { hideFloating() }
                handler.postDelayed(runnable!!, 3000)
            }
        })

        btnSubmit.setOnClickListener {
            filter = filterData
            genre = genreId[spinnerGenre.selectedItemPosition]
            if (BuildConfig.DEBUG) {
                Log.d("FILTER", "popupFilter: btnSubmit")
            }
            query.clear()
            if (filter.equals("now_playing")) {
                movieViewModel.getListMovieNowPlaying(1)
            } else if (filter.equals("popular")) {
                movieViewModel.getListMoviePopular(1)
            } else if (filter.equals("top_rated")) {
                movieViewModel.getListMovieTopRated(1)
            } else if (filter.equals("upcoming")) {
                movieViewModel.getListMovieUpcoming(1)
            } else {
                query.put("sort_by", "release_date.desc")
                query.put("page", "1")
                if (!genre.equals("0")) {
                    query.put("with_genres", genre)
                } else {
                    val date = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val dateNow = formatter.format(date)
                    query.put("release_date.lte", dateNow)
                }
                movieViewModel.getListMovieSorted(1, query)
            }

            if (runnable != null) {
                handler.removeCallbacks(runnable!!)
            }
            runnable = Runnable { hideFloating() }
            handler.postDelayed(runnable!!, 3000)
            dialog.dismiss()
        }
    }

}
