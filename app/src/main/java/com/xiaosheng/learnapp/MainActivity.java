package com.xiaosheng.learnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.github.eprendre.tingshu.sources.AudioUrlWebViewExtractor;
import com.github.eprendre.tingshu.sources.AudioUrlWebViewSniffExtractor;
import com.google.android.material.navigation.NavigationBarView;
import com.xiaosheng.learnapp.adapter.WechatPageAdapter;
import com.xiaosheng.learnapp.databinding.ActivityMainBinding;
import com.xiaosheng.learnapp.receiver.DynamicReceiver;
import com.xiaosheng.learnapp.service.MyService;
import com.xiaosheng.learnapp.utils.AppContextUtil;
import com.xiaosheng.learnapp.utils.ThreadUtil;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppContextUtil.initialize(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 设置默认选中的项，比如我们默认选中“收藏”项
        binding.bottomNavigation.setSelectedItemId(R.id.menu_favorate);
        binding.viewPage.setCurrentItem(0, false); // 0 对应的是 "收藏" Fragment

        binding.viewPage.setAdapter(new WechatPageAdapter(this));
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_show:
                    binding.viewPage.setCurrentItem(3,false);
                    break;
                case R.id.menu_favorate:
                    binding.viewPage.setCurrentItem(0,false);
                    break;
                case R.id.menu_history:
                    binding.viewPage.setCurrentItem(1,false);
                    break;
                case R.id.menu_search:
                    binding.viewPage.setCurrentItem(2,false);
                    break;
            }
            return true;
        });

        // 注册动态广播
        registerDynamicReceiver();
        AudioUrlWebViewSniffExtractor.getInstance();
        AudioUrlWebViewExtractor.initWebViewIfNeeded();
    }

    private void registerDynamicReceiver() {
        DynamicReceiver dynamicReceiver = new DynamicReceiver();
        IntentFilter ift = new IntentFilter();
        ift.addAction("com.xiaosheng.lea_");
        registerReceiver(dynamicReceiver, ift);
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.launch_container, fragment);
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadUtil.shutdownThreadPool();
    }
}