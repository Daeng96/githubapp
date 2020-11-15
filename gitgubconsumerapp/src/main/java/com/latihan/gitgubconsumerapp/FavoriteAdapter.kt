package com.latihan.gitgubconsumerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavoriteAdapter internal constructor(context: Context ):
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var listener: ((Favorites)->Unit)? = null

    fun click(listener: ((Favorites)-> Unit)){
        this.listener = listener
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorites)
    }
    private var listFavorite = emptyList<Favorites>()
    class FavoriteViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userAvatar: ImageView = itemView.findViewById(R.id.user_image)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_unfavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.userName.text = listFavorite[position].Login
        Glide.with(holder.itemView.context)
            .load(listFavorite[position].avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.userAvatar)
        holder.btnDelete.setOnClickListener { onItemClickCallback.onItemClicked(listFavorite[holder.adapterPosition]) }
        holder.itemView.setOnClickListener{listener?.invoke(listFavorite[position])}
    }

    internal fun setListFavorite(listFavorite: List<Favorites>) {
        this.listFavorite = listFavorite
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
       return listFavorite.size
    }
}