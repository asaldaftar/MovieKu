package com.irvan.movieku.mvvm.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.R
import com.irvan.movieku.databinding.ItemFavoriteBinding
import com.irvan.movieku.helpers.FormatStringHelper
import com.irvan.movieku.mvvm.models.FavoriteModel

class FavoriteAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var models: MutableList<FavoriteModel> = mutableListOf()

    private val VIEW_MOVIE = 1

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setModels(models: MutableList<FavoriteModel>) {
        this.models = models
        notifyDataSetChanged()
    }

    fun getModels(): MutableList<FavoriteModel> {
        return models
    }

    private fun clearData() {
        models.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoriteHolder(
                ItemFavoriteBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val model = models[position]
        if (viewHolder.itemViewType == VIEW_MOVIE) {
            val holder = viewHolder as FavoriteHolder
            holder.binding.txtTitle.text = model.movie_title
            if (model.movie_cover != null) {
                Glide.with(holder.itemView.context)
                    .load(BuildConfig.URL_IMG_500 + model.movie_cover)
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
                holder.binding.root.setOnClickListener {
                    onClickListener?.onClick(model, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
           return VIEW_MOVIE
    }

    override fun getItemId(position: Int): Long = models[position].id.hashCode().toLong()

    override fun getItemCount(): Int = models.size

    class FavoriteHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnClickListener {
        fun onClick(model: FavoriteModel, position: Int)
    }
}