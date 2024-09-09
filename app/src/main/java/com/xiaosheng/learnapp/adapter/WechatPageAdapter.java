package com.xiaosheng.learnapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.xiaosheng.learnapp.fragment.FavorateFragment;
import com.xiaosheng.learnapp.fragment.HistoryFragment;
import com.xiaosheng.learnapp.fragment.SearchFragment;
import com.xiaosheng.learnapp.fragment.ShowFragment;

import java.util.ArrayList;
import java.util.List;

public class WechatPageAdapter extends FragmentStateAdapter {
    private List<Fragment> list = new ArrayList<>();


    public WechatPageAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.list = list;
    }


    public WechatPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        list.add(new FavorateFragment());
        list.add(new HistoryFragment());
        list.add(new SearchFragment());
        list.add(new ShowFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
