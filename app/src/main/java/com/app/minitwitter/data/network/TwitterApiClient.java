package com.app.minitwitter.data.network;

import com.app.minitwitter.retrofit.request.RequestLogin;
import com.app.minitwitter.retrofit.request.RequestSignUp;
import com.app.minitwitter.retrofit.response.ResponseAuth;
import com.app.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.minitwitter.retrofit.response.DeletedTweet;
import com.app.minitwitter.retrofit.response.Tweet;
import com.app.minitwitter.retrofit.response.UpdatedUser;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TwitterApiClient {

    @POST("auth/login")
    Call<ResponseAuth> doLogin( @Body RequestLogin requestLogin, @Header ("Authorization") String token);

    @POST("auth/register")
    Call<ResponseAuth> doSignUp(@Body RequestSignUp requestSignUp);

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTweet requestCreateTweet);

    @POST("tweets/like/{userId}&{tweetId}")
    Call<Tweet> likeTweet(@Path("userId") String userId, @Path("tweetId") String idTweet);

    @DELETE("tweets/{tweetId}")
    Call<DeletedTweet> deleteTweet(@Path("tweetId") String tweetId);

    @Multipart
    @POST("user/updateUserData")
    Call<UpdatedUser> uploadUserData(
                                        @Part("userId") RequestBody userId,
                                        @Part MultipartBody.Part image,
                                       @Part("username") RequestBody username,
                                       @Part("email") RequestBody email,
                                       @Part("password") RequestBody password);
}
