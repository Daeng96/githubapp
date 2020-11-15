package com.dicoding.submission.adapter

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
import com.dicoding.submission.model.Following


class ListFollowingAdapter internal constructor( ) :
    RecyclerView.Adapter<ListFollowingAdapter.FollowerViewHolder>() {
    private var following = emptyList<Following>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.follower_list, parent, false)
        return FollowerViewHolder(view)
    }

    internal fun setListFollowing(following : ArrayList<Following>){
        this.following = following
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return following.size
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {

        Glide.with(holder.itemView.context)
            .load(following[position].avatarUrl)
            .error(ColorDrawable(Color.DKGRAY))
            .apply(RequestOptions.circleCropTransform())
            .into(holder.itemView.findViewById(R.id.user_image_follower))
       holder.nameFollower.text = following[position].login

    }

    class FollowerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameFollower: TextView = itemView.findViewById(R.id.follower_name)

    }

}
