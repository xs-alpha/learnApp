package com.xiaosheng.learnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.xiaosheng.learnapp.databinding.LaunchAcivityBinding;
import com.xiaosheng.learnapp.fragment.FavorateFragment;
import com.xiaosheng.learnapp.fragment.HistoryFragment;
import com.xiaosheng.learnapp.fragment.SearchFragment;
import com.xiaosheng.learnapp.fragment.ShowFragment;

import java.util.ArrayList;
import java.util.List;

public class LaunchAcivity extends AppCompatActivity {

    private LaunchAcivityBinding binding;
    List<Fragment> fragmentList;

    BottomNavigationView bottomNavigationView;

//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.launch_acivity);
        binding = binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 将fragment添加到list
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new FavorateFragment());
        fragmentList.add(new HistoryFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new ShowFragment());

        // 初始化,初次进入,显示首页
        showFragment(fragmentList.get(1));

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 底部导航栏的切换事件
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 判断切换到哪了
                switch (item.getItemId()){
                    case R.id.menu_show:
                        showFragment(fragmentList.get(3));
                        break;
                    case R.id.menu_favorate:
                        showFragment(fragmentList.get(0));
                        break;
                    case R.id.menu_history:
                        showFragment(fragmentList.get(1));
                        break;
                    case R.id.menu_search:
                        showFragment(fragmentList.get(2));
                        break;
                }
                return true;
            }
        });
    }


    private void showFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.launch_container, fragment);
        ft.commit();
    }
}