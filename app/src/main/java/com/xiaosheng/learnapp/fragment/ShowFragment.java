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
import com.github.eprendre.tingshu.sources.AudioUrlExtractor;
import com.github.eprendre.tingshu.sources.TingShu;
import com.github.eprendre.tingshu.utils.Book;
import com.github.eprendre.tingshu.utils.BookDetail;
import com.github.eprendre.tingshu.utils.SingletonSourceManage;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Produce;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.xiaosheng.learnapp.R;
import com.xiaosheng.learnapp.Utils;
import com.xiaosheng.learnapp.databinding.FragmentShowBinding;
import com.xiaosheng.learnapp.service.MyService;
import com.xiaosheng.learnapp.utils.AppContextUtil;
import com.xiaosheng.learnapp.utils.SpUtil;
import com.xiaosheng.learnapp.utils.ThreadUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;
import kotlin.Pair;


public class ShowFragment extends Fragment {


    private FragmentShowBinding binding;

    @Override
    public void onStart() {
        super.onStart();
        RxBus.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

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
        binding.startService.setOnClickListener(v -> {
            startService();
            Toast.makeText(getActivity(), "启动服务", Toast.LENGTH_LONG).show();
        });
        binding.stopService.setOnClickListener(v -> {
            stopService();
            Toast.makeText(getActivity(), "停止服务", Toast.LENGTH_LONG).show();
        });
        binding.startBro.setOnClickListener(v -> {
            startBro();
            Toast.makeText(getActivity(), "启动广播", Toast.LENGTH_LONG).show();
        });
        binding.dynBro.setOnClickListener(v -> {
            startBro();
            Toast.makeText(getActivity(), "启动广播", Toast.LENGTH_LONG).show();
        });
        binding.writeCache.setOnClickListener(v -> {
            writeToExternalCache();
        });
        binding.procBtn.setOnClickListener(v->{
            RxBus.get().post("xiaosheng", binding.searchTxt.getText().toString());
        });
        binding.readJar.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "读jar", Toast.LENGTH_LONG).show();
//            loadDexClass(getContext());
//                copyDexElements(getContext());
            ThreadUtil.executeTask(()->{
                File file = new File(getContext().getExternalCacheDir(), "sources_by_eprendre.jar");
                Utils.loadJarFile(file, getContext());
            },"加载jar包");
        });
        binding.search.setOnClickListener(v->{
            String string = binding.searchTxt.getText().toString();
            ThreadUtil.executeTask(()->{
                for (TingShu tingShu : SingletonSourceManage.getInstance()) {

                    if (!tingShu.isSearchable()){
                        // 不可搜索就下一个
                        continue;
                    }
                    Log.i("xiaosheng", "开始搜索"+tingShu.getName());
                    Pair<List<Book>, Integer> search;
                    try {
                        search = tingShu.search(string, 0);
                    }catch (Exception e){
//                        Log.i("xiaosheng", tingShu.getName()+"搜索失败"+e.getMessage());
                        Log.e("xiaosheng","error",e);
                        continue;
                    }
                    List<Book> books = search.component1();
                    Integer i = search.component2();
                    Log.i("xiaosheng", i+"");
                    StringBuffer sb = new StringBuffer();
                    for (Book book : books) {
                        sb.append(book.getTitle());
                        sb.append(book.getAuthor());
                        sb.append(book.getStatus());
                        sb.append(book.getCoverUrl());
                        sb.append(book.getPlaySpeed());
                        sb.append(book.getCurrentEpisodeUrl());
                        sb.append(" bookUrl:"+book.getBookUrl());
                        sb.append("  episode:"+ new Gson().toJson(book.getEpisodeList()));

                        try {
                            AudioUrlExtractor audioUrlExtractor = tingShu.getAudioUrlExtractor();
                            audioUrlExtractor.extract(book.getBookUrl(),true,true,true);
                            BookDetail bookDetailInfo = tingShu.getBookDetailInfo(book.getBookUrl(), true, true);
                            sb.append("bookDetail:"+new Gson().toJson(bookDetailInfo));
                        }catch (Exception e){
                            e.getCause();
                        }

                        sb.append("\n");
                    }
                    Log.i("xiaosheng", sb.toString());
                    getContext();
                    getActivity().runOnUiThread(()->{
                        binding.showInf.setText(binding.showInf.getText()+sb.toString());
                    });
                }
            }, "搜索");
        });


    }

