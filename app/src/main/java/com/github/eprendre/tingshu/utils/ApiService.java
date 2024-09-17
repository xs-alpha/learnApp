package com.github.eprendre.tingshu.utils;

import com.github.kittinunf.fuel.json.FuelJson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface ApiService {
    @GET
    Call<FuelJson> getResponse(@HeaderMap Map<String, String> headers, @retrofit2.http.Url String url);
}
