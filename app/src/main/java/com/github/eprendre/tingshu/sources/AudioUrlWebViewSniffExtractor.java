package com.github.eprendre.tingshu.sources;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import com.github.eprendre.tingshu.extensions.MyExtKt;
import com.github.eprendre.tingshu.widget.RxEvent;
import com.hwangjr.rxbus.RxBus;
import com.xiaosheng.learnapp.Utils;
import com.xiaosheng.learnapp.utils.AppContextUtil;
import com.xiaosheng.learnapp.utils.SpManage;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import kotlin.jvm.functions.Function1;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Keep
public class AudioUrlWebViewSniffExtractor implements AudioUrlExtractor {
    public static volatile AudioUrlWebViewSniffExtractor INSTANCE ;
    private CompositeDisposable compositeDisposable;
    private WebView webView;
    private static boolean isAutoPlay = true;
    private static String episodeUrl = "";
    private static String userAgent = "";
    private static String script = "";
    private static boolean isScriptExecuted = true;
    private static Handler handler = new Handler();
    private static StringBuilder logBuilder = new StringBuilder();
    private static List<String> extList;
    private static boolean isAudioGet;
    private static boolean isCache;
    private static boolean isCompatibleMode;
    private static boolean isDebug;
    private static boolean isError;
    private static boolean isPageFinished;
    private static AudioUrlValidator validateAudioUrl;

    public static AudioUrlWebViewSniffExtractor getInstance() {
        if (INSTANCE == null) {
            synchronized (AudioUrlWebViewSniffExtractor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AudioUrlWebViewSniffExtractor();
                }
            }
        }
        return INSTANCE;
    }

    private AudioUrlWebViewSniffExtractor() {
        compositeDisposable = new CompositeDisposable();
        initWebView();
        extList = new ArrayList<>(Arrays.asList(".m3u8", ".ts", ".mp3", ".m4a", ".m4b", ".flac", ".aa3", ".ogg", ".wma", ".wav", ".aac", ".ac3", ".mpg", ".mpeg", ".avi", ".vob", ".wmv", ".asf", ".rm", ".rmvb", ".mov", ".mkv", ".flv", ".mp4"));
    }

    private void initWebView() {
        // Ensure WebView and Handler are initialized in the main thread
        if (Looper.myLooper() == Looper.getMainLooper()) {
            createWebView();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    createWebView();
                }
            });
        }
    }
    // 手动添加 setUp$default 方法以模拟 Kotlin 编译器生成的行为
    public static void setUp(AudioUrlWebViewSniffExtractor instance, boolean isDesktop, AudioUrlValidator validator, int mask, Object obj) {
        if ((mask & 1) != 0) {
            isDesktop = false;
        }
        if ((mask & 2) != 0) {
            validator = null;
        }
        instance.setUp(isDesktop, validator);
    }

    public static void setUp$default(AudioUrlWebViewSniffExtractor instance, boolean isDesktop, Function1<String, Boolean> validator, int mask, Object ignored) {
        if ((mask & 1) != 0) {
            isDesktop = false; // 默认值
        }
        AudioUrlValidator javaValidator = null;
        if ((mask & 2) == 0 && validator != null) {
            javaValidator = new AudioUrlValidator() {
                @Override
                public boolean isValid(String url) {
                    return validator.invoke(url);
                }
            };
        }
        instance.setUp(isDesktop, javaValidator);
    }

