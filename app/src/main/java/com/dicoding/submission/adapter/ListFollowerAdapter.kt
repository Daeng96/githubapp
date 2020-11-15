package com.dicoding.submission.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submission.R
import com.dicoding.submission.model.Followers


class ListFollowerAdapter internal constructor(context : Context) :
    RecyclerView.Adapter<ListFollowerAdapter.FollowerViewHolder>() {

    private var follower = emptyList<Followers>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val itemView = inflater.inflate(R.layout.follower_list,parent,false)
        return FollowerViewHolder(itemView)

    }

    internal fun setListFollower(follow : ArrayList<Followers>){
        this.follower = follow
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return follower.size
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {

        Glide.with(holder.itemView.context)
            .load(follower[position].avatarUrl)
            .error(ColorDrawable(Color.DKGRAY))
            .apply(RequestOptions.circleCropTransform())
            .into(holder.itemView.findViewById(R.id.user_image_follower))
       holder.nameFollower.text = follower[position].login

    }

    class FollowerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameFollower: TextView = itemView.findViewById(R.id.follower_name)

    }

}
