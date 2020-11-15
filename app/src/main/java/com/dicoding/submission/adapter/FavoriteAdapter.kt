package com.dicoding.submission.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submission.R
import com.dicoding.submission.db.Favorites

class FavoriteAdapter internal constructor(context: Context?):
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var listener: ((Favorites)->Unit)? = null

    fun click(listener: ((Favorites)->Unit)){
        this.listener = listener
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorites)
    }
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var favorite = emptyList<Favorites>()

    class FavoriteViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userAvatar: ImageView = itemView.findViewById(R.id.user_image)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_unfavorite)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = inflater.inflate(R.layout.list_favorite,viewGroup,false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.userName.text = favorite[position].login
        Glide.with(holder.itemView.context)
            .load(favorite[position].avatarUrl)
            .placeholder(ColorDrawable(Color.DKGRAY))
            .apply(RequestOptions.circleCropTransform())
            .into(holder.userAvatar)
        holder.btnDelete.setOnClickListener { onItemClickCallback.onItemClicked(favorite[holder.adapterPosition]) }
        holder.itemView.setOnClickListener{listener?.invoke(favorite[position])}
    }

    internal fun setListFavorite(favorite : List<Favorites>) {
        this.favorite = favorite
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return favorite.size
    }
}