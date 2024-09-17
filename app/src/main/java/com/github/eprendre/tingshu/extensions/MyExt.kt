package com.github.eprendre.tingshu.extensions

//import io.reactivex.disposables.Disposable
import android.util.Log
import com.blankj.utilcode.util.StringUtils
import com.github.eprendre.tingshu.utils.Book
import com.hwangjr.rxbus.RxBus
import io.reactivex.rxjava3.disposables.Disposable
import org.jsoup.Connection
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URL
import java.net.URLDecoder
import kotlin.jvm.internal.Intrinsics


fun splitQuery(url: URL): LinkedHashMap<String, String> {
    val queryPairs = LinkedHashMap<String, String>()
    val query = url.query
    val pairs = query.split("&")
    for (pair in pairs) {
        val idx = pair.indexOf("=")
        queryPairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
            URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
    }
    return queryPairs
}

/**
 * 是否使用 PC 版的 UA，UA 字段会自动从 app 里的配置读取。
 */
//fun Connection.config(isDesktop: Boolean = false): Connection {
//    throw RuntimeException("Stub!")
//}


fun Connection.config(connection: Connection, z9: Boolean): Connection? {
    val userAgent: Connection
    val str: String
    Intrinsics.checkNotNullParameter(connection, "<this>")
    Log.i("xiaosheng","z9:"+z9);
    if (z9) {
        userAgent = connection.userAgent(getDesktopUA())
        str = "{\n        this.userAgent…s.desktopUserAgent)\n    }"
    } else {
        userAgent = connection.userAgent(getMobileUA())
        str = "{\n        this.userAgent…fs.mobileUserAgent)\n    }"
    }
    Intrinsics.checkNotNullExpressionValue(userAgent, str)
    return userAgent
}

fun config(connection: Connection, z9: Boolean = false): Connection {
    val userAgent: Connection
    val str: String
    if (z9) {
        userAgent = connection.userAgent(getDesktopUA())
        str = "{\n        this.userAgent…s.desktopUserAgent)\n    }"
    } else {
        userAgent = connection.userAgent(getMobileUA())
        str = "{\n        this.userAgent…fs.mobileUserAgent)\n    }"
    }
    return userAgent
}
fun stackTraceToString(th: Throwable): String {
    Intrinsics.checkNotNullParameter(th, "<this>")
    val stringWriter = StringWriter()
    val printWriter = PrintWriter(stringWriter)
    th.printStackTrace(printWriter)
    printWriter.flush()
    val stringWriter2 = stringWriter.toString()
    Intrinsics.checkNotNullExpressionValue(stringWriter2, "sw.toString()")
    return stringWriter2
}


/**
 * 获取 PC 版 UA
 */
fun getDesktopUA(): String {
//    throw RuntimeException("Stub!")
    return arrayOf("User-Agent,Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;",
        "User-Agent,Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)",
        "User-Agent,Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)",
        "User-Agent, Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)",
        "User-Agent, Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv,2.0.1) Gecko/20100101 Firefox/4.0.1",
        "User-Agent,Mozilla/5.0 (Windows NT 6.1; rv,2.0.1) Gecko/20100101 Firefox/4.0.1",
        "User-Agent,Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
        "User-Agent,Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11",
        "User-Agent, Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
        "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"
    ).random()
}

/**
 * 获取手机版 UA
 */
