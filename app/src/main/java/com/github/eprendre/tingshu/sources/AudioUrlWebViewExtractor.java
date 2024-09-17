package com.github.eprendre.tingshu.sources;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.eprendre.tingshu.extensions.MyExtKt;
import com.github.eprendre.tingshu.innerSource.LibriVoxSource;
import com.github.eprendre.tingshu.innerSource.YunTing;
import com.github.eprendre.tingshu.utils.Book;
import com.github.eprendre.tingshu.utils.SingletonSourceManage;
import com.github.eprendre.tingshu.widget.RxEvent;
import com.github.kittinunf.fuel.core.Headers;
import com.hwangjr.rxbus.RxBus;
import com.xiaosheng.learnapp.Utils;
import com.xiaosheng.learnapp.utils.AppContextUtil;
import com.xiaosheng.learnapp.utils.SpManage;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Keep
public final class AudioUrlWebViewExtractor implements AudioUrlExtractor {
    private static boolean isDesktop;
    private static Function<String, String> parse;
    public static final AudioUrlWebViewExtractor INSTANCE = new AudioUrlWebViewExtractor();
    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static String episodeUrl = "";
    private static boolean isAudioGet = false;
    private static boolean isAutoPlay = true;
    private static boolean isCache = false;
    private static boolean isCompatibleMode = false;
    private static boolean isDebug = false;
    private static boolean isError = false;
    private static boolean isPageFinished = false;
    private static String log = "";
    private static String script = "";
    private static String userAgent = "";
    private static WebView webView;

