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


public class FavorateFragment extends Fragment {

    private FragmentFavorateBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favorate, container, false);
        binding = FragmentFavorateBinding.inflate(inflater, container, false);

        init();
        return binding.getRoot();

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