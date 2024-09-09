//package com.xiaosheng.learnapp.adapter
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.xiaosheng.learnapp.fragment.FavorateFragment
//import com.xiaosheng.learnapp.fragment.HistoryFragment
//import com.xiaosheng.learnapp.fragment.SearchFragment
//import com.xiaosheng.learnapp.fragment.ShowFragment
//
//class WechatPageAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa) {
//    val list = mutableListOf<Fragment>();
//
//    init {
//        list.add(HistoryFragment());
//        list.add(SearchFragment());
//        list.add(ShowFragment());
//        list.add(FavorateFragment());
//    }
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return list[position]
//    }
//
//}