package com.irvan.movieku.mvvm.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.R
import com.irvan.movieku.databinding.ItemLoadingBinding
import com.irvan.movieku.databinding.ItemMovieBinding
import com.irvan.movieku.mvvm.models.MovieModel
import com.irvan.movieku.mvvm.view.viewholders.LoadingHolder
import com.irvan.movieku.sessions.SessionManager

class MovieAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var models: MutableList<MovieModel> = mutableListOf()

    private val VIEW_MOVIE = 1
    private val VIEW_LOADING = 0

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setModels(models: MutableList<MovieModel>) {
        this.models = models
        if (this.models.size > 0 && this.models.size % BuildConfig.LIMIT_PAGINATION.toInt() == 0) {
            addLoading()
        } else {
            notifyDataSetChanged()
        }
    }

    fun getModels(): MutableList<MovieModel> {
        return models
    }

    fun displayOnlyLoading() {
        clearData()
        addLoading()
    }

    private fun clearData() {
        models.clear()
        notifyDataSetChanged()
    }

    fun hideLoading() {
        if (isLoading()) {
            if (models.size > 0 && models[models.size - 1].id == -1) {
                models.removeAt(models.size - 1)
            }
            notifyDataSetChanged()
        }
    }

    fun displayLoading() {
        if (!isLoading()) {
            addLoading()
        }
    }

    private fun addLoading() {
        val loading = MovieModel(
            "0", false, "",
            "", intArrayOf(0), -1, "",
            "", "", "", 0,
            0, false, 0, ""
        )
        models.add(loading)
        notifyDataSetChanged()
    }

    private fun isLoading(): Boolean {
        return models.size > 0 && models[models.size - 1].id == -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_MOVIE -> MovieItemHolder(
                ItemMovieBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            else -> LoadingHolder(ItemLoadingBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val model = models[position]
        val sessionManager = SessionManager(viewHolder.itemView.context)
        if (viewHolder.itemViewType == VIEW_MOVIE) {
            val holder = viewHolder as MovieItemHolder
            holder.binding.txtTitle.text = model.title
            holder.binding.rating =
            holder.binding.txtTitle.text = model.title
            holder.binding.txtTitle.text = model.title
            if (model.poster_path != null) {
                Glide.with(holder.itemView.context)
                    .load(BuildConfig.URL_IMG_500 + model.poster_path)
                    .placeholder(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.broken_image
                        )
                    )
                    .error(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.broken_image
                        )
                    )
                    .into(holder.binding.imgCover)
            } else {
                holder.binding.imgCover.setImageResource(R.drawable.broken_image)
            }

            if (onClickListener != null) {
                holder.binding.imgFav.setOnClickListener {
                    onClickListener?.onClickFav(model, position)
                }
                holder.binding.root.setOnClickListener {
                    onClickListener?.onClick(model, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (models[position].id == -1) {
            VIEW_LOADING
        } else {
            VIEW_MOVIE
        }
    }

    override fun getItemId(position: Int): Long = models[position].id.hashCode().toLong()

    override fun getItemCount(): Int = models.size

    class MovieItemHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnClickListener {
        fun onClick(model: MovieModel, position: Int)
        fun onClickFav(model: MovieModel, position: Int)
    }
}