package com.app.minitwitter.data.network;

import com.app.minitwitter.retrofit.request.RequestLogin;
import com.app.minitwitter.retrofit.request.RequestSignUp;
import com.app.minitwitter.retrofit.response.ResponseAuth;
import com.app.minitwitter.core.RetrofitHelper;
import com.app.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.minitwitter.retrofit.response.DeletedTweet;
import com.app.minitwitter.retrofit.response.Tweet;
import com.app.minitwitter.retrofit.response.UpdatedUser;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public Call<UpdatedUser> updateUserData(RequestBody userId, MultipartBody.Part image,
                                            RequestBody username, RequestBody email, RequestBody password){
         return retrofit.create(TwitterApiClient.class).uploadUserData(userId, image, username, email, password);
    }
}