//    @Subscribe(tags = @Tag("xiaosheng"))
    @Subscribe
    public void consumeProduce(String str){
        binding.consuBtn.setText(str);
        Log.i("logg",str);
    }

    public void startService() {
        Intent intent = new Intent(getContext(), MyService.class);
        getActivity().startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(getContext(), MyService.class);
        getActivity().stopService(intent);
    }

    public void bindService() {
        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void unBindService() {
        getActivity().unbindService(conn);
    }

    public void startBro() {
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
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "sources.jar";
//        String internalPath = cacheFile.getAbsolutePath() + File.separator + "sources.dex";
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
                String str = (String) mMethodWrite.invoke(obj);
                Toast.makeText(context, "result is " + str, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(context, "result is error " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

            Toast.makeText(context, "方法调用成功" + invoke.toString(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "加载 JAR 包或调用方法失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 动态加载dex，并将dex合并到应用加载器中
     *
     * @param ctx Context对象
     */
    private void copyDexElements(Context ctx) {
        File file = new File(ctx.getExternalCacheDir(), "sources.jar");
        if (!file.exists()) {
            Log.i("Test", "Dex file not exists, file: " + file.getAbsolutePath());
            return;
        }
        //含有dex的jar或apk文件的路径
        String dexPath = file.getAbsolutePath();

        File dir = new File(ctx.getCacheDir(), "optDir");
        if (!dir.exists()) {
            dir.mkdir();
        }
        //优化后的dex文件存放的目录的路径
        String optimizedDirectory = dir.getAbsolutePath();

        try {
            //应用的加载器
            ClassLoader pathClassLoader = ctx.getClassLoader();
            //动态加载器，加载的dex文件
            //因为没有从外部引入so文件，所以第3个参数为null
            DexClassLoader dexClassLoader = new DexClassLoader(dexPath, optimizedDirectory, null, pathClassLoader);

            //1, 获取DexClassLoader的pathList
            Field dexPathListField = getField(dexClassLoader.getClass(), "pathList");
            Object dexPathList = dexPathListField.get(dexClassLoader);
            if (dexPathList == null) {
                Log.i("Test", "Fail to get pathList from DexClassLoader, dexClassLoader: " + dexClassLoader);
                return;
            }

            //2, 获取pathList的dexElements
            Field dexDexElementsField = getField(dexPathList.getClass(), "dexElements");
            Object[] dexDexElements = (Object[]) dexDexElementsField.get(dexPathList);
            if (dexDexElements == null) {
                Log.i("Test", "Fail to get dexElements from pathList in DexClassLoader, dexClassLoader: " + dexClassLoader);
                return;
            }

            if (dexDexElements.length == 0) {
                Log.i("Test", "The size of dexElements from pathList in DexClassLoader is 0, dexClassLoader: " + dexClassLoader);
                return;
            }

            //3, 获取应用加载器PathClassLoader的pathList
            Field pathPathListField = getField(pathClassLoader.getClass(), "pathList");
            Object pathPathList = pathPathListField.get(pathClassLoader);
            if (pathPathList == null) {
                Log.i("Test", "Fail to get pathList from application ClassLoader, classLoader: " + pathClassLoader);
                return;
            }

            //4, 获取应用加载器的pathList的dexElements
            Field pathDexElementsField = getField(pathPathList.getClass(), "dexElements");
            Object[] pathDexElements = (Object[]) pathDexElementsField.get(pathPathList);
            if (pathDexElements == null) {
                Log.i("Test", "Fail to get dexElements from pathList in application ClassLoader, classLoader: " + pathClassLoader);
                return;
            }

            //5, 创建新数组，并复制Element到新数组
            //创建长度为dexDexElements.length + pathDexElements.length的Element[]
            Object[] newDexElements = (Object[]) Array.newInstance(pathDexElements.getClass().getComponentType(), dexDexElements.length + pathDexElements.length);
            //将动态加载的dexElements复制到newDexElements，范围：[0，dexDexElements.length-1]
            System.arraycopy(dexDexElements, 0, newDexElements, 0, dexDexElements.length);
            //将应用的dexElements复制到newDexElements，范围：[dexDexElements.length, n]
            System.arraycopy(pathDexElements, 0, newDexElements, dexDexElements.length, pathDexElements.length);

            //6，将newDexElements设置给应用加载器的pathList的dexElements
//            pathDexElementsField.set(pathClassLoader, newDexElements);
            pathDexElementsField.set(pathPathList, newDexElements);
            testJarLoaded("com.github.eprendre.sources_by_eprendre.YouShengXiaoShuoBa");
            testJarLoaded("com.github.eprendre.sources_by_eprendre.HaiYangTingShu");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("xiaosheng", e.getMessage());
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "失败了" + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }

    /**
     * 获取Class中指定名称的属性Field
     *
     * @param clazz     Class对象
     * @param fieldName 属性名称
     * @return 属性Field对象
     * @throws NoSuchFieldException 如果未查找到对应属性，则抛出
     */
    private Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        Field field = null;
        String classStr = clazz.toString();
        //从子类向父类循环查找Field
        while (clazz != null) {
            try {
                Log.i("Test", "parent class = " + clazz);
                field = clazz.getDeclaredField(fieldName);
                if (field != null) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    return field;
                }
            } catch (Exception e) {
                //忽略错误信息
                //e.printStackTrace();
            }
            //获取父类字节码，从父类查找
            clazz = clazz.getSuperclass();
        }

        //如果未查找到，则抛出异常
        if (field == null) {
            throw new NoSuchFieldException("field: " + fieldName + " not in " + classStr);
        }

        return field;
    }

    private void testJarLoaded(String className) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> loadedClass = classLoader.loadClass(className);

            if (loadedClass != null) {
                Log.i("Test", "Class " + className + " loaded successfully!");

                // 通过反射获取无参构造方法
                Constructor<?> constructor = loadedClass.getDeclaredConstructor();

                // 设置构造方法为可访问的
                constructor.setAccessible(true);

                // 创建实例
                Object instance = constructor.newInstance();
                Log.i("Test", "Instance of " + className + " created successfully!");
                Method mMethodWrite = loadedClass.getMethod("getSourceId");
                mMethodWrite.setAccessible(true);
                String str = (String) mMethodWrite.invoke(instance);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "result is " + str, Toast.LENGTH_SHORT).show();
                });
                // 进一步测试方法调用等
            } else {
                Log.i("Test", "Class " + className + " could not be loaded.");
            }
        } catch (ClassNotFoundException e) {
            Log.e("Test", "Class " + className + " not found: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("Test", "No suitable constructor found for class " + className + ": " + e.getMessage());
        } catch (InstantiationException e) {
            Log.e("Test", "Failed to instantiate class " + className + ": " + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("Test", "Illegal access when trying to instantiate class " + className + ": " + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("Test", "Constructor threw an exception for class " + className + ": " + e.getMessage());
        } catch (Exception e) {
            Log.e("Test", "Unexpected error: " + e.getMessage());
        }
    }


}