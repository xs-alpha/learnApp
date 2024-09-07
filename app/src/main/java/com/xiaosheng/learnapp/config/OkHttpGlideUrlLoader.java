package com.xiaosheng.learnapp.config;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class OkHttpGlideUrlLoader extends BaseGlideUrlLoader<GlideUrl> {

    private final Call.Factory client;

    public OkHttpGlideUrlLoader(Call.Factory client) {
        super(null);
        this.client = client;
    }

    @Override
    protected String getUrl(GlideUrl glideUrl, int width, int height, Options options) {
        return glideUrl.toStringUrl();
    }

    protected InputStream getStream(GlideUrl glideUrl, int width, int height, Options options) {
        try {
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(glideUrl.toStringUrl())
                    .build();
            return client.newCall(request).execute().body().byteStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean handles(@NonNull GlideUrl glideUrl) {
        return false;
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final Call.Factory client;

        public Factory(Call.Factory client) {
            this.client = client;
        }

        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new OkHttpGlideUrlLoader(client);
        }

        @Override
        public void teardown() {
            // No-op
        }
    }
}
