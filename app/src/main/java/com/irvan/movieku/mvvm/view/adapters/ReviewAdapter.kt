package com.irvan.movieku.mvvm.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.R
import com.irvan.movieku.databinding.ItemLoadingBinding
import com.irvan.movieku.databinding.ItemReviewBinding
import com.irvan.movieku.helpers.FormatStringHelper
import com.irvan.movieku.mvvm.models.ReviewModel
import com.irvan.movieku.mvvm.view.viewholders.LoadingHolder

class ReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var models: MutableList<ReviewModel> = mutableListOf()
    private var stringHelper: FormatStringHelper = FormatStringHelper()

    private val VIEW_REVIEW = 1
    private val VIEW_LOADING = 0

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setModels(models: MutableList<ReviewModel>) {
        this.models = models
        if (this.models.size > 0 && this.models.size % BuildConfig.LIMIT_PAGINATION.toInt() == 0) {
            addLoading()
        } else {
            notifyDataSetChanged()
        }
    }

    fun getModels(): MutableList<ReviewModel> {
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
            if (models.size > 0 && models[models.size - 1].id.equals("-1")) {
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
        val loading = ReviewModel(
            "-1", "", null,
            "","","",""
        )
        models.add(loading)
        notifyDataSetChanged()
    }

    private fun isLoading(): Boolean {
        return models.size > 0 && models[models.size - 1].id.equals("-1")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_REVIEW -> ReviewItemHolder(
                ItemReviewBinding.inflate(
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
        if (viewHolder.itemViewType == VIEW_REVIEW) {
            val holder = viewHolder as ReviewItemHolder
            holder.binding.txtFullname.text = model.author
            holder.binding.txtComment.text = model.content
            holder.binding.txtDateCreated.text = stringHelper.convertDateToIndo(model.createdAt)
            holder.binding.txtRating.text = model.authorDetails?.rating.toString()
            if (model.authorDetails?.avatar_path != null) {
                Glide.with(holder.itemView.context)
                    .load(BuildConfig.URL_IMG_500 + model.authorDetails?.avatar_path)
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
                    .into(holder.binding.imgUser)
            } else {
                holder.binding.imgUser.setImageResource(R.drawable.broken_image)
            }

            if (onClickListener != null) {
                holder.binding.root.setOnClickListener {
                    onClickListener?.onClick(model, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (models[position].id.equals("-1")) {
            VIEW_LOADING
        } else {
            VIEW_REVIEW
        }
    }

    override fun getItemId(position: Int): Long = models[position].id.hashCode().toLong()

    override fun getItemCount(): Int = models.size

    class ReviewItemHolder(val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnClickListener {
        fun onClick(model: ReviewModel, position: Int)
    }
}