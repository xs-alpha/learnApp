package com.xiaosheng.learnapp;

import static com.xiaosheng.learnapp.utils.AppContextUtil.getAppContext;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.eprendre.tingshu.innerSource.LibriVoxSource;
import com.github.eprendre.tingshu.innerSource.YunTing;
import com.github.eprendre.tingshu.sources.CoverUrlExtraHeaders;
import com.github.eprendre.tingshu.sources.TingShu;
import com.github.eprendre.tingshu.utils.Book;
import com.github.eprendre.tingshu.utils.JarSource;
import com.github.eprendre.tingshu.utils.SingletonSourceManage;
import com.google.gson.Gson;
import com.xiaosheng.learnapp.utils.AppContextUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dalvik.system.DexClassLoader;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class Utils {
    private static Book currentBook;
    public static final <T> void loadJarFile(File jarFile, Context context) {
        String string;
        String str;
        Object obj;
        Intrinsics.checkNotNullParameter(jarFile, "jarFile");
        try {
            // 使用 DexClassLoader 加载 jar 文件中的类
            Class<?> loadClass = new DexClassLoader(jarFile.getAbsolutePath(),
                    context.getExternalCacheDir().getAbsolutePath(), null, context.getClassLoader())
                    .loadClass("com.github.eprendre." + FilesKt.getNameWithoutExtension(jarFile) + ".SourceEntry");

            // 获取 getSources 静态方法
            Method declaredMethod = loadClass.getDeclaredMethod("getSources");
            declaredMethod.setAccessible(true);

            if (Modifier.isStatic(declaredMethod.getModifiers())) {
                Log.i("xiaosheng", "getSources is static");
            } else {
                Log.i("xiaosheng", "getSources is not static");
            }

            // 调用静态方法，不需要实例，直接传 null
            Object invoke = declaredMethod.invoke(null);

            // 获取 getDesc 静态方法
            Method declaredMethod2 = loadClass.getDeclaredMethod("getDesc");
            declaredMethod2.setAccessible(true);

            // 调用 getDesc 静态方法
            Object invoke2 = declaredMethod2.invoke(null);
            if (invoke2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
            String str2 = (String) invoke2;

            if (invoke == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<com.github.eprendre.tingshu.sources.TingShu>");
            }

            // 遍历结果并添加到 SingletonSourceManage
            for (TingShu tingShu : (List<TingShu>) invoke) {
                // id是唯一的,判断不包含才进行添加
                Map<String, TingShu> tingshuIdMap = getTingshuIdMap();
                if (! tingshuIdMap.containsKey(tingShu.getSourceId())){
                    SingletonSourceManage.getInstance().add(tingShu);
                }
            }

        } catch (Exception e) {
            Log.e("xiaosheng","error", e);
            e.getCause();
            ToastUtils.showLong("加载出错" + e.getMessage());
        }
    }

    private static Map<String, TingShu> getTingshuIdMap(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return SingletonSourceManage.getInstance().stream().collect(Collectors.toMap(TingShu::getSourceId, v -> v));
        }else {
            HashMap<String, TingShu> retMap = new HashMap<>();
            for (TingShu tingShu : SingletonSourceManage.getInstance()) {
                retMap.put(tingShu.getSourceId(), tingShu);
            }
            return retMap;
        }
    }

    public static boolean containsDefault(CharSequence str, CharSequence substr, boolean ignoreCase, int maxCount) {
        if (str == null || substr == null) {
            return false;
        }

        if (maxCount < 0) {
            throw new IllegalArgumentException("Max count must be non-negative");
        }

        String strStr = str.toString();
        String substrStr = substr.toString();

        if (ignoreCase) {
            strStr = strStr.toLowerCase();
            substrStr = substrStr.toLowerCase();
        }

        int count = 0;
        int index = strStr.indexOf(substrStr);
        while (index != -1 && (maxCount == 0 || count < maxCount)) {
            count++;
            if (maxCount == 0 || count >= maxCount) {
                return true;
            }
            index = strStr.indexOf(substrStr, index + 1);
        }

        return false;
    }

    public static final boolean isSameHost( String str,  String url) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        Intrinsics.checkNotNullParameter(url, "url");
        if (Intrinsics.areEqual(str, url)) {
            return true;
        }
        if (Intrinsics.areEqual(str, "local_book_repo") || Utils.containsDefault(str, "content", false, 2) ){
            return Intrinsics.areEqual(url, "local_book_repo") || Utils.containsDefault(url, "content", false, 2);
        } else if (Intrinsics.areEqual(str, "baiduyun") || Utils.containsDefault(str, "baiduyun", false, 2)) {
            return Intrinsics.areEqual(url, "baiduyun") ||Utils.containsDefault(url, "baiduyun", false, 2);
        } else {
            try {
                return Intrinsics.areEqual(new URI(str).getHost(), new URI(url).getHost());
            } catch (Exception e10) {
                e10.printStackTrace();
                return false;
            }
        }
    }


    public static final TingShu findSourceByBook(Book book) {
        if (book == null) {
            return null;
        }
        String sourceId = book.getSourceId();
        return sourceId == null || sourceId.length() == 0 ? findSourceByUrl(book.getBookUrl()) : findSourceById(book.getSourceId());
    }
    public static final TingShu findSourceById( String str) {
        Object obj;
        Iterator<TingShu> it = SingletonSourceManage.getInstance().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual(((TingShu) obj).getSourceId(), str)) {
                break;
            }
        }
        TingShu tingShu = (TingShu) obj;
        if (ObjectUtils.isEmpty(tingShu)){
            ToastUtils.showLong("该id不存在jar中");
            return null;
        }
        return tingShu  ;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final TingShu findSourceByUrl( String url) {
        TingShu tingShu;
        Intrinsics.checkNotNullParameter(url, "url");
        TingShu tingShu2 = null;
        if (Utils.containsDefault((CharSequence) url, (CharSequence) ".m3u8", false, 2) || Utils.containsDefault((CharSequence) url, (CharSequence) "radio.cn", false, 2)) {
            tingShu = YunTing.INSTANCE;
        } else if (Utils.containsDefault((CharSequence) url, (CharSequence) "www.archive.org", false, 2)) {
            tingShu = LibriVoxSource.INSTANCE;
        } else {
            Iterator<TingShu> it = SingletonSourceManage.getInstance().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                if (Utils.isSameHost(url, ((TingShu) next).getUrl())) {
                    tingShu2 = (TingShu) next;
                    break;
                }
            }
            tingShu = tingShu2;
        }
        return tingShu;
    }

    public static synchronized final Book getCurrentBook() {
        int maxRetries = 1;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                Book currentBook = getBook(); // 假设这是静态getter方法
                if (currentBook == null) {
                    currentBook = getBook();
                }

                if (currentBook != null) {
                    String sourceId = currentBook.getSourceId();
                    if (sourceId == null || sourceId.isEmpty()) {
                        TingShu source = findSourceByBook(currentBook);
                        currentBook.setSourceId(source.getSourceId());
                    }
                }

                return currentBook;
            } catch (Exception e) {
                e.printStackTrace();
                retryCount++;
                // 如果不是最后一次尝试，可以在这里添加一些延迟
                if (retryCount < maxRetries) {
                    try {
                        Thread.sleep(200); // 等待1秒再重试
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        // 在最大重试次数后返回null
        return null;
    }

    private static Book getBook() throws IOException {
        File bookFile = new File(getAppContext().getFilesDir(), "book.json");
        Book temp = null;

        if (bookFile.exists()) {
            Gson gson = new Gson();
            String jsonContent = readFile(bookFile);
            temp = gson.fromJson(jsonContent, Book.class);
            if (temp != null){
                setCurrentBook(temp); // 假设这是静态setter方法
            }
        }
        return temp;
    }

    private static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }
        }
        return content.toString();
    }
    public static void setCurrentBook(Book book) {
        // 设置当前书籍对象为传入的 book
        currentBook = book;

        // 如果 book 不为空，将其持久化保存到本地文件，方便下次使用
        if (book != null) {
            saveCurrentBookToFile(book);
        }
    }

    // 持久化保存当前书籍到本地文件
    private static void saveCurrentBookToFile(Book book) {
        try {
            // 获取本地文件目录
            File bookFile = new File(AppContextUtil.getAppContext().getFilesDir(), "book.json");

            // 使用 Gson 将 Book 对象转为 JSON 并保存到文件中
            Gson gson = new Gson();
            String json = gson.toJson(book);

            // 将 JSON 写入文件
            FilesKt.writeText(bookFile, json, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
