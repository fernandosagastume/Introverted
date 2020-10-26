package com.introverted;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    String ip = "192.168.0.5";
    String url = "http://"+ ip +":80/WebService-Introverted/";

    @FormUrlEncoded
    @POST("swipeCardInfo.php")
    Call<List<SwipeCardInfo>> getSwipeInfo(@Field("id_us")int id, @Field("type")String type);

    @FormUrlEncoded
    @POST("swipeCardInfo.php")
    Call<List<SwipeCardImages>> getProfilePics(@Field("id_us")int id);

    @FormUrlEncoded
    @POST("getMatchInfo.php")
    Call<List<MatchCarrouselInfo>> getMatchesInfo(@Field("id_us")int id);
}
