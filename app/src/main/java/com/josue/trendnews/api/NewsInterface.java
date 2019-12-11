package com.josue.trendnews.api;

import com.josue.trendnews.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsInterface {

    @GET("top-headlines")
    Call <News> getNews(
            @Query("country") String country,
            @Query("apiKey") String apiKey    );

    @GET("everything")
    Call <News> getNewsSearch(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey  );
}