fun getMobileUA(): String {
//    throw RuntimeException("Stub!")
    return arrayListOf(
        "Mozilla/5.0 (Linux; Android 9; MHA-L29 Build/HUAWEIMHA-L29; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/75.0.3770.143 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; DLI-AL10 Build/HONORDLI-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PBBM00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; MI 5 Build/OPR1.170623.032; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; FRD-AL00 Build/HUAWEIFRD-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; vivo X21 Build/OPM1.171019.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; Redmi Note 5 Build/PKQ1.180904.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; V1818CA Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 12_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 /sa-sdk-ios/sensors-verify/sc.baertt.com?production ",
        "Mozilla/5.0 (Linux; Android 8.1.0; Redmi 5 Plus Build/OPM1.171019.019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PACT00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; TRT-AL00 Build/HUAWEITRT-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; BLN-AL40 Build/HONORBLN-AL40; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; SM-C7100 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.2; vivo Y66i Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; BKK-AL10 Build/HONORBKK-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; HUAWEI NXT-AL10 Build/HUAWEINXT-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PADM00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; SLA-AL00 Build/HUAWEISLA-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0; NCE-AL10 Build/HUAWEINCE-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; Redmi Note 7 Build/PKQ1.180904.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0; GN8002S Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.106 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; Y79 Build/RB3N5C; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; PCAM10 Build/PPR1.180610.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PBBT00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1; OPPO A37m Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OPPO A83 Build/N6F26Q; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; OPPO R9 Plusm A Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; TRT-AL00 Build/HUAWEITRT-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044429 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0; HUAWEI GRA-UL10 Build/HUAWEIGRA-UL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; OPPO R9s Plus Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; SLA-TL10 Build/HUAWEISLA-TL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PCDM00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; GO T6 Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; U; Android 4.2.2; zh-cn; unknown Build/JDQ39E) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
        "Mozilla/5.0 (Linux; Android 6.0; HUAWEI MLA-AL10 Build/HUAWEIMLA-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PBAM00 Build/OPM1.171019.026; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; BND-AL10 Build/HONORBND-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; V1838A Build/PKQ1.190302.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; Redmi 6A Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PBBM00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; Redmi Note 3 Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; TRT-TL10 Build/HUAWEITRT-TL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1; OPPO A59m Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; HUAWEI CAZ-AL10 Build/HUAWEICAZ-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/56.0.2924.87 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; MI 5 Build/MXB48T; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; EVA-AL10 Build/HUAWEIEVA-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; Z3 Build/RB3N5C; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; JSN-AL00 Build/HONORJSN-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; MI 8 Lite Build/PKQ1.181007.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; VTR-AL00 Build/HUAWEIVTR-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; 16 X Build/OPM1.171019.026; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/65.0.3325.109 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OPPO R11s Plust Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; VCE-AL00 Build/HUAWEIVCE-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; vivo Y55L Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; Redmi Note 7 Pro Build/PKQ1.181203.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; DUA-TL00 Build/HONORDUA-TL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.97 Mobile Safari/537.36 huaweioem (8.0.0.362)",
        "Mozilla/5.0 (Linux; Android 8.1.0; PAAM00 Build/OPM1.171019.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1; OPPO A37m Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; OPPO R9s Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; MRD-AL00 Build/HUAWEIMRD-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.0.2; vivo X6Plus A Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; vivo Y85A Build/OPM1.171019.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; OPPO R9sk Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/46.0.2490.76 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; MI 9 SE Build/PKQ1.181121.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; OPPO A57 Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; GIONEE S10CL Build/NMF26F; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/56.0.2924.87 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.0; SM-G9009W Build/LRX21T; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; PRA-AL00X Build/HONORPRA-AL00X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; ARS-AL00 Build/HUAWEIARS-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; V1809A Build/OPM1.171019.026; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; FRD-AL10 Build/HUAWEIFRD-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; V1813A Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; EML-AL00 Build/HUAWEIEML-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; DUB-AL00 Build/HUAWEIDUB-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; GLK-AL00 Build/HUAWEIGLK-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.64 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; MI 8 SE Build/PKQ1.181121.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; Redmi Note 5 Build/OPM1.171019.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1; OPPO A59s Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; U; Android 9;zh-cn; BND-AL00 Build/HONORBND-AL00) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/57.0.2987.132 MQQBrowser/8.1 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; WAS-AL00 Build/HUAWEIWAS-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; V1831A Build/PPR1.180610.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; COR-AL10 Build/HUAWEICOR-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OD105 Build/NMF26F; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; MI 8 UD Build/PKQ1.180729.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0; vivo Y67A Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; OPPO A33 Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/42.0.2311.138 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; SAMSUNG SM-G9550 Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/9.3 Chrome/67.0.3396.87 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; YU FLY F9 Build/NMF26F; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/52.0.2743.100 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PBEM00 Build/OPM1.171019.026; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; PAR-AL00 Build/HUAWEIPAR-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; vivo Y66 Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OPPO A83t Build/N6F26Q; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; BKK-TL00 Build/HONORBKK-TL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.2; vivo X9s Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; PBBT00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.2; M6 Note Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; vivo X7Plus Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; vivo X7 Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0; M A5 Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/44.0.2403.143 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; DE106 Build/OPM1.171019.026; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; U; Android 5.1; zh-cn; OPPO A37m Build/LMY47I) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.134 Mobile Safari/537.36 OppoBrowser/4.3.8",
        "Mozilla/5.0 (Linux; Android 8.0.0; SM-G9350 Build/R16NW; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; vivo X20A Build/OPM1.171019.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; GIONEE S10L Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; PRA-TL10 Build/HONORPRA-TL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15F79",
        "Mozilla/5.0 (Linux; Android 8.1.0; PADT00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; PCT-AL10 Build/HUAWEIPCT-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 12_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148",
        "Mozilla/5.0 (Linux; Android 9; COL-AL10 Build/HUAWEICOL-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 4.4.4; OPPO R7s Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; LND-AL30 Build/HONORLND-AL30; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 4.4.4; vivo Y13iL Build/KTU84P; wv) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36 VivoBrowser/6.6.0.1",
        "Mozilla/5.0 (Linux; Android 8.1.0; DRA-AL00 Build/HUAWEIDRA-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; BND-AL00 Build/HONORBND-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OPPO R11 Plus Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; MI 6 Build/OPR1.170623.027; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; POT-AL00a Build/HUAWEIPOT-AL00a; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OPPO R11 Plusk Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; vivo X21A Build/OPM1.171019.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; TRT-AL00 Build/HUAWEITRT-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; TRT-TL10A Build/HUAWEITRT-TL10A; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.0.2; vivo X6A Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; V1809A Build/PKQ1.181030.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.2; vivo X9Plus Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; JMM-AL10 Build/HONORJMM-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; HRY-AL00Ta Build/HONORHRY-AL00Ta; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; vivo V3Max A Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; GIONEE GN5007 Build/NMF26F; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/52.0.2743.100 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; Redmi 6 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; JKM-AL00a Build/HUAWEIJKM-AL00a; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0; GN8003 Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; FLA-AL20 Build/HUAWEIFLA-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; U; Android 9; zh-cn; SM-A6050 Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/9.5 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; OPPO R9 Plusm A Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/56.0.2924.87 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; JSN-AL00a Build/HONORJSN-AL00a; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; vivo Y51A Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; LDN-AL10 Build/HUAWEILDN-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 4.4.4; Che1-CL20 Build/Che1-CL20) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; GIONEE S10B Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.0.2; vivo Y33 Build/LRX21M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; SM-G977N Build/PPR1.180610.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; SM-A7000 Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 Weibo (samsung-SM-A7000__weibo__9.4.3__android__android6.0.1)",
        "Mozilla/5.0 (Linux; Android 9; MHA-AL00 Build/HUAWEIMHA-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; VTR-TL00 Build/HUAWEIVTR-TL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0; HUAWEI VNS-AL00 Build/HUAWEIVNS-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; LND-AL40 Build/HONORLND-AL40; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; vivo Y51 Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OPPO A79t Build/N6F26Q; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 5.1.1; vivo X7Plus Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OPPO A83 Build/N6F26Q; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; JAT-AL00 Build/HONORJAT-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; LLD-AL20 Build/HONORLLD-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.0.0; LDN-AL00 Build/HUAWEILDN-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.1.1; OPPO A73 Build/N6F26Q; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; vivo Z3x Build/PKQ1.180819.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148",
        "Mozilla/5.0 (Linux; Android 9; INE-AL00 Build/HUAWEIINE-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044705 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 8.1.0; JSN-AL00a Build/HONORJSN-AL00a; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; HMA-AL00 Build/HUAWEIHMA-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 7.0; TRT-AL00 Build/HUAWEITRT-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/59.0.3071.125 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; LLD-AL00 Build/HONORLLD-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/72.0.3626.121 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0; HUAWEI MT7-TL10 Build/HuaweiMT7-TL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.105 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 9; PCAM10 Build/PPR1.180610.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57"
        ).random()
}

