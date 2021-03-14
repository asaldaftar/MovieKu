package com.irvan.movieku.mvvm.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.R
import com.irvan.movieku.databinding.FragmentDetailMovieBinding
import com.irvan.movieku.databinding.FragmentFavoriteBinding
import com.irvan.movieku.helpers.FormatStringHelper
import com.irvan.movieku.mvvm.models.FavoriteModel
import com.irvan.movieku.mvvm.models.ReviewModel
import com.irvan.movieku.mvvm.view.activities.MainActivity
import com.irvan.movieku.mvvm.view.adapters.FavoriteAdapter
import com.irvan.movieku.mvvm.view.adapters.MovieAdapter
import com.irvan.movieku.mvvm.view.adapters.ReviewAdapter
import com.irvan.movieku.mvvm.viewmodels.MovieViewModel
import com.irvan.movieku.sessions.SessionManager

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var headerTitle: TextView
    private lateinit var headerToolbar: View
    private lateinit var sessionManager: SessionManager
    private lateinit var formatStringHelper: FormatStringHelper
    private lateinit var adapter: FavoriteAdapter
    private lateinit var recyclerView: RecyclerView
    private var models: MutableList<FavoriteModel> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        subscribeObservers()
        subscribeListeners()
    }


    private fun initComponents() {
        sessionManager = SessionManager(requireContext())
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        formatStringHelper = FormatStringHelper()
        binding.headerMain.imgCollection.visibility = View.GONE
        binding.headerMain.imgFav.visibility = View.VISIBLE
        binding.recyclerFavorites.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerFavorites.setHasFixedSize(true)
        binding.recyclerFavorites.setItemViewCacheSize(20)
//        binding.recyclerFavorites.addItemDecoration(GridSpacingItemDecoratorHelper(2, 10, true))
        adapter = FavoriteAdapter()
        binding.recyclerFavorites.adapter = adapter

        binding.headerMain.imgCollection.visibility = View.VISIBLE
        binding.headerMain.imgFav.visibility = View.GONE
    }

    private fun subscribeObservers() {
        movieViewModel.getFavorite(sessionManager.token!!).removeObservers(viewLifecycleOwner)
        movieViewModel.getFavorite(sessionManager.token!!)
            .observe(viewLifecycleOwner, Observer {
                models = it
                adapter.setModels(models)
                adapter.notifyDataSetChanged()
            })
    }

    private fun subscribeListeners() {
        binding.headerMain.imgIcon.setOnClickListener {
            goHome()
        }
        binding.headerMain.txtHeaderTitle.setOnClickListener {
            goHome()
        }

        adapter.setOnClickListener(object : FavoriteAdapter.OnClickListener {
            override fun onClick(model: FavoriteModel, position: Int) {
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
}