//    public static void setUp$default(AudioUrlWebViewSniffExtractor instance, boolean isDesktop, AudioUrlValidator validator, int mask, Object ignored) {
//        if ((mask & 1) != 0) {
//            isDesktop = false; // 默认值
//        }
//        if ((mask & 2) != 0) {
//            validator = null; // 默认值
//        }
//        instance.setUp(isDesktop, validator);
//    }
    private void createWebView() {
        compositeDisposable = new CompositeDisposable();
        webView = new WebView(AppContextUtil.getAppContext());
        extList = new ArrayList<>(Arrays.asList(".m3u8",".ts", ".mp3", ".m4a", ".m4b", ".flac", ".aa3", ".ogg", ".wma", ".wav", ".aac", ".ac3", ".mpg", ".mpeg", ".avi", ".vob", ".wmv", ".asf", ".rm", ".rmvb", ".mov", ".mkv", ".flv", ".mp4"));

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setAppCachePath(AppContextUtil.getAppContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setDatabaseEnabled(true);
        webView.layout(0, 0, 1080, 1920);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        webView.getSettings().setMixedContentMode(0);
        webView.setWebChromeClient(new AudioWebChromeClient());
        webView.setWebViewClient(new AudioWebViewClient());
    }

    private static class AudioWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (view.getUrl() != null && !view.getUrl().contains("about:blank")) {
                if (newProgress > 60 && !AudioUrlWebViewSniffExtractor.isScriptExecuted && !AudioUrlWebViewSniffExtractor.script.isEmpty()) {
                    AudioUrlWebViewSniffExtractor.isScriptExecuted = true;
                    AudioUrlWebViewSniffExtractor.INSTANCE.webView.evaluateJavascript(AudioUrlWebViewSniffExtractor.script, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            // Do nothing
                        }
                    });
                }
            }
        }
    }

    private static class AudioWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            AudioUrlWebViewSniffExtractor.INSTANCE.webView.clearHistory();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
            handler.proceed();
        }

        @Override
        @RequiresApi(21)
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (request != null && request.getUrl() != null && !AudioUrlWebViewSniffExtractor.isAudioGet && !AudioUrlWebViewSniffExtractor.isError) {
                Uri url = request.getUrl();
                String scheme = url.getScheme();
                if (scheme != null && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                    AudioUrlWebViewSniffExtractor.INSTANCE.extractAudio(url.toString());
                    if (AudioUrlWebViewSniffExtractor.isCompatibleMode) {
                        try {
                            OkHttpClient client = new OkHttpClient.Builder().build();
                            Request.Builder requestBuilder = new Request.Builder()
                                    .url(url.toString())
                                    .method(request.getMethod(), null);

                            Map<String, String> headers = request.getRequestHeaders();
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                if (!entry.getKey().equals("X-Requested-With") && 
                                    !entry.getValue().equals("com.github.eprendre.tingshu") && 
                                    !entry.getValue().equals("com.android.chrome")) {
                                    requestBuilder.addHeader(entry.getKey(), entry.getValue());
                                }
                            }

                            requestBuilder.addHeader("Referer", SpManage.getCurrentBookUrl());
                            requestBuilder.addHeader("User-Agent", AudioUrlWebViewSniffExtractor.userAgent);

                            String cookie = CookieManager.getInstance().getCookie(url.getScheme() + "://" + url.getHost());
                            if (cookie != null) {
                                requestBuilder.addHeader("Cookie", cookie);
                            }

                            Response response = client.newCall(requestBuilder.build()).execute();
                            if (response.body() != null && response.body().contentType() != null) {
                                String mimeType = response.body().contentType().type() + "/" + response.body().contentType().subtype();
                                String encoding = response.header("Content-Encoding", null);
                                return new WebResourceResponse(mimeType, encoding, response.body().byteStream());
                            }
                        } catch (Exception e) {
                            // Log exception
                        }
                    }
                }
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }
    }

    public void extractAudioInternal(String url) {
        if (isAudioGet || isError) {
            return;
        }
        if (isDebug) {
            logBuilder.append(url).append('\n');
        }
        boolean isValidAudio = false;
        if (validateAudioUrl != null) {
            isValidAudio = validateAudioUrl.isValid(url);
        } else {
            // Implement your audio validation logic here
            // This is a placeholder and should be replaced with actual logic
            isValidAudio = url.contains("audio") || url.contains("video") || url.contains("mpegurl");
        }
        if (isValidAudio) {
            handlePlayUrl(url);
        }
    }
    public void extractAudio(final String url) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            extractAudioInternal(url);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    extractAudioInternal(url);
                }
            });
        }
    }

    private void handlePlayUrl(String audioUrl) {
        compositeDisposable.clear();
        isAudioGet = true;
        try {
            if (audioUrl.equals(URLDecoder.decode(audioUrl, "UTF-8"))) {
                URL url = new URL(audioUrl);
                audioUrl = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef()).toASCIIString();
            }
        } catch (Exception e) {
            // Log exception
        }

        Log.i("xiaoshengSucc:", "AudioUrlWebViewSniff"+audioUrl+":"+episodeUrl+":"+isDebug+":"+isCache);
        if (isDebug) {
            RxBus.get().post(new RxEvent.LogEvent("嗅探地址列表: \n" + audioUrl));
            RxBus.get().post(new RxEvent.LogEvent("音频地址: \n" + audioUrl));
        } else if (!isCache) {
            SpManage.setCurrentAudioUrl(audioUrl);
            if (isAutoPlay) {
                RxBus.get().post(new RxEvent.StatusEvent(RxEvent.StatusEvent.STATUS_PLAY));
            } else {
                RxBus.get().post(new RxEvent.StatusEvent(1)); // Assuming 1 is the code for preparing to play
            }
        } else {
//            Log.i("xiaoshengSucc:", "AudioUrlWebViewSniff"+audioUrl+":"+episodeUrl);
            RxBus.get().post(new RxEvent.CacheEvent(episodeUrl, audioUrl, 0));
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                CookieManager.getInstance().flush();
                webView.stopLoading();
                webView.loadUrl("about:blank");
            }
        });
    }

    private void postError() {
        if (isAudioGet) {
            return;
        }
        isError = true;
        RxBus.get().post(new RxEvent.StatusEvent(RxEvent.StatusEvent.STATUS_FAILED));
        webView.loadUrl("about:blank");
    }

    @Override
    public void extract(String url, boolean autoPlay, final boolean cache, final boolean debug) {
        compositeDisposable.clear();
        isAudioGet = false;
        isPageFinished = false;
        isError = false;
        isAutoPlay = autoPlay;
        episodeUrl = url;
        isDebug = debug;
        isCache = cache;
        logBuilder.setLength(0);
        isCompatibleMode = Utils.findSourceByBook(Utils.getCurrentBook()).isWebViewCompatible();
        isScriptExecuted = script.isEmpty();

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "com.android.chrome");
        headers.put("Referer", SpManage.getCurrentBookUrl());
        webView.loadUrl(url, headers);

        Disposable disposable = Completable.timer(45, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() {
                        if (!isAudioGet) {
                            if (debug) {
                                RxBus.get().post(new RxEvent.LogEvent("获取音频地址超时"));
                                if (logBuilder.length() > 0) {
                                    RxBus.get().post(new RxEvent.LogEvent("嗅探地址列表: \n" + logBuilder.toString()));
                                }
                            } else if (cache) {
                                RxBus.get().post(new RxEvent.CacheEvent(episodeUrl, "", RxEvent.CacheEvent.STATUS_FAILED));
                            } else {
                                postError();
                            }
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void setUp(final boolean isDesktop, final AudioUrlValidator validator) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setUpInternal(isDesktop, validator);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    setUpInternal(isDesktop, validator);
                }
            });
        }
    }

    private void setUpInternal(boolean isDesktop, AudioUrlValidator validator) {
        userAgent = isDesktop ? MyExtKt.getDesktopUA() : MyExtKt.getMobileUA();
        validateAudioUrl = validator;
        webView.getSettings().setUserAgentString(userAgent);
        webView.loadUrl("about:blank");
    }

    public interface AudioUrlValidator {
        boolean isValid(String url);
    }
}