package com.xiaosheng.learnapp.fragment;

import static com.bumptech.glide.Glide.get;
import static com.bumptech.glide.Glide.init;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Initializable;
import com.xiaosheng.learnapp.R;
import com.xiaosheng.learnapp.databinding.FragmentShowBinding;
import com.xiaosheng.learnapp.service.MyService;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;


public class ShowFragment extends Fragment {


    private FragmentShowBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_show, container, false);
        binding = FragmentShowBinding.inflate(inflater, container, false);

        initData();
        return binding.getRoot();
    }

    private void initData() {
        binding.startService.setOnClickListener(v->{
            startService();
            Toast.makeText(getActivity(),"启动服务", Toast.LENGTH_LONG).show();
        });
        binding.stopService.setOnClickListener(v->{
            stopService();
            Toast.makeText(getActivity(),"停止服务", Toast.LENGTH_LONG).show();
        });
        binding.startBro.setOnClickListener(v->{
            startBro();
            Toast.makeText(getActivity(),"启动广播", Toast.LENGTH_LONG).show();
        });
        binding.dynBro.setOnClickListener(v->{
            startBro();
            Toast.makeText(getActivity(),"启动广播", Toast.LENGTH_LONG).show();
        });
        binding.writeCache.setOnClickListener(v->{
            writeToExternalCache();
        });
        binding.readJar.setOnClickListener(v->{
            Toast.makeText(getActivity(),"读jar", Toast.LENGTH_LONG).show();
            loadDexClass(getContext());
//            loadJar(getContext());

        });
    }

    public  void startService(){
        Intent intent = new Intent(getContext(), MyService.class);
        getActivity().startService(intent);
    }
    public void stopService(){
        Intent intent = new Intent(getContext(), MyService.class);
        getActivity().stopService(intent);
    }

    public void bindService(){
        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void unBindService(){
        getActivity().unbindService(conn);
    }
    public void startBro(){
        Intent intent = new Intent();
        // 要和manifest文件一致
        intent.setAction("com.xiaosheng.lea_");
        getActivity().sendBroadcast(intent);
    }


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public void writeToExternalCache() {
        try {
            // 获取外部缓存目录（SD卡上的缓存目录）
            File externalCacheDir = getContext().getExternalCacheDir();

            if (externalCacheDir != null) {
                // 创建要写入的文件
                File cacheFile = new File(externalCacheDir, "myCacheFile.txt");

                // 如果文件不存在，则创建新文件
                if (!cacheFile.exists()) {
                    cacheFile.createNewFile();
                }

                // 写入数据到文件
                FileOutputStream fos = new FileOutputStream(cacheFile);
                String content = "This is a cache file content.";
                fos.write(content.getBytes());
                fos.close();

                Toast.makeText(getContext(), "文件写入外部缓存目录成功: " + cacheFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "无法获取外部缓存目录", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "写入缓存文件失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 加载dex文件中的class，并调用其中的方法
     * 这里由于是加载 jar文件，所以采用DexClassLoader
     * 下面开始加载dex class
     */
    public static void loadDexClass(Context context) {
//        File cacheFile = FileUtils.getCacheDir(context);
        File cacheFile = context.getExternalCacheDir(); // 获取缓存目录
        Log.i("xiaosheng", cacheFile.getName());
//        String internalPath = cacheFile.getAbsolutePath() + File.separator + "sources.jar";
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "sources.dex";
        Log.i("xiaosheng", internalPath);
        File desFile = new File(internalPath);
        if (desFile.exists()) {
            DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, context.getClassLoader());
            try {
                Class libClazz = dexClassLoader.loadClass("com.github.eprendre.sources_by_eprendre.YouShengXiaoShuoBa");
                Constructor<?> localConstructor = libClazz.getDeclaredConstructor();
//
                localConstructor.setAccessible(true);
                Object obj = localConstructor.newInstance();
                Method mMethodWrite = libClazz.getMethod("getSourceId");
                mMethodWrite.setAccessible(true);
                String  str = (String ) mMethodWrite.invoke(obj);
                Toast.makeText(context,"result is " + str,Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(context,"result is error " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadJar(Context context) {
        // 获取应用的外部缓存目录
        File cacheDir = context.getExternalCacheDir();

        // 假设 JAR 文件放置在缓存目录下
        String jarPath = cacheDir.getAbsolutePath() + File.separator + "sources.jar";

        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            Toast.makeText(context, "JAR 文件不存在: " + jarPath, Toast.LENGTH_LONG).show();
            return;
        }

        // 使用 DexClassLoader 加载 JAR 包
        DexClassLoader dexClassLoader = new DexClassLoader(jarPath,
                cacheDir.getAbsolutePath(),
                null,
                context.getClassLoader());

        try {
            // 假设你想加载 JAR 包中的某个类，比如 "com.example.MyClass"
            Class<?> loadedClass = dexClassLoader.loadClass("com.github.eprendre.sources_by_eprendre.YouShengXiaoShuoBa");
            // 获取类的构造函数
            Constructor<?> constructor = loadedClass.getDeclaredConstructor();

            // 使构造函数可访问
            constructor.setAccessible(true);

            // 创建类的实例
            Object instance = constructor.newInstance();

            // 假设类中有一个方法 `doSomething()`
            Method method = loadedClass.getMethod("getSourceId");

            // 调用该方法
            Object invoke = method.invoke(instance);

            Toast.makeText(context, "方法调用成功"+invoke.toString(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "加载 JAR 包或调用方法失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}