package com.dicoding.submission.adapter

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
import com.dicoding.submission.model.ItemUser

class ListUserAdapter internal constructor() : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(){

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var listUser = emptyList<ItemUser>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemUser)
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userAvatar: ImageView = itemView.findViewById(R.id.user_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val scoreCount: TextView = itemView.findViewById(R.id.score_text)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_user, viewGroup, false)
        return ListViewHolder(view)
    }
    internal fun setListUsers(users : List<ItemUser>) {
        this.listUser = users
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val score = "score: " + listUser[position].score
        holder.scoreCount.text = score
        holder.userName.text = listUser[position].login
        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .error(ColorDrawable(Color.DKGRAY))
            .apply(RequestOptions.circleCropTransform())
            .into(holder.userAvatar)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }

    }

}