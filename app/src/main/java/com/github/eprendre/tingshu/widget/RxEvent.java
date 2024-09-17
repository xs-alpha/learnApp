package com.github.eprendre.tingshu.widget;

import androidx.annotation.Keep;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Keep
public final class RxEvent {

    public static final class AlbumChangeEvent {
        private final int status;

        public AlbumChangeEvent(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        @NotNull
        public AlbumChangeEvent copy(int status) {
            return new AlbumChangeEvent(status);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) return true;
            if (obj instanceof AlbumChangeEvent) {
                return this.status == ((AlbumChangeEvent) obj).status;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(status);
        }

        @NotNull
        @Override
        public String toString() {
            return "AlbumChangeEvent(status=" + status + ")";
        }
    }

    public static final class BookMarkPositionEvent {
        @NotNull
        private final BookMark bookmark;

        public BookMarkPositionEvent(@NotNull BookMark bookmark) {
            this.bookmark = bookmark;
        }

        @NotNull
        public BookMark getBookmark() {
            return bookmark;
        }

        @NotNull
        public BookMarkPositionEvent copy(@NotNull BookMark bookmark) {
            return new BookMarkPositionEvent(bookmark);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) return true;
            if (obj instanceof BookMarkPositionEvent) {
                return this.bookmark.equals(((BookMarkPositionEvent) obj).bookmark);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return bookmark.hashCode();
        }

        @NotNull
        @Override
        public String toString() {
            return "BookMarkPositionEvent(bookmark=" + bookmark + ")";
        }
    }

    public static final class CacheEvent {
        public static final int STATUS_FAILED = 1;
        public static final int STATUS_SUCCESS = 2;
        private final String episodeUrl;
        private final String audioUrl;
        private  int status;
        private long progress;
        private String msg;

        public CacheEvent(String episodeUrl, String audioUrl){
            this.episodeUrl = episodeUrl;
            this.audioUrl = audioUrl;
        }

        public CacheEvent(String episodeUrl, String audioUrl, int status) {
            this.episodeUrl = episodeUrl;
            this.audioUrl = audioUrl;
            this.status = status;
            this.msg = "";
        }

        public String getEpisodeUrl() {
            return episodeUrl;
        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public int getStatus() {
            return status;
        }

        public long getProgress() {
            return progress;
        }

        @NotNull
        public String getMsg() {
            return msg;
        }

        public void setMsg(@NotNull String msg) {
            this.msg = msg;
        }

        public void setProgress(long progress) {
            this.progress = progress;
        }

        @NotNull
        public CacheEvent copy(@NotNull String episodeUrl, @NotNull String audioUrl, int status) {
            return new CacheEvent(episodeUrl, audioUrl, status);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) return true;
            if (obj instanceof CacheEvent) {
                CacheEvent other = (CacheEvent) obj;
                return this.episodeUrl.equals(other.episodeUrl) &&
                       this.audioUrl.equals(other.audioUrl) &&
                       this.status == other.status;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = episodeUrl.hashCode();
            result = 31 * result + audioUrl.hashCode();
            result = 31 * result + status;
            return result;
        }

        @NotNull
        @Override
        public String toString() {
            return "CacheEvent(episodeUrl=" + episodeUrl + ", audioUrl=" + audioUrl + ", status=" + status + ")";
        }
    }

    public static class AudioUrlEvent {
        private final String audioUrl;
        private final boolean isCache;
        private final boolean isDebug;

        public AudioUrlEvent(String audioUrl, boolean isCache, boolean isDebug) {
            this.audioUrl = audioUrl;
            this.isCache = isCache;
            this.isDebug = isDebug;
        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public boolean isCache() {
            return isCache;
        }

        public boolean isDebug() {
            return isDebug;
        }
    }

    // Other event classes (e.g., CastEvent, LoadingBookEvent, LogEvent) would be similarly refactored.



    public static class StatusEvent {
        public static final int STATUS_PLAY = 1;
        public static final int STATUS_SUCCESS = 2;
        public static int STATUS_FAILED = 0;
        public final int statusCode;

        public StatusEvent(int statusCode) {
            this.statusCode = statusCode;
        }


    }
    private RxEvent() {

    }

    public static class LogEvent {
        public final String event;

        public LogEvent(String event) {
            this.event = event;
        }


    }

}
