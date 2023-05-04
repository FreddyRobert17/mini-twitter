package com.app.minitwitter.data.network;

import com.app.minitwitter.core.RequestLogin;
import com.app.minitwitter.core.RequestSignUp;
import com.app.minitwitter.core.ResponseAuth;
import com.app.minitwitter.core.RetrofitHelper;
import com.app.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.minitwitter.retrofit.response.DeletedTweet;
import com.app.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class TwitterService {
    private Retrofit retrofit = RetrofitHelper.getInstance();

    public Call<ResponseAuth> doLogin(RequestLogin requestLogin, String token){
        return retrofit.create(TwitterApiClient.class).doLogin(requestLogin, token);
    }

    public Call<ResponseAuth> doSignUp(RequestSignUp requestSignUp) {
        return retrofit.create(TwitterApiClient.class).doSignUp(requestSignUp);
    }

    public Call<List<Tweet>> getAllTweets(){
        return retrofit.create(TwitterApiClient.class).getAllTweets();
    }

    public Call<Tweet> createTweet(RequestCreateTweet requestCreateTweet){
        return retrofit.create(TwitterApiClient.class).createTweet(requestCreateTweet);
    }

    public Call<Tweet> likeTweet(String userId, String idTweet){
        return retrofit.create(TwitterApiClient.class).likeTweet(userId, idTweet);
    }

    public Call<DeletedTweet> deleteTweet(String tweetId){
        return retrofit.create(TwitterApiClient.class).deleteTweet(tweetId);
    }
}
