package com.dicoding.submission.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.submission.R
import com.dicoding.submission.view.FollowFragment
import com.dicoding.submission.view.FollowingFragment

class ViewPagerAdapter(private val mContext: Context, fr: FragmentManager) :
    FragmentPagerAdapter(fr, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(R.string.tab_followers, R.string.tab_following)

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> FollowFragment()
            else -> FollowingFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }
}
