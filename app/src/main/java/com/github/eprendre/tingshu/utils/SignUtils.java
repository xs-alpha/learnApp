package com.github.eprendre.tingshu.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignUtils {
    private static final String BASE_URL = "https://www.ximalaya.com/revision/time";
    private static final String USER_AGENT = "YourUserAgent"; // Set your User-Agent here

    public static String sign() {
        String responseString = fetchResponse();
        String md5Hash = md5("himalaya-" + responseString);

        StringBuilder sb = new StringBuilder();
        sb.append(md5Hash);
        sb.append("["); // Equivalent to o0.a.ARRAY_BEGIN_TOKEN
        Random random = new Random();
        sb.append(random.nextInt(100));
        sb.append("]"); // Equivalent to o0.a.ARRAY_END_TOKEN
        sb.append(responseString);
        sb.append("["); // Equivalent to o0.a.ARRAY_BEGIN_TOKEN
        sb.append(random.nextInt(100));
        sb.append("]"); // Equivalent to o0.a.ARRAY_END_TOKEN
        sb.append(System.currentTimeMillis());

        return sb.toString();
    }

    private static String fetchResponse() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
