package com.github.eprendre.tingshu.sources;

import androidx.annotation.Keep;

import com.github.eprendre.tingshu.widget.RxEvent;
import com.hwangjr.rxbus.RxBus;
import com.xiaosheng.learnapp.utils.SpManage;
import com.xiaosheng.learnapp.utils.SpUtil;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Keep
public final class AudioUrlDirectExtractor implements AudioUrlExtractor {
    @NotNull
    public static final AudioUrlDirectExtractor INSTANCE = new AudioUrlDirectExtractor();

    private AudioUrlDirectExtractor() {
    }

    @Override
    public void extract(@NotNull String url, boolean autoPlay, boolean isCache, boolean isDebug) {
        Intrinsics.checkNotNullParameter(url, "url");

        if (isDebug) {
            // Debug: Post the URL itself
            RxBus.get().post(new DebugEvent("Audio URL: " + url));
        } else if (isCache) {
            // Cache: Post the URL twice
            RxBus.get().post(new RxEvent.CacheEvent(url, url));
        } else {
            // Normal: Set the current URL and post a status event
//            e.INSTANCE.setCurrentAudioUrl(url);
            SpManage.setCurrentAudioUrl(url);
            if (autoPlay) {
                RxBus.get().post(new RxEvent.StatusEvent(3));
            } else {
                RxBus.get().post(new RxEvent.StatusEvent(1));
            }
        }
    }

    // Define events as simple classes
    public static class DebugEvent {
        public final String message;

        public DebugEvent(String message) {
            this.message = message;
        }
    }

//    public static class CacheEvent {
//        public final String originalUrl;
//        public final String cacheUrl;
//
//        public CacheEvent(String originalUrl, String cacheUrl) {
//            this.originalUrl = originalUrl;
//            this.cacheUrl = cacheUrl;
//        }
//    }
//
//    public static class StatusEvent {
//        public final int statusCode;
//
//        public StatusEvent(int statusCode) {
//            this.statusCode = statusCode;
//        }
//    }
}
