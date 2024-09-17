package com.github.eprendre.tingshu.innerSource;


import android.os.Build;

import com.github.eprendre.tingshu.sources.AudioUrlDirectExtractor;
import com.github.eprendre.tingshu.sources.AudioUrlExtractor;
import com.github.eprendre.tingshu.sources.TingShu;
import com.github.eprendre.tingshu.utils.Book;
import com.github.eprendre.tingshu.utils.BookDetail;
import com.github.eprendre.tingshu.utils.Category;
import com.github.eprendre.tingshu.utils.CategoryMenu;
import com.github.eprendre.tingshu.utils.CategoryTab;
import com.github.eprendre.tingshu.utils.Episode;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kotlin.Pair;

public final class LibriVoxSource extends TingShu {
    public static final LibriVoxSource INSTANCE = new LibriVoxSource();

    @Override
    @NotNull
    public AudioUrlExtractor getAudioUrlExtractor() {
        return AudioUrlDirectExtractor.INSTANCE;
    }

    @Override
    @NotNull
    public BookDetail getBookDetailInfo(@NotNull String bookUrl, boolean isRadio, boolean isCache) {
        try {
            Document document = getDocument(Jsoup.connect(bookUrl), true);
            Elements chapterElements = document.select(".chapter-download > tbody > tr > td > .chapter-name");
            List<Episode> episodes = new ArrayList<>();

            for (Element element : chapterElements) {
                String chapterTitle = element.text();
                String chapterUrl = element.absUrl("href");
                episodes.add(new Episode(chapterTitle, chapterUrl));
            }

            Element descriptionElement = document.selectFirst(".description");
            String intro = (descriptionElement != null) ? descriptionElement.text() : null;

            return new BookDetail(episodes, intro, null, null, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @NotNull
    public Category getCategoryList(@NotNull String url) {
        try {
            JSONObject responseJson = getResponseJson(url, 1);
            int currentPage = responseJson.getJSONObject("pagination").getInt("search_page");
            int totalPages = parseTotalPages(responseJson.getString("pagination"));
            String nextUrl = generateNextUrl(url, currentPage + 1);

            Document resultsDocument = Jsoup.parse(responseJson.getString("results"));
            List<Book> books = parseBooks(resultsDocument);

            return new Category(books, currentPage, totalPages, url, nextUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @NotNull
    public List<CategoryMenu> getCategoryMenus() {
        List<CategoryMenu> categoryMenus = new ArrayList<>();

        // Children category
        List<CategoryTab> childrenTabs = Arrays.asList(
                new CategoryTab("Children's Fiction", "https://librivox.org/search/get_results?primary_key=1&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Action & Adventure", "https://librivox.org/search/get_results?primary_key=37&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Animals & Nature", "https://librivox.org/search/get_results?primary_key=38&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Myths, Legends & Fairy Tales", "https://librivox.org/search/get_results?primary_key=39&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Family", "https://librivox.org/search/get_results?primary_key=40&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("General", "https://librivox.org/search/get_results?primary_key=41&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Historical", "https://librivox.org/search/get_results?primary_key=42&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Poetry", "https://librivox.org/search/get_results?primary_key=43&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Religion", "https://librivox.org/search/get_results?primary_key=44&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("School", "https://librivox.org/search/get_results?primary_key=45&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Short works", "https://librivox.org/search/get_results?primary_key=46&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Non-fiction", "https://librivox.org/search/get_results?primary_key=2&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Non-fiction > Arts", "https://librivox.org/search/get_results?primary_key=47&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Non-fiction > General", "https://librivox.org/search/get_results?primary_key=48&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Non-fiction > Reference", "https://librivox.org/search/get_results?primary_key=49&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Non-fiction > Religion", "https://librivox.org/search/get_results?primary_key=50&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Non-fiction > Science", "https://librivox.org/search/get_results?primary_key=51&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either")
        );
        categoryMenus.add(new CategoryMenu("Children", childrenTabs));

        // Fantastic category
        List<CategoryTab> fantasticTabs = Arrays.asList(
                new CategoryTab("Fantastic Fiction", "https://librivox.org/search/get_results?primary_key=13&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Myths, Legends & Fairy Tales", "https://librivox.org/search/get_results?primary_key=11&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Horror & Supernatural Fiction", "https://librivox.org/search/get_results?primary_key=16&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Gothic Fiction", "https://librivox.org/search/get_results?primary_key=17&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Science Fiction", "https://librivox.org/search/get_results?primary_key=30&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Fantasy Fiction", "https://librivox.org/search/get_results?primary_key=55&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either")
        );
        categoryMenus.add(new CategoryMenu("Fantastic", fantasticTabs));

        // Fiction category
        List<CategoryTab> fictionTabs = Arrays.asList(
                new CategoryTab("General Fiction", "https://librivox.org/search/get_results?primary_key=15&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("General Fiction > Published before 1800", "https://librivox.org/search/get_results?primary_key=52&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("General Fiction > Published 1800-1900", "https://librivox.org/search/get_results?primary_key=53&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("General Fiction > Published 1900 onward", "https://librivox.org/search/get_results?primary_key=54&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Historical Fiction", "https://librivox.org/search/get_results?primary_key=18&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Humorous Fiction", "https://librivox.org/search/get_results?primary_key=19&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Literary Fiction", "https://librivox.org/search/get_results?primary_key=20&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Nature & Animal Fiction", "https://librivox.org/search/get_results?primary_key=21&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Nautical & Marine Fiction", "https://librivox.org/search/get_results?primary_key=23&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either")
        );
        categoryMenus.add(new CategoryMenu("Fiction", fictionTabs));

        // Nonfiction category
        List<CategoryTab> nonfictionTabs = Arrays.asList(
                new CategoryTab("Non-fiction", "https://librivox.org/search/get_results?primary_key=36&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("War & Military", "https://librivox.org/search/get_results?primary_key=73&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Animals", "https://librivox.org/search/get_results?primary_key=77&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Art, Design & Architecture", "https://librivox.org/search/get_results?primary_key=78&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Bibles", "https://librivox.org/search/get_results?primary_key=79&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Biography & Autobiography", "https://librivox.org/search/get_results?primary_key=80&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Biography & Autobiography > Memoirs", "https://librivox.org/search/get_results?primary_key=111&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Business & Economics", "https://librivox.org/search/get_results?primary_key=81&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Education", "https://librivox.org/search/get_results?primary_key=83&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Essays & Short Works", "https://librivox.org/search/get_results?primary_key=84&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("History", "https://librivox.org/search/get_results?primary_key=87&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("History > Antiquity", "https://librivox.org/search/get_results?primary_key=113&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("History > Middle Ages/Middle History", "https://librivox.org/search/get_results?primary_key=114&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("History > Early Modern", "https://librivox.org/search/get_results?primary_key=115&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("History > Modern (19th C)", "https://librivox.org/search/get_results?primary_key=116&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Modern (20th C)", "https://librivox.org/search/get_results?primary_key=117&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Nature", "https://librivox.org/search/get_results?primary_key=96&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Philosophy", "https://librivox.org/search/get_results?primary_key=98&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Political Science", "https://librivox.org/search/get_results?primary_key=99&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Psychology", "https://librivox.org/search/get_results?primary_key=100&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Religion", "https://librivox.org/search/get_results?primary_key=102&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Science", "https://librivox.org/search/get_results?primary_key=103&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Self-Help", "https://librivox.org/search/get_results?primary_key=104&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Social Science (Culture & Anthropology)", "https://librivox.org/search/get_results?primary_key=105&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Travel & Geography", "https://librivox.org/search/get_results?primary_key=108&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either")
        );
        categoryMenus.add(new CategoryMenu("Nonfiction", nonfictionTabs));
        List<CategoryTab> othersTabs = Arrays.asList(
                new CategoryTab("Action & Adventure Fiction", "https://librivox.org/search/get_results?primary_key=3&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Crime & Mystery Fiction", "https://librivox.org/search/get_results?primary_key=5&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Crime & Mystery Fiction > Detective Fiction", "https://librivox.org/search/get_results?primary_key=22&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Plays", "https://librivox.org/search/get_results?primary_key=24&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Poetry", "https://librivox.org/search/get_results?primary_key=25&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Romance", "https://librivox.org/search/get_results?primary_key=27&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Satire", "https://librivox.org/search/get_results?primary_key=29&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Short Stories", "https://librivox.org/search/get_results?primary_key=31&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Short Stories > Anthologies", "https://librivox.org/search/get_results?primary_key=75&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Short Stories > Single Author Collections", "https://librivox.org/search/get_results?primary_key=76&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("War & Military Fiction", "https://librivox.org/search/get_results?primary_key=34&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either"),
                new CategoryTab("Westerns", "https://librivox.org/search/get_results?primary_key=35&search_category=genre&sub_category=&search_page=1&search_order=catalog_date&project_type=either")
        );
        categoryMenus.add(new CategoryMenu("Others", othersTabs));
        return Collections.unmodifiableList(categoryMenus);
    }

    @Override
    @NotNull
    public String getDesc() {
        return "推荐指数:3星 ⭐⭐⭐\n需要科学上网才能正常打开封面和播放。\n当之无愧的全世界最大的自制有声书社区，都是志愿者录制的公共版权的英语有声书。优点是基本上出名的书都有有声书版本，包括非常少有有声版本的文史哲书籍。学生党如果要学习本学科专业著作，大可以下载对应名著，边听边读，一条网线即座与哈佛牛津学生同窗。";
    }

    @Override
    @NotNull
    public String getName() {
        return "LibriVox";
    }

    @Override
    @NotNull
    public String getSourceId() {
        return "f4624b52b8624bf98910f914d285832e";
    }

    @Override
    @NotNull
    public String getUrl() {
        return "https://librivox.org";
    }

    @Override
    public boolean isWebViewNotRequired() {
        return true;
    }

    @Override
    @NotNull
    public Pair<List<Book>, Integer> search(@NotNull String keywords, int page) {
        try {
            String encodedKeywords = URLEncoder.encode(keywords, "utf-8");
            String url = "https://librivox.org/advanced_search?title=&author=&reader=&keywords=&genre_id=0&status=all&project_type=either&recorded_language=&sort_order=alpha&search_page=" + page + "&search_form=advanced&q=" + encodedKeywords;

            JSONObject responseJson = getResponseJson(url, page);
            int totalPages = parseTotalPages(responseJson.getString("pagination"));

            Document resultsDocument = Jsoup.parse(responseJson.getString("results"));
            List<Book> books = parseBooks(resultsDocument);

            return new Pair<>(books, Integer.valueOf(totalPages));
        } catch (Exception e) {
            e.printStackTrace();
            return  new Pair<>(new ArrayList<>(), Integer.valueOf(1));
        }
    }

    private JSONObject getResponseJson(String url, int page) throws IOException, JSONException {
        Connection.Response response = Jsoup.connect(url)
                .header("X-Requested-With", "XMLHttpRequest")
                .ignoreContentType(true)
                .execute();
        return new JSONObject(response.body());
    }

    private int parseTotalPages(String paginationHtml) {
        if (paginationHtml == null || paginationHtml.isEmpty()) {
            return 1;
        } else {
            String lastPageNumber = Jsoup.parse(paginationHtml).selectFirst(".last").attr("data-page_number");
            return Integer.parseInt(lastPageNumber);
        }
    }

    private String generateNextUrl(String currentUrl, int nextPage) throws IOException {
        URL url = new URL(currentUrl);
        Map<String, String> queryParams = splitQuery(url);
        queryParams.put("search_page", String.valueOf(nextPage));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return "https://librivox.org/search/get_results?" + queryParams.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
        }
        return "";
    }

    private Map<String, String> splitQuery(URL url) throws IOException {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }
        return queryPairs;
    }

    private List<Book> parseBooks(Document resultsDocument) {
        List<Book> books = new ArrayList<>();
        Elements bookElements = resultsDocument.select(".catalog-result");

        for (Element element : bookElements) {
            try {
                String coverUrl = element.selectFirst(".book-cover > img").absUrl("src");
                String bookUrl = element.selectFirst(".result-data > h3 > a").absUrl("href");
                String title = element.selectFirst(".result-data > h3 > a").text();
                String author = element.select(".book-author > a").text();
                String metaInfo = element.select(".book-meta").text();
                String[] metaParts = metaInfo.split("\\|");
                String status = metaParts[0].trim();
                String bookType = metaParts[1].trim();
                String intro = metaParts[2].trim();

                if (!bookUrl.startsWith("https://forum.librivox.org")) {
                    Book book = new Book(coverUrl, bookUrl, title, author, bookType);
                    book.setIntro(intro);
                    book.setStatus(status);
                    book.setSourceId(INSTANCE.getSourceId());
                    books.add(book);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return books;
    }

    private Document getDocument(Connection connection, boolean isDesktop) throws IOException {
        return connection.userAgent(isDesktop ? getDesktopUserAgent() : getMobileUserAgent())
                .timeout(30000)
                .get();
    }

    private String getDesktopUserAgent() {
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36";
    }

    private String getMobileUserAgent() {
        return "Mozilla/5.0 (Linux; Android 10; SM-G960U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.72 Mobile Safari/537.36";
    }
}