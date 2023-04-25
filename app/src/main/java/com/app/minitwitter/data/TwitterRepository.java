package com.app.minitwitter.data;

import com.app.minitwitter.core.RequestLogin;
import com.app.minitwitter.core.RequestSignUp;
import com.app.minitwitter.core.ResponseAuth;
import com.app.minitwitter.data.network.TwitterService;

import retrofit2.Call;

public class TwitterRepository {
    TwitterService twitterService;

    public TwitterRepository(TwitterService twitterService){
        this.twitterService = twitterService;
    }

    public Call<ResponseAuth> doLogin(RequestLogin requestLogin){
        return twitterService.doLogin(requestLogin);
    }

    public Call<ResponseAuth> doSignUp(RequestSignUp requestSignUp){
        return twitterService.doSignUp(requestSignUp);
    }
}
