package com.app.minitwitter.data.network;

import com.app.minitwitter.core.RequestLogin;
import com.app.minitwitter.core.RequestSignUp;
import com.app.minitwitter.core.ResponseAuth;
import com.app.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TwitterApiClient {

    @POST("auth/login")
    Call<ResponseAuth> doLogin( @Body RequestLogin requestLogin, @Header ("Authorization") String token);

    @POST("auth/register")
    Call<ResponseAuth> doSignUp(@Body RequestSignUp requestSignUp);

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTweet requestCreateTweet);

}
