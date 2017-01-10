package com.inovatis.inovatis;


import com.inovatis.inovatis.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Lud' on 19/09/2016.
 */
public interface RequestInterface {

    @POST("inovatis_bdd/")
    Call<ServerResponse> operation(@Body com.inovatis.inovatis.models.ServerRequest request);


}
