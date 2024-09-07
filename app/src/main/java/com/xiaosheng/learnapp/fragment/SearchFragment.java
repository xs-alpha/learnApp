package com.xiaosheng.learnapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xiaosheng.learnapp.R;
import com.xiaosheng.learnapp.databinding.FragmentSearchBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding =FragmentSearchBinding.inflate(getLayoutInflater());
//
//        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("你点到我了");
//                Toast.makeText(SearchFragment.super.getContext(),"你点到我了", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false);
        // 使用ViewBinding绑定视图
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        init();

        // 返回绑定的视图
        return binding.getRoot();
    }

    private void init() {
        binding.writeBtn.setOnClickListener(view -> {
            String string = binding.writeText.getText().toString();
            // 内部存储
            innerWrite(string);
            Toast.makeText(getContext(), "写入成功", Toast.LENGTH_LONG).show();
        });
        binding.writeOuterBtn.setOnClickListener(view->{
            String string = binding.writeText.getText().toString();
            outerWrite(string);
            Toast.makeText(getContext(), "外部存储写入成功", Toast.LENGTH_LONG).show();
        });
        binding.readOuterBtn.setOnClickListener(view -> {
            binding.showText.setText(innerRead());
            Toast.makeText(getActivity(), "外部存储读取成功", Toast.LENGTH_LONG).show();
        });
        // 设置按钮点击事件
        binding.readBtn.setOnClickListener(view -> {
            binding.showText.setText(innerRead());
            Toast.makeText(getActivity(), "你点到我了", Toast.LENGTH_LONG).show();
        });
    }

    private void outerWrite(String string) {
        String es = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(es)){
            File ed = Environment.getExternalStorageDirectory();
            File file = new File("dataEs.txt");
            try {
                FileOutputStream fs = new FileOutputStream(file);
                fs.write(string.getBytes());
                fs.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private String outerRead() {
        String es = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(es)){
            File ed = Environment.getExternalStorageDirectory();
            File file = new File("dataEs.txt");
            try {
                FileInputStream fs = new FileInputStream(file);
                byte[] bytes = new byte[fs.available()];
                fs.read(bytes);
                fs.close();
                return new String(bytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

    private void innerWrite(String s){
        try {
            FileOutputStream fs = getActivity().openFileOutput("data.txt", Context.MODE_PRIVATE);
            fs.write(s.getBytes());
            fs.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String innerRead(){
        try {
            FileInputStream fs = getActivity().openFileInput("data.txt");
            byte[] bytes = new byte[fs.available()];
            fs.read(bytes);
            return new String(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}