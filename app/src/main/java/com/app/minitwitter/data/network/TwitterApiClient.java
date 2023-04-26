package com.app.minitwitter.data.network;

import com.app.minitwitter.core.RequestLogin;
import com.app.minitwitter.core.RequestSignUp;
import com.app.minitwitter.core.ResponseAuth;

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

}
