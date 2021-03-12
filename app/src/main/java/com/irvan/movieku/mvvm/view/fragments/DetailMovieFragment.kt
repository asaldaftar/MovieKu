package com.irvan.movieku.mvvm.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.irvan.movieku.R
import com.irvan.movieku.databinding.FragmentDetailMovieBinding
import com.irvan.movieku.databinding.FragmentHomeBinding

class DetailMovieFragment : Fragment() {

    private lateinit var binding: FragmentDetailMovieBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

}