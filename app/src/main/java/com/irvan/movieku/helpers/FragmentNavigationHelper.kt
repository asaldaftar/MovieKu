package com.irvan.movieku.helpers

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.mvvm.models.FragmentModel
import com.irvan.movieku.mvvm.view.fragments.DetailMovieFragment
import com.irvan.movieku.mvvm.view.fragments.FavoriteFragment
import com.irvan.movieku.mvvm.view.fragments.HomeFragment

class FragmentNavigationHelper(val idContainer: Int, val activity: AppCompatActivity) {
    private val TAG = "FragmentNavigation"

    private var fragmentTags: MutableList<String> = mutableListOf()
    private val fragmentModels: MutableList<FragmentModel> = mutableListOf()

    fun setFragmentNavigation(fragment: Fragment, tag: String, isCLear: Boolean) {
        if (isCLear) {
            fragmentTags.clear()
            fragmentTags = mutableListOf()
        }
        if (fragmentTags.indexOf(tag) == -1 && !fragment.isAdded) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "setFragmentNavigation: not added ")
            }
            val transaction: FragmentTransaction =
                activity.getSupportFragmentManager().beginTransaction()
            transaction.add(idContainer, fragment, tag)
            transaction.commit()
            fragmentTags.add(tag)
            fragmentModels.add(FragmentModel(fragment, tag))
        } else if (fragmentTags.indexOf(tag) == -1 && fragment.isAdded) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "setFragmentNavigation: added ")
            }
            fragmentTags.add(tag)
            fragmentModels.add(FragmentModel(fragment, tag))
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "setFragmentNavigation: same ")
            }
            val indexTag = fragmentTags.indexOf(tag)
            if (fragment is DetailMovieFragment || fragment is FavoriteFragment || fragment is HomeFragment) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "setFragmentNavigation: same specific")
                }
                val transaction: FragmentTransaction =
                    activity.supportFragmentManager.beginTransaction()
                transaction.add(idContainer, fragment, tag)
                transaction.commit()
                fragmentTags.removeAt(indexTag)
                fragmentTags.add(tag)
                fragmentModels.removeAt(indexTag)
                fragmentModels.add(FragmentModel(fragment, tag))
            } else {
                fragmentTags.remove(tag)
                fragmentTags.add(tag)

                fragmentModels.removeAt(indexTag)
                fragmentModels.add(FragmentModel(fragment, tag))
            }
        }
        if (BuildConfig.DEBUG) {
            checkStack()
        }
        setFragmentVisibilities(tag)
    }

    fun checkStack() {
        var i = 1
        fragmentTags.forEach {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "checkStack: ${i}. ${it}")
            }
            i++
        }
    }

    fun setFragmentNavigationWithClearPreviousStack(fragment: Fragment, tag: String) {
        setFragmentNavigation(fragment, tag, false)
        val backStackCount: Int = fragmentTags.size
        val indexPreviousFragment = backStackCount - 2
        if (indexPreviousFragment > 0) {
            fragmentTags.removeAt(indexPreviousFragment)
        }
    }

    fun setFragmentVisibilities(tagname: String) {
        for (i in fragmentModels.indices) {
            if (tagname == fragmentModels.get(i).tag) {
                val transaction: FragmentTransaction =
                    activity.getSupportFragmentManager().beginTransaction()
                fragmentModels.get(i).fragment?.let { transaction.show(it) }
                transaction.commit()
            } else {
                val transaction: FragmentTransaction =
                    activity.getSupportFragmentManager().beginTransaction()
                fragmentModels.get(i).fragment?.let { transaction.hide(it) }
                transaction.commit()
            }
        }
    }

    fun backstackFragment(isMainActivity: Boolean) {
        val backStackCount = fragmentTags.size
        if (backStackCount > 1) {
            val indexTopTag = backStackCount - 1
            val topFragmentModel = fragmentModels[indexTopTag]
            val topFragmentTag = fragmentTags[indexTopTag]
            val newTopFragmentTag = fragmentTags[backStackCount - 2]
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "backstackFragment: $newTopFragmentTag")
            }
            setFragmentVisibilities(newTopFragmentTag)
            fragmentTags.removeAt(indexTopTag)
            fragmentModels.removeAt(indexTopTag)
            checkStack()
        } else if (backStackCount == 1) {
            checkStack()
            if (isMainActivity) {
                val snackbar: Snackbar = Snackbar
                    .make(
                        activity.findViewById(android.R.id.content),
                        "Apakah ingin menutup aplikasi ?",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction("TUTUP", { view ->
                        destroy()
                    })
                snackbar.show()
            } else {
                destroy()
            }
        }
    }

    private fun destroy() {
        activity.finish()
    }

    fun checkVisibilityFragment(): String {
        val backStackCount = fragmentTags.size
        val indexTopTag = backStackCount - 1
        return fragmentTags[indexTopTag]
    }

    fun countBackstact(): Int {
        return fragmentTags.size
    }
}