/**
 * WebView 登录之后，可以用此方法得到 Cookie
 */
fun getCookie(url: String): String? {
    throw RuntimeException("Stub!")
}

/**
 * 提示消息
 */
fun showToast(msg: String) {
    throw RuntimeException("Stub!")
}

/**
 * 获取当前正在播放的书籍
 * 2.1.1 开始加入
 * 2.1.7 改为 nullable
 */
fun getCurrentBook(): Book? {
    throw RuntimeException("Stub!")
}


/**
 * 加载多页章节列表时用到这个
 * 调用之后相关界面上会显示：正在加载章节列表: $pageInfo
 * 如果 pageInfo 传空，代表加载完毕
 */
fun notifyLoadingEpisodes(str: String?) {
    if (StringUtils.isEmpty(str)){
        return
    }
    RxBus.get().post(str);

}


/**
 * 适用于继承 AudioUrlExtractor，若需要嵌套调用多个 AudioUrlXXXExtractor 时务必调用此方法
 * backgroundTask: 网络请求或者耗时代码需要在后台线程处理, 返回一个 String
 * mainThreadCallback: 处理前者返回的String，并根据情况选择具体的 AudioUrlXXXExtractor (因为部分AudioUrlXXXExtractor需要在主线程执行）
 * 2.1.3 开始加入
 */