    public static void initWebViewIfNeeded() {
        if (webView == null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                createWebView();
            } else {
                new Handler(Looper.getMainLooper()).post(AudioUrlWebViewExtractor::createWebView);
            }
        }
    }
    private static void createWebView()  {
        webView = new WebView(AppContextUtil.getAppContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.layout(0, 0, 1080, 1920);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        webView.getSettings().setMixedContentMode(0);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (view.getUrl() != null && !view.getUrl().contains("about:blank")) {
                    if (newProgress > 60 && !isPageFinished) {
                        isPageFinished = true;
                        tryGetAudioSrc();
                    }
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager.getInstance().flush();
                webView.clearHistory();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorCode == -8) {
                    compositeDisposable.clear();
                    if (isDebug) {
                        RxBus.get().post(new RxEvent.LogEvent("Failed to get audio URL"));
                        if (!log.isEmpty()) {
                            RxBus.get().post(new RxEvent.LogEvent("Last script execution result:\n" + log));
                        }
                    } else if (isCache) {
                        RxBus.get().post(new RxEvent.CacheEvent(episodeUrl, "", RxEvent.CacheEvent.STATUS_FAILED));
                    } else {
                        postError();
                    }
                }
            }

            @Override
            @SuppressLint("NewApi")
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }

            @Override
            @RequiresApi(21)
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (isCompatibleMode && request != null && request.getUrl() != null) {
                    Uri url = request.getUrl();
                    try {
                        String scheme = url.getScheme();
                        if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
                            OkHttpClient client = new OkHttpClient.Builder().build();
                            Request.Builder builder = new Request.Builder()
                                    .url(url.toString())
                                    .method(request.getMethod(), null);

                            for (Map.Entry<String, String> header : request.getRequestHeaders().entrySet()) {
                                if (!"X-Requested-With".equalsIgnoreCase(header.getKey()) &&
                                        !"com.github.eprendre.tingshu".equalsIgnoreCase(header.getValue()) &&
                                        !"com.android.chrome".equalsIgnoreCase(header.getValue())) {
                                    builder.header(header.getKey(), header.getValue());
                                }
                            }

                            builder.header("Referer", SpManage.getCurrentBookUrl());
                            builder.header("User-Agent", userAgent);
                            String cookie = CookieManager.getInstance().getCookie(url.getScheme() + "://" + url.getHost());
                            if (cookie != null) {
                                builder.header(Headers.COOKIE, cookie);
                            }

                            Response response = client.newCall(builder.build()).execute();

                            if (response.body() != null) {
                                ResponseBody body = response.body();
                                if (body.contentType() != null) {
                                    MediaType contentType = body.contentType();
                                    String mimeType = contentType.type() + "/" + contentType.subtype();
                                    String encoding = response.header("encoding");
                                    return new WebResourceResponse(mimeType, encoding, body.byteStream());
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Log the exception
                    }
                }
                return super.shouldInterceptRequest(view, request);
            }
        });
    }

    private AudioUrlWebViewExtractor() {
    }

    private static void postError() {
        if (!isAudioGet) {
            isError = true;
            RxBus.get().post(new RxEvent.StatusEvent(RxEvent.StatusEvent.STATUS_FAILED));
            webView.loadUrl("about:blank");
        }
    }

    public void setUp(boolean isDesktop, String script, Function<String, String> parse) {
        AudioUrlWebViewExtractor.isDesktop = isDesktop;
        AudioUrlWebViewExtractor.script = script;
        AudioUrlWebViewExtractor.parse = parse;

        userAgent = isDesktop ? MyExtKt.getDesktopUA() : MyExtKt.getMobileUA();
        new Handler(Looper.getMainLooper()).post(() -> {
            initWebViewIfNeeded();
            webView.getSettings().setUserAgentString(userAgent);
            webView.loadUrl("about:blank");
        });
    }

    public void setUp(boolean isDesktop, Function<String, String> parse) {
        setUp(isDesktop, "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();", parse);
    }

    public void setUp(Function<String, String> parse) {
        setUp(false, parse);
    }

    private static void tryGetAudioSrc() {
        if (!isAudioGet && !isError) {
            webView.evaluateJavascript(script, html -> {
                if (html == null || html.isEmpty() || "null".equals(html)) {
                    new Handler().postDelayed(AudioUrlWebViewExtractor::tryGetAudioSrc, 500);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Single.fromCallable(() -> parse.apply(unescapeJava(html)))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        audioUrl -> {
                                            if (isDebug) {
                                                log = audioUrl;
                                            }

                                            if (audioUrl == null || audioUrl.isEmpty()) {
                                                new Handler().postDelayed(AudioUrlWebViewExtractor::postError, 500);
                                            } else if (!isAudioGet) {
                                                compositeDisposable.clear();
                                                isAudioGet = true;

                                                if (URLDecoder.decode(audioUrl, "UTF-8").equals(audioUrl)) {
                                                    URL url = new URL(audioUrl);
                                                    audioUrl = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
                                                            url.getPath(), url.getQuery(), url.getRef()).toASCIIString();
                                                }

                                                if (isDebug) {
                                                    Log.i("xiaoshengSucc", "AudioUrlWeb"+audioUrl);
                                                    RxBus.get().post(new RxEvent.LogEvent("Audio URL: " + audioUrl));
                                                } else if (isCache) {
                                                    Log.i("xiaoshengSucc", "AudioUrlWeb"+audioUrl+":"+episodeUrl);
                                                    RxBus.get().post(new RxEvent.CacheEvent(episodeUrl, audioUrl, RxEvent.CacheEvent.STATUS_SUCCESS));
                                                } else {
                                                    SpManage.setCurrentAudioUrl(audioUrl);
                                                    RxBus.get().post(new RxEvent.StatusEvent(isAutoPlay ? RxEvent.StatusEvent.STATUS_PLAY : RxEvent.StatusEvent.STATUS_SUCCESS));
                                                }

                                                webView.stopLoading();
                                                webView.loadUrl("about:blank");
                                            }
                                        },
                                        throwable -> {
                                            new Handler().postDelayed(AudioUrlWebViewExtractor::tryGetAudioSrc, 500);
                                        }
                                );
                    }
                }
            });
        }
    }

    @Override
    public void extract(String url, boolean autoPlay, boolean isCache, boolean isDebug) {
        initWebViewIfNeeded();
        compositeDisposable.clear();
        isAudioGet = false;
        isPageFinished = false;
        isError = false;
        isAutoPlay = autoPlay;
        episodeUrl = url;
        AudioUrlWebViewExtractor.isDebug = isDebug;
        AudioUrlWebViewExtractor.isCache = isCache;
//        isCompatibleMode = AppContextUtil.getCurrentBookSource().isWebViewCompatible();
        isCompatibleMode = Utils.findSourceByBook(Utils.getCurrentBook()).isWebViewCompatible();
        log = "";

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "com.android.chrome");
        headers.put("Referer", SpManage.getCurrentBookUrl());

        if (url.contains("&&&")) {
            url = url.split("&&&")[0];
        }

        webView.loadUrl(url, headers);

        Disposable disposable = Completable.timer(30, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (isDebug) {
                        isError = true;
                        webView.loadUrl("about:blank");
                        RxBus.get().post(new RxEvent.LogEvent("Timeout getting audio URL"));
                    } else if (isCache) {
                        Log.i("xiaoshengSucc",episodeUrl+":"+episodeUrl);
                        RxBus.get().post(new RxEvent.CacheEvent(episodeUrl, "", RxEvent.CacheEvent.STATUS_FAILED));
                    } else {
                        postError();
                    }
                });

        compositeDisposable.add(disposable);
    }

    public static void setUp$default(AudioUrlWebViewExtractor instance, boolean isDesktop, String script, Function1<String, String> parse, int mask, Object ignored) {
        if ((mask & 1) != 0) {
            isDesktop = false; // 默认值
        }
        if ((mask & 2) != 0) {
            script = "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();"; // 默认值
        }
        if ((mask & 4) != 0) {
            parse = null; // 默认值
        }
        instance.setUp(isDesktop, script, parse != null ? parse::invoke : null);
    }


    private static String unescapeJava(String escaped) {
        if (escaped == null) {
            return null;
        }

        int length = escaped.length();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length;) {
            char ch = escaped.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == length - 1) ? '\\' : escaped.charAt(i + 1);
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = escaped.substring(i + 1, Math.min(i + 4, length));
                    int value = Integer.parseInt(code, 8);
                    builder.append((char) value);
                    i += code.length() + 1;
                } else {
                    switch (nextChar) {
                        case 'b':
                            builder.append('\b');
                            break;
                        case 'f':
                            builder.append('\f');
                            break;
                        case 'n':
                            builder.append('\n');
                            break;
                        case 'r':
                            builder.append('\r');
                            break;
                        case 't':
                            builder.append('\t');
                            break;
                        case '\"':
                            builder.append('\"');
                            break;
                        case '\'':
                            builder.append('\'');
                            break;
                        case '\\':
                            builder.append('\\');
                            break;
                        default:
                            builder.append(nextChar);
                            break;
                    }
                    i += 2;
                }
            } else {
                builder.append(ch);
                i++;
            }
        }

        return builder.toString();
    }




}