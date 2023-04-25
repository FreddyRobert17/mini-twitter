package com.app.minitwitter.data.network;

import com.app.minitwitter.core.RequestLogin;
import com.app.minitwitter.core.RequestSignUp;
import com.app.minitwitter.core.ResponseAuth;
import com.app.minitwitter.core.RetrofitHelper;

import retrofit2.Call;
import retrofit2.Retrofit;

public class TwitterService {
    private Retrofit retrofit = RetrofitHelper.getInstance();

    public Call<ResponseAuth> doLogin(RequestLogin requestLogin){
        return retrofit.create(TwitterApiClient.class).doLogin(requestLogin);
    }

    public Call<ResponseAuth> doSignUp(RequestSignUp requestSignUp) {
        return retrofit.create(TwitterApiClient.class).doSignUp(requestSignUp);
    }
}