fun extractorAsyncExecute(
    url: String,
    autoPlay: Boolean,
    isCache: Boolean,
    isDebug: Boolean,
    backgroundTask: () -> String,
    mainThreadCallback: (String) -> Unit
): Disposable {
    throw RuntimeException("Stub!")
}

/**
 * 获取今天播放时长, 单位毫秒
 * 2.3.5 开始加入
 */
fun todayPlaybackTime(): Long {
    throw RuntimeException("Stub!")
}

/**
 * 控制播放器, 根据传入的 action 进行相应的操作:
 * play, pause, skipToNext, skipToPrevious, rewind, fastForward, stop
 * 2.3.5 开始加入
 */
fun playerControl(action: String) {
    throw RuntimeException("Stub!")
}

/**
 * 获取每个源自己的缓存目录
 * @param sourceId 源id
 * @param subDir 指定一个子目录，比如用来存放lrc
 * 2.5.1 开始加入
 */
fun getSourceCacheDir(sourceId: String, subDir: String): File {
    throw RuntimeException("Stub!")
}

//fun socketFactory(): SSLSocketFactory {
//    val trustManagerArr = arrayOf<TrustManager>(cls())
//    try {
//        val sSLContext = SSLContext.getInstance("TLS")
//        sSLContext.init(null, trustManagerArr, SecureRandom())
//        val socketFactory: SSLSocketFactory = sSLContext.socketFactory
//        Intrinsics.checkNotNullExpressionValue(socketFactory, "sslContext.socketFactory")
//        return socketFactory
//    } catch (e10: Exception) {
//        if (if (e10 is java.lang.RuntimeException) true else e10 is KeyManagementException) {
//            throw java.lang.RuntimeException("Failed to create a SSL socket factory", e10)
//        }
//        throw e10
//    }
//
//
//     class cls : X509TrustManager {
//        // javax.net.ssl.X509TrustManager
//        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
//            Intrinsics.checkNotNullParameter(chain, "chain")
//            Intrinsics.checkNotNullParameter(authType, "authType")
//        }
//
//        // javax.net.ssl.X509TrustManager
//        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
//            Intrinsics.checkNotNullParameter(chain, "chain")
//            Intrinsics.checkNotNullParameter(authType, "authType")
//        }
//
//        override fun getAcceptedIssuers(): Array<X509Certificate?> {
//            return arrayOfNulls<java.security.cert.X509Certificate>(0)
//        }
//    }
//}
