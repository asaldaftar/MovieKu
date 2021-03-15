package com.irvan.movieku.mvvm.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.R
import com.irvan.movieku.databinding.FragmentDetailMovieBinding
import com.irvan.movieku.helpers.FormatStringHelper
import com.irvan.movieku.mvvm.models.FavoriteModel
import com.irvan.movieku.mvvm.models.MovieModel
import com.irvan.movieku.mvvm.models.ReviewModel
import com.irvan.movieku.mvvm.models.VideoModel
import com.irvan.movieku.mvvm.view.activities.MainActivity
import com.irvan.movieku.mvvm.view.adapters.ReviewAdapter
import com.irvan.movieku.mvvm.viewmodels.MovieViewModel
import com.irvan.movieku.sessions.SessionManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import java.util.*


class DetailMovieFragment : Fragment() {

    private lateinit var binding: FragmentDetailMovieBinding

    companion object {
        val MOVIE_ID =
            "${BuildConfig.APPLICATION_ID}_${DetailMovieFragment::class.java.simpleName}_MOVIE_ID"
    }

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var adapter: ReviewAdapter
    private lateinit var headerTitle: TextView
    private lateinit var headerToolbar: View
    private lateinit var sessionManager: SessionManager
    private lateinit var formatStringHelper: FormatStringHelper
    private var movieDetailModel: MovieModel? = null
    private var movieId: String? = null
    private var isFav: Boolean = false
    private var query: HashMap<String, String> = HashMap()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        if (requireArguments().containsKey(MOVIE_ID)) {
            movieId = requireArguments().getString(MOVIE_ID)!!
            movieViewModel.getMovieDetail(movieId!!)
        }
        subscribeObservers()
        subscribeListeners()


    }

    private fun initComponents() {
        sessionManager = SessionManager(requireContext())
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        formatStringHelper = FormatStringHelper()
        binding.recyclerReview.setHasFixedSize(true)
        binding.recyclerReview.setItemViewCacheSize(20)
        adapter = ReviewAdapter()
        binding.recyclerReview.adapter = adapter
        binding.headerMain.imgCollection.visibility = View.GONE
        binding.headerMain.imgFav.visibility = View.VISIBLE
    }


    private fun subscribeObservers() {
        movieViewModel.isMovieLoaded().removeObservers(viewLifecycleOwner)
        movieViewModel.isMovieLoaded().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.equals("loading")) {
                    mainActivity.showLoading()
                } else if (it.equals("success")) {
                    movieId?.let { id -> movieViewModel.getVideo(id) }
                } else if (it.equals("error")) {
                    mainActivity.hideLoading()
                    movieViewModel.isMovieLoaded().value = null
                }
            }
        })

        movieViewModel.getMovieDetailModel().removeObservers(viewLifecycleOwner)
        movieViewModel.getMovieDetailModel().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setData(it)
                movieDetailModel = it
            } else {
                mainActivity.hideLoading()
                movieViewModel.getMovieDetailModel().value = null
            }
        })

        movieViewModel.getListVideoModels().removeObservers(viewLifecycleOwner)
        movieViewModel.getListVideoModels().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if(it.size > 0) {
                    setVideo(it)
                    binding.txtLabelTrailer.visibility = View.VISIBLE
                    binding.youtubePlayer.visibility = View.VISIBLE
                }else{
                    binding.txtLabelTrailer.visibility = View.GONE
                    binding.youtubePlayer.visibility = View.GONE
                }
                movieViewModel.getListMovieReview(movieId!!, 1, query)
                movieViewModel.getListVideoModels().value = null
            } else {
                movieViewModel.getListMovieReview(movieId!!, 1, query)
                mainActivity.hideLoading()
            }

        })
        movieViewModel.isReviewLoaded().removeObservers(viewLifecycleOwner)
        movieViewModel.isReviewLoaded().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.equals("loading")) {
                    if (!movieViewModel.isNextPage) {
                        adapter.displayOnlyLoading()
                    }
                } else if (it.equals("success")) {
                    adapter.hideLoading()
                    mainActivity.hideLoading()
                    if (movieViewModel.isNextPage) {
                        movieViewModel.isNextPage = false
                    }
                    if (movieViewModel.isRequest) {
                        movieViewModel.isRequest = false
                    }
                    movieViewModel.isListMovieLoaded().value = null
                } else if (it.equals("error")) {
                    mainActivity.hideLoading()
                    adapter.hideLoading()
                    if (movieViewModel.isNextPage) {
                        movieViewModel.isNextPage = false
                    }
                    if (movieViewModel.isRequest) {
                        movieViewModel.isRequest = false
                    }
                    movieViewModel.isReviewLoaded().value = null
                }
            }
        })

        movieViewModel.getListReviewModels().removeObservers(viewLifecycleOwner)
        movieViewModel.getListReviewModels().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("LOG_DATA", "subscribeObservers: ${it.size}")
                if (it.size > 0) {
                    binding.txtLabelReview.visibility = View.VISIBLE
                    binding.recyclerReview.visibility = View.VISIBLE
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
                } else {
                    binding.txtLabelReview.visibility = View.GONE
                    binding.recyclerReview.visibility = View.GONE
                }
            }
        })

        movieViewModel.isFav().removeObservers(viewLifecycleOwner)
        movieViewModel.isFav().observe(viewLifecycleOwner, Observer {
            isFav = it
            if (it) {
                binding.headerMain.imgFav.setImageResource(R.drawable.ic_baseline_favorite_red)
            } else {
                binding.headerMain.imgFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        })

    }
    private fun subscribeListeners() {
        binding.headerMain.imgIcon.setOnClickListener {
            goHome()
        }
        binding.headerMain.txtHeaderTitle.setOnClickListener {
            goHome()
        }
        binding.headerMain.imgFav.setOnClickListener {
            if(movieId != null && movieDetailModel != null && sessionManager.token != null){
                val fav = FavoriteModel(
                    0, sessionManager.token!!, movieId!!, movieDetailModel!!.poster_path ,
                    movieDetailModel!!.title, !isFav
                )
                movieViewModel.addToFavorite(fav)
                checkFav()
            }
        }

        adapter.setOnClickListener(object : ReviewAdapter.OnClickListener {
            override fun onClick(model: ReviewModel, position: Int) {
                //
            }

        })

        binding.recyclerReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = (recyclerView.getLayoutManager() as LinearLayoutManager)
                    val position = layoutManager.findLastVisibleItemPosition()
                    if (!recyclerView.canScrollVertically(1) && position >= 0) {
                        val type = adapter.getItemViewType(position)
                        if (adapter.itemCount > 0) {
                            if (type == 0) {
                                movieViewModel.nextPageListMovieReview(movieId!!, query)
                            }
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    private fun goHome() {
        if (mainActivity.homeFragment != null) {
            mainActivity.supportFragmentManager.beginTransaction()
                .remove(mainActivity.homeFragment)
                .commitAllowingStateLoss()
        }
        mainActivity.homeFragment = HomeFragment()
        mainActivity.fragmentNavigationHelper.setFragmentNavigation(
            mainActivity.homeFragment,
            mainActivity.homeFragment::class.java.simpleName,
            false
        )
    }


    private fun setVideo(video: MutableList<VideoModel>) {
        lifecycle.addObserver(binding.youtubePlayer)
        binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val videoId = video.get(0).key
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

    private fun setData(movieModel: MovieModel) {
        if (movieModel.poster_path != null) {
            Glide.with(requireContext())
                .load(BuildConfig.URL_IMG_500 + movieModel.poster_path)
                .placeholder(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.broken_image
                    )
                )
                .error(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.broken_image
                    )
                )
                .into(binding.imgPoster)
        } else {
            binding.imgPoster.setImageResource(R.drawable.broken_image)
        }
        if (movieModel.backdrop_path != null) {
            Glide.with(requireContext())
                .load(BuildConfig.URL_IMG_500 + movieModel.backdrop_path)
                .placeholder(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.broken_image
                    )
                )
                .error(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.broken_image
                    )
                )
                .into(binding.imgBackdrop)
        } else {
            binding.imgBackdrop.setImageResource(R.drawable.broken_image)
        }
        binding.tvTitle.text = movieModel.title
        binding.chipMovieYear.text = formatStringHelper.getDateYear(movieModel.release_date)
        if (movieModel.genres != null && movieModel.genres!!.size > 0) {
            binding.chipMovieGenre.text = movieModel.genres!![0].name
        } else {
            binding.chipMovieGenre.visibility = View.GONE
        }
        if(movieModel.vote_average.toFloat() > 0){
            binding.chipMovieRating.text = movieModel.vote_average.toString()
        }else{
            binding.chipMovieRating.visibility = View.GONE
        }
        binding.txtDesc.text = movieModel.overview
        checkFav()
    }

    fun checkFav(){
        movieViewModel.checkIfFavorite(sessionManager.token!!,movieId!!)
    }

}