package com.xiaosheng.learnapp.utils;

public class SpManage {
    private static final String CONFIG = "tingshu.config";

    public static void setCurrentAudioUrl(String url){
        SpUtil.put(CONFIG,"current_audio_url", url);
    }
    public static String getCurrentAudioUrl(){
        return SpUtil.get(CONFIG,"current_audio_url", "");
    }

    public static void setCurrentBookUrl(String url){
        SpUtil.put(CONFIG,"current_book_url", url);
    }
    public static String getCurrentBookUrl(){
         return SpUtil.get(CONFIG,"current_book_url", "");
    }
}
