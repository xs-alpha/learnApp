package com.github.eprendre.tingshu.sources;

import android.util.Log;

import androidx.annotation.Keep;

import com.blankj.utilcode.util.StringUtils;
import com.github.eprendre.tingshu.extensions.MyExtKt;
import com.github.eprendre.tingshu.utils.ApiService;
import com.github.eprendre.tingshu.utils.SignUtils;
import com.github.eprendre.tingshu.widget.RxEvent;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.RequestFactory;
import com.github.kittinunf.fuel.json.FuelJson;
import com.github.kittinunf.fuel.json.FuelJsonKt;
import com.github.kittinunf.result.Result;
import com.hwangjr.rxbus.RxBus;
import com.xiaosheng.learnapp.utils.SpManage;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.ExceptionsKt;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.text.StringsKt;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Keep
public final class AudioUrlJsonExtractor implements AudioUrlExtractor {

    private static final AudioUrlJsonExtractor INSTANCE = new AudioUrlJsonExtractor();
    private static final Lazy<CompositeDisposable> compositeDisposableDelegate = LazyKt.lazy(() -> new CompositeDisposable());
    private static boolean isDesktop;
    private static Function1<? super FuelJson, String> parse;

    private AudioUrlJsonExtractor() {
    }

    public static AudioUrlJsonExtractor getInstance() {
        return INSTANCE;
    }

    private CompositeDisposable getCompositeDisposable() {
        return compositeDisposableDelegate.getValue();
    }

//    private static String extractUrl(String url) {
//        Map<String, Object> headers = MapsKt.mutableMapOf(
//                TuplesKt.to("User-Agent", isDesktop ? MyExtKt.getDesktopUA() : MyExtKt.getMobileUA())
//        );
//
//        if (! StringUtils.isEmpty(url) && url.contains("ximalaya.com"))) {
//            headers.put("xm-sign", SignUtils.sign());
//        }
//
//        Triple<Request, Response, Result<FuelJson, FuelError>> responseJson = FuelJsonKt.responseJson(
//                RequestFactory.Convenience.DefaultImpls.get$default(Fuel.INSTANCE, url, null, 2, null)
//                        .header(headers)
//        );
//
//        Function1<? super FuelJson, String> function = parse;
//        if (function == null) {
//            throw new IllegalStateException("parse function is not initialized");
//        }
//
//        return function.invoke(responseJson.getThird().get());
//    }
private static String extractUrl(String url) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://base.url") // Replace with a base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService apiService = retrofit.create(ApiService.class);

    // Prepare headers
    Map<String, String> headers = new HashMap<>();
    headers.put("User-Agent", isDesktop ? MyExtKt.getDesktopUA() : MyExtKt.getMobileUA());

    if (url != null && url.contains("ximalaya.com")) {
        headers.put("xm-sign", SignUtils.sign());
    }

    // Create call
    Call<FuelJson> call = apiService.getResponse(headers, url);

    try {
//        Response response = call.execute();
        Response<FuelJson> execute = call.execute();
        if (!execute.isSuccessful()) {
            throw new IOException("Unexpected code " + execute);
        }

        // Parse JSON response
        FuelJson fuelJson = execute.body();
        return parse.invoke(fuelJson);
    } catch (IOException e) {
        throw new RuntimeException("Failed to execute request", e);
    }
}


    @Override
    public void extract(String url, boolean autoPlay, boolean isCache, boolean isDebug) {
        getCompositeDisposable().clear();
        Disposable disposable = Single.fromCallable(() -> extractUrl(url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        audioUrl -> handleSuccess(audioUrl, autoPlay, isCache, isDebug),
                        throwable -> handleError(throwable, autoPlay, isCache, url)
                );
        getCompositeDisposable().add(disposable);
    }

    private void handleSuccess(String audioUrl, boolean autoPlay, boolean isCache, boolean isDebug) {
        if (audioUrl == null || audioUrl.trim().isEmpty() || "null".equals(audioUrl)) {
            if (autoPlay) {
                RxBus.get().post(new AudioUrlErrorEvent("获取音频地址失败, 地址为空"));
            } else {
//                RxBus.get().post(new AudioUrlNotFoundEvent(autoPlay ? "" : "", 2));
                RxBus.get().post(new RxEvent.StatusEvent( 2));
            }
        } else {
            try {
                URL url = new URL(audioUrl);
                audioUrl = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
                        url.getPath(), url.getQuery(), url.getRef()).toASCIIString();
            } catch (Exception e) {
                // Handle URL encoding issues if necessary
            }
            Log.i("xiaoshengSucc","AudioUrlJson:"+audioUrl+" isautoPlay:"+autoPlay+" iscache:"+isCache);
            if (autoPlay) {
                RxBus.get().post(new AudioUrlSuccessEvent("音频地址: " + audioUrl));
            } else if (isCache) {
                RxBus.get().post(new RxEvent.CacheEvent("", audioUrl, 0));
            } else {
                SpManage.setCurrentAudioUrl(audioUrl);
                RxBus.get().post(new AudioUrlReadyEvent(isDebug ? 3 : 1));
            }
        }
    }

    private void handleError(Throwable throwable, boolean autoPlay, boolean isCache, String url) {
        if (autoPlay) {
            RxBus.get().post(new AudioUrlErrorEvent(ExceptionsKt.stackTraceToString(throwable)));
        } else {
            if (isCache) {
                RxBus.get().post(new RxEvent.CacheEvent(url, "", 2));
            } else {
                RxBus.get().post(new RxEvent.StatusEvent( 2));
            }
        }
    }

    public void setUp(Function1<? super FuelJson, String> parseFunction) {
        setUp(false, parseFunction);
    }

    public void setUp(boolean isDesktop, Function1<? super FuelJson, String> parseFunction) {
        parse = parseFunction;
        AudioUrlJsonExtractor.isDesktop = isDesktop;
    }

    // Custom event classes
    public static class AudioUrlErrorEvent {
        private final String error;

        public AudioUrlErrorEvent(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    public static class AudioUrlSuccessEvent {
        private final String message;

        public AudioUrlSuccessEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }



    public static class AudioUrlReadyEvent {
        private final int code;

        public AudioUrlReadyEvent(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
