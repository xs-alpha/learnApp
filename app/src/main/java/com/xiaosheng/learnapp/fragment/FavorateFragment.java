package com.xiaosheng.learnapp.fragment;

import android.graphics.BlurMaskFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.xiaosheng.learnapp.R;
import com.xiaosheng.learnapp.databinding.FragmentFavorateBinding;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;


public class FavorateFragment extends Fragment {

    private FragmentFavorateBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favorate, container, false);
        binding = FragmentFavorateBinding.inflate(inflater, container, false);

        init();
        initBanner();
        return binding.getRoot();

    }

    private void initBanner() {
        //--------------------------简单使用-------------------------------
//        binding.banner.addBannerLifecycleObserver(this)//添加生命周期观察者
//                .setAdapter(new BannerImageAdapter(DataBean.getTestData()) {
//                    @Override
//                    public void onBindView(Object holder, Object data, int position, int size) {
//
//                    }
//                })
//                .setIndicator(new CircleIndicator(getContext()));

        //—————————————————————————如果你想偷懒，而又只是图片轮播————————————————————————
        ArrayList<String> bannerUrls = new ArrayList<>();
        bannerUrls.add("https://cn.bing.com/th?id=OHR.Alesund_ZH-CN9437421934_UHD.jpg");
        bannerUrls.add("https://cn.bing.com/th?id=OHR.CascadesNP_ZH-CN1830542356_1920x1080.jpg");
        bannerUrls.add("https://dailybing.com/api/v1/20240501/ja-jp/MBL");
        binding.banner.setAdapter(new BannerImageAdapter<String>(bannerUrls) {
                    @Override
                    public void onBindView(BannerImageHolder holder, String url, int position, int size) {
                        //图片加载自己实现
                        Glide.with(holder.itemView)
                                .load(url)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                                .into(holder.imageView);
                    }
                })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getContext()));
        //更多使用方法仔细阅读文档，或者查看demo
    }

    private void init() {
        String headUrl = "https://cn.bing.com/th?id=OHR.Alesund_ZH-CN9437421934_UHD.jpg";
        String url = "https://cn.bing.com/th?id=OHR.CascadesNP_ZH-CN1830542356_1920x1080.jpg";
//        binding.img.set
        Glide
                .with(getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.load_img)
                .into(binding.img);
        Glide
                .with(getContext())
                .load(headUrl)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.drawable.load_img)
                .into(binding.headImg);
        Glide
                .with(getContext())
                .load("https://dailybing.com/api/v1/20240501/ja-jp/MBL")
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .placeholder(R.drawable.load_img)
                .into(binding.rImg);
        Glide
                .with(getContext())
                .load("https://dailybing.com/api/v1/20240501/ja-jp/MBL")
                .placeholder(R.drawable.load_img)
                .into(binding.blurImg);
    }
}