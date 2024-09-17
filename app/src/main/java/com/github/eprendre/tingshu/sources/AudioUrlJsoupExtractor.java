package com.github.eprendre.tingshu.sources;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Keep;

import com.github.eprendre.tingshu.extensions.MyExtKt;
import com.github.eprendre.tingshu.widget.RxEvent;
import com.hwangjr.rxbus.RxBus;
import com.xiaosheng.learnapp.utils.SpManage;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.jvm.internal.Intrinsics;

@Keep
public class AudioUrlJsoupExtractor implements AudioUrlExtractor {
    private static boolean isDesktop;
    private static java.util.function.Function<Document, String> parse;
    public static final AudioUrlJsoupExtractor INSTANCE = new AudioUrlJsoupExtractor();
    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static String log = "";

    private AudioUrlJsoupExtractor() {
    }

    private static String extractLambda(String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", SpManage.getCurrentBookUrl());

        Connection connection = Jsoup.connect(url)
//                .sslSocketFactory(MyExtKt.socketFactory())
                .headers(headers);

        Document document = null;
        try {
            document = MyExtKt.config(connection, isDesktop).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return parse.apply(document);
        }
        return null;
    }



    @Override
    public void extract(@NotNull String url, boolean autoPlay, boolean isCache, boolean isDebug) {
        compositeDisposable.clear();
        log = "";

        Single.fromCallable(() -> extractLambda(url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        audioUrl -> {
                            if (audioUrl == null || audioUrl.isBlank()) {
                                if (isDebug) {
                                    RxBus.get().post(new RxEvent.LogEvent("Failed to get audio URL, returned empty"));
                                } else if (isCache) {
                                    RxBus.get().post(new RxEvent.CacheEvent(url, "", 2));
                                } else {
                                    RxBus.get().post(new RxEvent.StatusEvent(2));
                                }
                            } else {
                                if (URLDecoder.decode(audioUrl, "UTF-8").equals(audioUrl)) {
                                    URL decodedUrl = new URL(audioUrl);
                                    audioUrl = new URI(decodedUrl.getProtocol(), decodedUrl.getUserInfo(),
                                            decodedUrl.getHost(), decodedUrl.getPort(), decodedUrl.getPath(),
                                            decodedUrl.getQuery(), decodedUrl.getRef()).toASCIIString();
                                }
                                Log.i("xiaoshengSucc", "AudioUrlJsoup"+audioUrl+":"+url+":"+isCache+"isDebug:"+isDebug+" isAutoPlay:"+autoPlay);
                                if (isDebug) {
                                    RxBus.get().post(new RxEvent.LogEvent("Audio URL:\n" + audioUrl));
                                } else if (isCache) {
                                    RxBus.get().post(new RxEvent.CacheEvent(url, audioUrl, 0));
                                } else {
                                    SpManage.setCurrentAudioUrl(audioUrl);
                                    if (autoPlay) {
                                        RxBus.get().post(new RxEvent.StatusEvent(3));
                                    } else {
                                        RxBus.get().post(new RxEvent.StatusEvent(1));
                                    }
                                }
                            }
                        },
                        throwable -> {
                            if (isDebug) {
                                RxBus.get().post(new RxEvent.LogEvent(MyExtKt.stackTraceToString(throwable)));
                            } else if (isCache) {
                                RxBus.get().post(new RxEvent.CacheEvent(url, "", 2));
                            } else {
                                RxBus.get().post(new RxEvent.StatusEvent(2));
                            }
                        }
                );
    }

    public void setUp(boolean desktop, @NotNull java.util.function.Function<Document, String> parseFunction) {
        parse = parseFunction;
        isDesktop = desktop;
    }

    public void setUp(@NotNull java.util.function.Function<Document, String> parseFunction) {
        setUp(false, parseFunction);
    }
}