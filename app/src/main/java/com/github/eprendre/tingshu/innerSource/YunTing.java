package com.github.eprendre.tingshu.innerSource;

import com.blankj.utilcode.util.ObjectUtils;
import com.github.eprendre.tingshu.sources.AudioUrlDirectExtractor;
import com.github.eprendre.tingshu.sources.AudioUrlExtractor;
import com.github.eprendre.tingshu.sources.TingShu;
import com.github.eprendre.tingshu.utils.Book;
import com.github.eprendre.tingshu.utils.BookDetail;
import com.github.eprendre.tingshu.utils.Category;
import com.github.eprendre.tingshu.utils.CategoryMenu;
import com.github.eprendre.tingshu.utils.CategoryTab;
import com.github.eprendre.tingshu.utils.Episode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import kotlin.Pair;

public final class YunTing extends TingShu {
    public static final YunTing INSTANCE = new YunTing();

    @Override
    @NotNull
    public AudioUrlExtractor getAudioUrlExtractor() {
        return AudioUrlDirectExtractor.INSTANCE;
    }

    @Override
    @NotNull
    public BookDetail getBookDetailInfo(@NotNull String bookUrl, boolean isRadio, boolean z10) {
        List<Episode> episodes = new ArrayList<>();
        if (isRadio) {
            episodes.add(new Episode("电台直播", bookUrl));
        }
        return new BookDetail(episodes, "云听电台", null, null, 0, null );
    }

    @Override
    @NotNull
    public Category getCategoryList(@NotNull String url) {
        String response;
        try {
            response = Jsoup.connect(url).ignoreContentType(true).execute().body();
            response = response.substring(1, response.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Category(new ArrayList<>(), 1, 1, url, "");
        }

        JSONArray topArray = null;
        try {
            topArray = new JSONObject(response).getJSONObject("data").getJSONArray("top");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        List<Book> books = new ArrayList<>();
        if (ObjectUtils.isEmpty(topArray)){
            return null;
        }

        for (int i = 0; i < topArray.length(); i++) {
            JSONObject top = null;
            try {
                top = topArray.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                String coverUrl = top.getJSONArray("tag").getJSONObject(0).getString("url");
                String bookUrl = top.getJSONArray("streams").getJSONObject(0).getString("url");
                String title = top.getString("name");
                String playNum = top.getString("playNum");
                String status = "人气: " + formatNumber(playNum);

                Book book = new Book(coverUrl, bookUrl, title, "", "");
                book.setIntro("");
                book.setStatus(status);
                book.setSourceId(INSTANCE.getSourceId());
                books.add(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Category(books, 1, 1, url, "");
    }

    private String formatNumber(String number) {
        try {
            long num = Long.parseLong(number);
            NumberFormat format = NumberFormat.getInstance();
            return format.format(num);
        } catch (NumberFormatException e) {
            return number;
        }
    }

    @Override
    @NotNull
    public List<CategoryMenu> getCategoryMenus() {
        List<CategoryTab> tabs = new ArrayList<>();
        tabs.add(new CategoryTab("国家", "http://tacc.radio.cn/pcpages/radiopages?callback=&place_id=3225"));
        tabs.add(new CategoryTab("北京", "http://tacc.radio.cn/pcpages/radiopages?callback=&place_id=3226"));
        tabs.add(new CategoryTab("天津", "http://tacc.radio.cn/pcpages/radiopages?callback=&place_id=3227"));
        // Add the rest of the CategoryTab objects...

        return List.of(new CategoryMenu("云听电台", tabs));
    }

    @Override
    @NotNull
    public String getDesc() {
        return "推荐指数:2星 ⭐⭐\n这个源只提供电台功能";
    }

    @Override
    @NotNull
    public String getName() {
        return "云听电台";
    }

    @Override
    @NotNull
    public String getSourceId() {
        return "b3ce754df5d74959a8769a8b4d7033f8";
    }

    @Override
    @NotNull
    public String getUrl() {
        return "http://www.radio.cn";
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    public boolean isWebViewNotRequired() {
        return true;
    }

    @Override
    public Pair<List<Book>, Integer> search(String keywords, int i10) {
        return new Pair<>(new ArrayList(), 1);
    }
}