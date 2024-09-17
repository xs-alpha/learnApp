package com.github.eprendre.tingshu.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "bookmarks")
public final class BookMark {
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private Integer id;
    
    @NonNull
    private String bookUrl;
    @NonNull
    private String sourceId;
    @NonNull
    private String bookTitle;
    @NonNull
    private String episodeTitle;
    @NonNull
    private String episodeUrl;
    @NonNull
    private String bookMarkTitle;
    @NonNull
    private String bookMarkDesc;
    private long position;

    public BookMark(@NonNull String bookUrl, @NonNull String sourceId, @NonNull String bookTitle, 
                    @NonNull String episodeTitle, @NonNull String episodeUrl, 
                    @NonNull String bookMarkTitle, @NonNull String bookMarkDesc, long position) {
        this.bookUrl = bookUrl;
        this.sourceId = sourceId;
        this.bookTitle = bookTitle;
        this.episodeTitle = episodeTitle;
        this.episodeUrl = episodeUrl;
        this.bookMarkTitle = bookMarkTitle;
        this.bookMarkDesc = bookMarkDesc;
        this.position = position;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    @NonNull
    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(@NonNull String bookUrl) {
        this.bookUrl = bookUrl;
    }

    @NonNull
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(@NonNull String sourceId) {
        this.sourceId = sourceId;
    }

    @NonNull
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(@NonNull String bookTitle) {
        this.bookTitle = bookTitle;
    }

    @NonNull
    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(@NonNull String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    @NonNull
    public String getEpisodeUrl() {
        return episodeUrl;
    }

    public void setEpisodeUrl(@NonNull String episodeUrl) {
        this.episodeUrl = episodeUrl;
    }

    @NonNull
    public String getBookMarkTitle() {
        return bookMarkTitle;
    }

    public void setBookMarkTitle(@NonNull String bookMarkTitle) {
        this.bookMarkTitle = bookMarkTitle;
    }

    @NonNull
    public String getBookMarkDesc() {
        return bookMarkDesc;
    }

    public void setBookMarkDesc(@NonNull String bookMarkDesc) {
        this.bookMarkDesc = bookMarkDesc;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BookMark)) return false;
        BookMark other = (BookMark) obj;
        return bookUrl.equals(other.bookUrl) &&
                sourceId.equals(other.sourceId) &&
                bookTitle.equals(other.bookTitle) &&
                episodeTitle.equals(other.episodeTitle) &&
                episodeUrl.equals(other.episodeUrl) &&
                bookMarkTitle.equals(other.bookMarkTitle) &&
                bookMarkDesc.equals(other.bookMarkDesc) &&
                position == other.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookUrl, sourceId, bookTitle, episodeTitle, episodeUrl, bookMarkTitle, bookMarkDesc, position);
    }

    @Override
    @NonNull
    public String toString() {
        return "BookMark{" +
                "id=" + id +
                ", bookUrl='" + bookUrl + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", episodeTitle='" + episodeTitle + '\'' +
                ", episodeUrl='" + episodeUrl + '\'' +
                ", bookMarkTitle='" + bookMarkTitle + '\'' +
                ", bookMarkDesc='" + bookMarkDesc + '\'' +
                ", position=" + position +
                '}';
    }

    public static final class DiffCallback extends DiffUtil.ItemCallback<BookMark> {
        @Override
        public boolean areItemsTheSame(@NonNull BookMark oldItem, @NonNull BookMark newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull BookMark oldItem, @NonNull BookMark newItem) {
            return oldItem.equals(newItem);
        }
    }
}
