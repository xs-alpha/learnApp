package com.github.eprendre.tingshu.sources;

import android.util.Log;

import androidx.annotation.Keep;

import com.github.eprendre.tingshu.widget.RxEvent;
import com.hwangjr.rxbus.RxBus;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Keep
public final class AudioUrlCustomExtractor implements AudioUrlExtractor {
    public static final AudioUrlCustomExtractor INSTANCE = new AudioUrlCustomExtractor();
    private static Function1<String, String> parse;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AudioUrlCustomExtractor() {}

    private CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void extract( String url, boolean autoPlay, boolean isCache, boolean isDebug) {
        Intrinsics.checkNotNullParameter(url, "url");
        getCompositeDisposable().clear();
        Disposable disposable = Single.fromCallable(() -> extractUrl(url))
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                        audioUrl -> handleSuccess(audioUrl, autoPlay, isCache, isDebug),
                        throwable -> handleError(throwable, autoPlay, isCache, url, isDebug)
                );
        getCompositeDisposable().add(disposable);
    }

    private String extractUrl(String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        Function1<String, String> function1 = parse;
        if (function1 == null) {
            throw new IllegalStateException("parse function is not initialized");
        }
        return function1.invoke(url);
    }

    private void handleSuccess(String audioUrl, boolean autoPlay, boolean isCache, boolean isDebug) {
        if (StringsKt.isBlank(audioUrl)) {
            if (autoPlay) {
                Log.i("xiaoshengSucc", autoPlay+"");
                RxBus.get().post("获取音频地址失败");
            } else {
                Log.i("xiaoshengSucc", audioUrl);
                RxBus.get().post(new RxEvent.AudioUrlEvent(audioUrl, isCache, isDebug));
            }
        } else {
            if (autoPlay) {
                Log.i("xiaoshengSucc", audioUrl);
                RxBus.get().post("音频地址: " + audioUrl);
            } else {
                Log.i("xiaoshengSucc", audioUrl);
                RxBus.get().post(new RxEvent.AudioUrlEvent(audioUrl, isCache, isDebug));
            }
        }
    }

    private void handleError(Throwable throwable, boolean autoPlay, boolean isCache, String url, boolean isDebug) {
        if (autoPlay) {
            Log.i("xiaoshengFail", url+isCache);
            RxBus.get().post("Error: " + throwable.getMessage());
        } else {
            Log.i("xiaoshengFail", url+isCache);
            RxBus.get().post(new RxEvent.AudioUrlEvent("", isCache, isDebug));
        }
    }

    public void setUp( Function1<String, String> parseFunction) {
        Intrinsics.checkNotNullParameter(parseFunction, "parse");
        parse = parseFunction;
    }

    // Define a simple event class to replace RxEvent

}
