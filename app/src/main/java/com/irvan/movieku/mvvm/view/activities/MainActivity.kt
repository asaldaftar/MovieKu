package com.irvan.movieku.mvvm.view.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.irvan.movieku.R
import com.irvan.movieku.databinding.ActivityMainBinding
import com.irvan.movieku.helpers.FragmentNavigationHelper
import com.irvan.movieku.mvvm.view.fragments.DetailMovieFragment
import com.irvan.movieku.mvvm.view.fragments.FavoriteFragment
import com.irvan.movieku.mvvm.view.fragments.HomeFragment
import com.irvan.movieku.sessions.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var fragmentNavigationHelper: FragmentNavigationHelper
    lateinit var homeFragment: HomeFragment
    lateinit var favoriteFragment: FavoriteFragment
    lateinit var detailMovieFragment: DetailMovieFragment
    lateinit var sessionManager: SessionManager
    private var loading: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initComponents()
        initFragments()
        if(!sessionManager.readLoginStatus()){
            sessionManager.setUserSession(sessionManager.generateSession())
        }
        fragmentNavigationHelper.setFragmentNavigation(
            homeFragment,
            homeFragment.javaClass.simpleName,
            true
        )
    }

    private fun initComponents() {
        sessionManager = SessionManager(this)
        fragmentNavigationHelper = FragmentNavigationHelper(R.id.mainContainer, this)
    }

    private fun initFragments() {
        homeFragment = HomeFragment()
        favoriteFragment = FavoriteFragment()
        detailMovieFragment = DetailMovieFragment()
    }

    override fun onBackPressed() {
        if (fragmentNavigationHelper.countBackstact() == 1 && !fragmentNavigationHelper.checkVisibilityFragment()
                .equals(homeFragment.javaClass.simpleName)
        ) {
            fragmentNavigationHelper.setFragmentNavigation(
                homeFragment,
                homeFragment.javaClass.simpleName,
                true
            )
        } else {
            fragmentNavigationHelper.backstackFragment(true)
        }
    }

    fun showLoading() {
        if (loading == null) {
            loading = ProgressDialog.show(this, null, "Harap Tunggu ...", true, false)
        }
    }

    fun hideLoading() {
        if (loading != null) {
            loading!!.dismiss()
            loading = null
        }
    }
}